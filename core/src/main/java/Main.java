import caller.CallerClassGetter;
import lombok.val;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author ZZZank
 */
public class Main {

    public static void main(String[] args) throws IOException {
        var mani = new Manifest();
        mani.getMainAttributes().putValue("wow", "WOW");
        mani.getMainAttributes().putValue(Attributes.Name.MANIFEST_VERSION.toString(), "12");

        var path = Path.of("example.jar");

        try (var out = new JarOutputStream(Files.newOutputStream(path), mani)) {

        }

        var callerClass = CallerClassGetter.of(System.err::println).get();
        System.out.println("caller class: " + callerClass);
    }
}
