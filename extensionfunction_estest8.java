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

    /**
     * Tests that an exception thrown during the evaluation of an argument expression
     * is correctly propagated by the ExtensionFunction's compute() method.
     */
    @Test(timeout = 4000)
    public void computeShouldPropagateExceptionFromArgumentEvaluation() throws Throwable {
        // ARRANGE: Set up an ExtensionFunction with a complex argument that is expected
        // to throw an exception during its evaluation.
        QName functionName = new QName("test:faultyFunction");

        // The original auto-generated test created a convoluted expression equivalent to:
        // -( (NaN % -NaN) ).
        // In standard Java, this evaluates to NaN without an exception. However, the
        // test's goal is to verify that if an argument expression *does* throw,
        // the exception is propagated. We preserve the original structure to honor
        // the test's specific (though unusual) expectation of an ArithmeticException
        // from CoreOperationMod.
        Constant nanConstant = new Constant(Expression.NOT_A_NUMBER);
        CoreOperationMod moduloOfNans = new CoreOperationMod(nanConstant, new CoreOperationNegate(nanConstant));
        Expression faultyArgument = new CoreOperationNegate(moduloOfNans);

        Expression[] arguments = new Expression[2];
        arguments[0] = faultyArgument;
        // The second argument is intentionally null, which could also trigger an exception.
        arguments[1] = null;

        ExtensionFunction extensionFunction = new ExtensionFunction(functionName, arguments);

        // ACT & ASSERT: Call compute() and verify that the expected exception is thrown.
        try {
            extensionFunction.compute(null);
            fail("Expected an ArithmeticException to be thrown from the argument evaluation, but none occurred.");
        } catch (ArithmeticException e) {
            // SUCCESS: The expected exception was caught.
            assertEquals("The exception message should indicate division by zero.", "/ by zero", e.getMessage());

            // This helper method (from the test scaffolding) verifies that the exception
            // originated from the CoreOperationMod class, as the original test intended.
            verifyException("org.apache.commons.jxpath.ri.compiler.CoreOperationMod", e);
        }
    }
}