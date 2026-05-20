package compile;

import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.Phases;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * @author ZZZank
 */
public class Compil {

    public static void main(String[] args) {
        var configuration = new CompilerConfiguration();
        configuration.setTargetBytecode(CompilerConfiguration.JDK8);
        configuration.setOptimizationOptions(Map.of(
            CompilerConfiguration.INVOKEDYNAMIC, true,
            "asmResolving", true,
            "classLoaderResolving", false
        ));

        var unit = new CompilationUnit(configuration);
        unit.setClassNodeResolver(new TransformingClassNodeResolver());

        unit.addSource("try_it.groovy", """
            import zzzank.probejs.utils.CollectUtils
            import groovy.transform.CompileStatic

            @CompileStatic
            class AAA {
                void run() {
                    println(CollectUtils.ofList("easy"))
                }
            }
            """);

        unit.compile(Phases.CLASS_GENERATION);

        for (var groovyClass : unit.getClasses()) {
            System.out.println("Name: " + groovyClass.getName());
            try (var out = Files.newOutputStream(Path.of("./" + groovyClass.getName() + ".class"))) {
                out.write(groovyClass.getBytes());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
