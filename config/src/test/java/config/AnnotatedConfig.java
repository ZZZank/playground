package config;

import config.annotation.AnnotatedConfigFactory;
import config.annotation.Config;

/**
 * @author ZZZank
 */
@Config(
    subConfigs = {AnnotatedConfig.AnnotatedSubConfig.class}
)
public class AnnotatedConfig {

    public static final ConfigRoot INSTANCE = AnnotatedConfigFactory.INSTANCE.create(
        AnnotatedConfig.class,
        null,
        null
    );

    public static boolean aBoolean = false;
    public static String aString = "www";
    public static int anInt = 42;

    public static class AnnotatedSubConfig {

        public static boolean aBoolean = true;
        public static String aString = "mmm";
        public static int anInt = 24;
    }
}
