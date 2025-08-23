package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Locale;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.NodeSetContext;
import org.apache.commons.jxpath.ri.axes.RootContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPointer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class ExtensionFunction_ESTestTest4 extends ExtensionFunction_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        QName qName0 = new QName("org.apache.commons.jxpath.ri.Parser");
        ExtensionFunction extensionFunction0 = new ExtensionFunction(qName0, (Expression[]) null);
        BasicNodeSet basicNodeSet0 = new BasicNodeSet();
        NodeSetContext nodeSetContext0 = new NodeSetContext((EvalContext) null, basicNodeSet0);
        JXPathContextReferenceImpl jXPathContextReferenceImpl0 = (JXPathContextReferenceImpl) JXPathContext.newContext((Object) nodeSetContext0);
        Locale locale0 = Locale.PRC;
        BeanPointer beanPointer0 = (BeanPointer) NodePointer.newNodePointer(qName0, nodeSetContext0, locale0);
        RootContext rootContext0 = new RootContext(jXPathContextReferenceImpl0, beanPointer0);
        // Undeclared exception!
        try {
            extensionFunction0.computeValue(rootContext0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Cannot invoke extension function org.apache.commons.jxpath.ri.Parser
            //
            verifyException("org.apache.commons.jxpath.PackageFunctions", e);
        }
    }
}
