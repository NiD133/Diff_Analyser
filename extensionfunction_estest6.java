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

public class ExtensionFunction_ESTestTest6 extends ExtensionFunction_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        QName qName0 = new QName("", "");
        Expression[] expressionArray0 = new Expression[1];
        Double double0 = Expression.NOT_A_NUMBER;
        Constant constant0 = new Constant(double0);
        CoreOperationNegate coreOperationNegate0 = new CoreOperationNegate(constant0);
        CoreOperationMultiply coreOperationMultiply0 = new CoreOperationMultiply(coreOperationNegate0, coreOperationNegate0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual0 = new CoreOperationLessThanOrEqual(coreOperationMultiply0, coreOperationNegate0);
        expressionArray0[0] = (Expression) coreOperationLessThanOrEqual0;
        ExtensionFunction extensionFunction0 = new ExtensionFunction(qName0, expressionArray0);
        JXPathContextReferenceImpl jXPathContextReferenceImpl0 = (JXPathContextReferenceImpl) JXPathContext.newContext((JXPathContext) null, (Object) coreOperationLessThanOrEqual0);
        BeanPointer beanPointer0 = (BeanPointer) NodePointer.newChildNodePointer((NodePointer) null, qName0, double0);
        RootContext rootContext0 = new RootContext(jXPathContextReferenceImpl0, beanPointer0);
        Object object0 = new Object();
        InitialContext initialContext0 = (InitialContext) rootContext0.getConstantContext(object0);
        // Undeclared exception!
        try {
            extensionFunction0.compute(initialContext0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Undefined function: :
            //
            verifyException("org.apache.commons.jxpath.ri.JXPathContextReferenceImpl", e);
        }
    }
}
