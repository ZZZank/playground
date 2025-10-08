package config.test_obj;

import config.ConfigRoot;
import config.annotation.AnnotatedConfigFactory;
import config.annotation.Config;
import config.impl.ConfigTreePrinter;
import config.impl.io.PropertiesConfigIO;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * @author ZZZank
 */
@SuppressWarnings("unused")
@Config
public class ExampleConfig {

    public static final Supplier<ConfigRoot> ROOT = AnnotatedConfigFactory.create(
        ExampleConfig.class,
        new PropertiesConfigIO(),
        Path.of("run/example.cfg")
    );

    public static void main(String[] args) {
        new ConfigTreePrinter(ROOT.get()).print().forEach(System.out::println);
    }

    public static boolean aBoolean = false;
    public static String aString = "www";
    public static int anInt = 42;

    public static class ExampleSub {
        @Config.Entry(name = "someRandomBool")
        public static boolean aBoolean = true;
        @Config.Entry(name = "someRandomStr", comments = "wow")
        public static String aString = "mmm";
        public static int anInt = 24;
    }

    public static class ExampleSub2 {

        public static List<String> stringList = List.of();
        public static String aString = "mmm";
        public static int anInt = 24;

        public static class ExampleSub2Sub {

            public static Pattern[] regexArray = new Pattern[0];
        }
    }
}
