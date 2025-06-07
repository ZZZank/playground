package org.example;

import java.util.ArrayList;

/**
 * @author ZZZank
 */
public interface Example {
    static Example get() {
        return new Impl();
    }

    default String getLength() {
        return "a string here";
    }

    class Impl extends ArrayList<Object> implements Example {
    }
}
