package config.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author ZZZank
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    Class<?>[] subConfigs() default {};

    boolean innerClassAsSubConfig() default true;

    /**
     * @author ZZZank
     */
    @Retention(RetentionPolicy.RUNTIME)
    @interface Entry {
        String name() default "";

        String[] comments() default "";
    }
}
