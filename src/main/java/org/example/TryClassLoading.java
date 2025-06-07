package org.example;

import java.util.List;

/**
 * @author ZZZank
 */
public class TryClassLoading {
    public static void main(String[] args) {
        try {
            var debug = Inner.DEBUG;
            throw new RuntimeException("Inner should at least throw something");
        } catch (Throwable e) {
            System.out.println("error captured");
            e.printStackTrace();
        }
    }

    public List<Inner> referInGeneric() {
        return null;
    }

    public abstract static class Inner {
        public static final boolean DEBUG;
        static {
            DEBUG = true;
            if (DEBUG) {
                throw new IllegalStateException("how dare you loading me");
            }
        }
    }
}
