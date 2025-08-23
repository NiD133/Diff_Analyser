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

public class ExtensionFunction_ESTestTest12 extends ExtensionFunction_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Expression[] expressionArray0 = new Expression[0];
        ExtensionFunction extensionFunction0 = new ExtensionFunction((QName) null, expressionArray0);
        JXPathContextReferenceImpl jXPathContextReferenceImpl0 = mock(JXPathContextReferenceImpl.class, new ViolatedAssumptionAnswer());
        doReturn((Function) null).when(jXPathContextReferenceImpl0).getFunction(any(org.apache.commons.jxpath.ri.QName.class), any(java.lang.Object[].class));
        doReturn((NamespaceResolver) null).when(jXPathContextReferenceImpl0).getNamespaceResolver();
        VariablePointer variablePointer0 = new VariablePointer((QName) null);
        RootContext rootContext0 = new RootContext(jXPathContextReferenceImpl0, variablePointer0);
        // Undeclared exception!
        try {
            extensionFunction0.computeValue(rootContext0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // No such function: null[]
            //
            verifyException("org.apache.commons.jxpath.ri.compiler.ExtensionFunction", e);
        }
    }
}
