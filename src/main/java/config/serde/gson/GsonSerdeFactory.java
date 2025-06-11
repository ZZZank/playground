package config.serde.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import utils.Asser;
import config.serde.ConfigSerde;
import config.serde.ConfigSerdeFactory;

import java.lang.reflect.Type;

/**
 * @author ZZZank
 */
public class GsonSerdeFactory implements ConfigSerdeFactory<JsonElement> {
    private final Gson gson;

    public GsonSerdeFactory(Gson gson) {
        this.gson = Asser.tNotNull(gson, "gson");
    }

    @Override
    public ConfigSerde<JsonElement, ?> getSerde(Type type) {
        try {
            return new GsonAdapterSerde<>(gson.getAdapter(TypeToken.get(type)));
        } catch (Exception e) {
            return null;
        }
    }
}
