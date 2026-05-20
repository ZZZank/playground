package compile;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.decompiled.AsmDecompiler;
import org.codehaus.groovy.ast.decompiled.AsmReferenceResolver;
import org.codehaus.groovy.ast.decompiled.ClassStub;
import org.codehaus.groovy.ast.decompiled.DecompiledClassNode;
import org.codehaus.groovy.control.ClassNodeResolver;
import org.codehaus.groovy.control.CompilationUnit;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

/**
 * @author ZZZank
 */
public class TransformingClassNodeResolver extends ClassNodeResolver {

    private final ClassLoader loader;

    public TransformingClassNodeResolver(ClassLoader loader) {
        this.loader = loader;
    }

    public TransformingClassNodeResolver() {
        try {
            this.loader = new URLClassLoader(new URL[]{
                Path.of("./groovy/libs/pjsl.jar").toUri().toURL()
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LookupResult findClassNode(String name, CompilationUnit compilationUnit) {
        if (compilationUnit != null) {
            var resource = loader.getResource(name.replace('.', '/') + ".class");

            ClassStub stub = null;
            if (resource != null) {
                try {
                    // TODO: replace this
                    stub = AsmDecompiler.parseClass(resource);
                } catch (IOException ignored) {
                }
            }

            ClassNode node = null;
            if (stub != null) {
                node = new DecompiledClassNode(stub, new AsmReferenceResolver(this, compilationUnit));
                if (!node.getName().equals(name)) {
                    // this may happen under Windows because getResource is case-insensitive under that OS!
                    node = null;
                }
            }

            if (node != null) {
                return new LookupResult(null, node);
            }
        }
        return super.findClassNode(name, compilationUnit);
    }
}
