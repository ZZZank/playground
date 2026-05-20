package compile;

import org.codehaus.groovy.ast.decompiled.AsmReferenceResolver;
import org.codehaus.groovy.ast.decompiled.ClassStub;
import org.codehaus.groovy.ast.decompiled.DecompiledClassNode;

/**
 * @author ZZZank
 */
public class TransformedDecompilingClassNode extends DecompiledClassNode {
    public TransformedDecompilingClassNode(ClassStub classData, AsmReferenceResolver resolver) {
        super(classData, resolver);
    }
}
