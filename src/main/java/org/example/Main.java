package org.example;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.IntFunction;

/**
 * @author ZZZank
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        testPath();
    }

    private static void testPath() {
        var root = Path.of("");
        var sub = root.resolve("kubejs");
        log(root.toAbsolutePath());
        log(sub.toAbsolutePath());
        log(sub.relativize(root));
        log(root.relativize(sub));
    }

    private static void testHash() {
        var INT_PHI = 0x9E3779B9;
        var mix = (IntFunction<Integer>) (final int x) -> {
            final int h = x * INT_PHI;
            return h ^ (h >>> 16);
        };
        var toTest = new int[]{1, 0, 0};
        log(String.format(
            "%s hash: %s",
            Arrays.toString(toTest),
            mix.apply(mix.apply(toTest[0]) + toTest[1]) + toTest[2]
        ));
    }


    public static void log(Object o) {
        System.out.println(o);
    }
}