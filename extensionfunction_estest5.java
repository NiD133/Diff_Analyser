package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// The original test class name is kept for consistency, though a more descriptive
// name like 'ExtensionFunctionTest' would be preferable in a real-world scenario.
public class ExtensionFunction_ESTestTest5 {

    /**
     * Tests that ExtensionFunction#computeValue correctly propagates exceptions
     * that occur during the evaluation of its argument expressions.
     */
    @Test
    public void computeValueShouldPropagateExceptionFromArgumentEvaluation() {
        // Arrange
        // 1. Create a mock argument expression that is designed to fail.
        Expression faultyArgument = mock(Expression.class);
        ArithmeticException expectedException = new ArithmeticException("Intended failure for testing");
        
        // Configure the mock to throw an exception when its 'compute' method is called.
        // The 'compute' method is invoked internally by ExtensionFunction#computeValue.
        when(faultyArgument.compute(any(EvalContext.class))).thenThrow(expectedException);

        // 2. Create an array of arguments including the faulty one.
        Expression[] functionArguments = new Expression[]{
                new Constant("valid argument"), // A valid argument to ensure the loop progresses
                faultyArgument                 // The argument that will throw the exception
        };

        // 3. Instantiate the ExtensionFunction with a clear name and the arguments.
        QName functionName = new QName("test:my-function");
        ExtensionFunction extensionFunction = new ExtensionFunction(functionName, functionArguments);

        // Act & Assert
        try {
            // This call should trigger the evaluation of `faultyArgument`, causing an exception.
            extensionFunction.computeValue(null);
            fail("Expected an ArithmeticException to be thrown, but the method completed without error.");
        } catch (ArithmeticException actualException) {
            // Verify that the caught exception is the exact instance we expected.
            // This confirms the exception was propagated correctly without being wrapped or altered.
            assertEquals("The propagated exception should be the same as the one thrown by the argument.",
                    expectedException, actualException);
        }
    }
}