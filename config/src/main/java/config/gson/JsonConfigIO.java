package config.gson;

import com.google.gson.*;
import config.ConfigIO;
import config.impl.io.SerdeHolder;
import lombok.val;
import utils.Asser;
import utils.Cast;
import config.prop.ConfigProperty;
import config.ConfigSerde;
import config.ConfigCategory;
import config.ConfigRoot;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author ZZZank
 */
public class JsonConfigIO extends SerdeHolder<JsonElement> implements ConfigIO {
    public static final String DEFAULT_VALUE_KEY = "$default";
    public static final String VALUE_KEY = "$value";
    public static final String COMMENTS_KEY = "$comment";

    public static final ConfigProperty<ConfigSerde<JsonElement, ?>> PROP_SERDE =
        ConfigProperty.register("json_io_serde", null);

    private final Gson gson;

    public static JsonConfigIO make(Gson gson, Consumer<JsonConfigIO> modifier) {
        var io = new JsonConfigIO(gson);
        modifier.accept(io);
        return io;
    }

    public JsonConfigIO(Gson gson) {
        super(PROP_SERDE);
        this.gson = Asser.tNotNull(gson, "gson");
    }

    @Override
    public void read(ConfigRoot config, Reader reader) {
        var json = gson.fromJson(reader, JsonObject.class);
        readCategory(config, json);
    }

    private void readCategory(ConfigCategory category, JsonObject config) {
        for (var entry : category.get().values()) {
            var name = entry.name();
            var entryInConfig = config.getAsJsonObject(name);
            if (entryInConfig == null) {
                continue;
            }
            if (entry.isCategory()) {
                readCategory(entry.asCategory(), entryInConfig);
                continue;
            }
            var valueInConfig = entryInConfig.get(VALUE_KEY);
            if (valueInConfig == null) {
                continue;
            }
            var serde = getSerde(entry);
            entry.set(Cast.to(serde.deserialize(valueInConfig)));
        }
    }

    @Override
    public void save(ConfigRoot config, Writer writer) {
        gson.toJson(writeCategory(config), writer);
    }

    private JsonObject writeCategory(ConfigCategory category) {
        var writeTo = new JsonObject();

        for (var entry : category.get().values()) {
            var name = entry.name();
            if (entry.isCategory()) {
                writeTo.add(name, writeCategory(entry.asCategory()));
                continue;
            }

            var serde = getSerde(entry);

            var entryJson = new JsonObject();

            entryJson.add(DEFAULT_VALUE_KEY, serde.serialize(Cast.to(entry.binding().getDefault())));
            entryJson.add(VALUE_KEY, serde.serialize(Cast.to(entry.get())));
            var comments = entry.getProp(ConfigProperty.COMMENTS).orElse(Collections.emptyList());
            switch (comments.size()) {
                case 0 -> {
                }
                case 1 -> entryJson.add(COMMENTS_KEY, new JsonPrimitive(comments.get(0)));
                default -> entryJson.add(COMMENTS_KEY, parseObject(comments));
            }

            writeTo.add(name, entryJson);
        }

        return writeTo;
    }

    private static JsonElement parseObject(Object obj) {
        if (obj == null) {
            return JsonNull.INSTANCE;
        } else if (obj instanceof Number number) {
            return new JsonPrimitive(number);
        } else if (obj instanceof String string) {
            return new JsonPrimitive(string);
        } else if (obj instanceof Boolean bool) {
            return new JsonPrimitive(bool);
        } else if (obj instanceof Character c) {
            return new JsonPrimitive(c);
        } else if (obj instanceof List<?> list) {
            val jsonArray = new JsonArray();
            for (val o : list) {
                jsonArray.add(parseObject(o));
            }
            return jsonArray;
        } else if (obj instanceof Map<?, ?> map) {
            val object = new JsonObject();
            for (val entry : map.entrySet()) {
                val key = entry.getKey();
                val value = entry.getValue();
                object.add(String.valueOf(key), parseObject(value));
            }
            return object;
        } else if (obj.getClass().isArray()) {
            val length = Array.getLength(obj);
            val jsonArray = new JsonArray();
            for (int i = 0; i < length; i++) {
                jsonArray.add(parseObject(Array.get(obj, i)));
            }
            return jsonArray;
        }
        return JsonNull.INSTANCE;
    }
}
