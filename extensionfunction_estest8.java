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

public class ExtensionFunction_ESTestTest8 extends ExtensionFunction_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        QName qName0 = new QName(",", "");
        Double double0 = Expression.NOT_A_NUMBER;
        Constant constant0 = new Constant(double0);
        CoreOperationNegate coreOperationNegate0 = new CoreOperationNegate(constant0);
        Expression[] expressionArray0 = new Expression[2];
        CoreOperationMod coreOperationMod0 = new CoreOperationMod(constant0, coreOperationNegate0);
        CoreOperationNegate coreOperationNegate1 = new CoreOperationNegate(coreOperationMod0);
        expressionArray0[0] = (Expression) coreOperationNegate1;
        ExtensionFunction extensionFunction0 = new ExtensionFunction(qName0, expressionArray0);
        // Undeclared exception!
        try {
            extensionFunction0.compute((EvalContext) null);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // / by zero
            //
            verifyException("org.apache.commons.jxpath.ri.compiler.CoreOperationMod", e);
        }
    }
}
