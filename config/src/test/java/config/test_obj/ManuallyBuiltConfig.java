package config.test_obj;

import config.ConfigEntry;
import config.ConfigRoot;
import config.impl.io.PropertiesConfigIO;

import java.nio.file.Path;
import java.util.List;

/**
 * @author ZZZank
 */
public class ManuallyBuiltConfig {

    public static final ConfigRoot ROOT = ConfigRoot.of(
        new PropertiesConfigIO(),
        Path.of("run/%s.properties".formatted(ManuallyBuiltConfig.class.getName()))
    );

    public static final ConfigEntry<Integer> MAX_INT = ROOT.define("max_int")
        .bindDefault(10)
        .comment("well well well")
        .build();

    public static final ConfigEntry<Double> MAX_DBL = ROOT.define("max_dbl")
        .bindRanged(3.0, 2.0, 5.0)
        .comment("ranged double config entry")
        .build();

    public static final ConfigEntry<List<String>> LIST_STR = ROOT.define("list_str")
        .bindDefaultAndBuild(List.of());

    static {
        ROOT.fillEntryType(ManuallyBuiltConfig.class);
    }
}
