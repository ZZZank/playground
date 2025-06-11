package config.io;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import utils.Asser;
import utils.Cast;
import utils.JsonUtils;
import config.prop.ConfigProperty;
import config.serde.ConfigSerde;
import config.struct.ConfigCategory;
import config.struct.ConfigRoot;

import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
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
                default -> entryJson.add(COMMENTS_KEY, JsonUtils.parseObject(comments));
            }

            writeTo.add(name, entryJson);
        }

        return writeTo;
    }
}
