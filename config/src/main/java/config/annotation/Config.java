package config.annotation;

/**
 * @author ZZZank
 */
public @interface Config {

    Class<?>[] subConfigs() default {};
}
