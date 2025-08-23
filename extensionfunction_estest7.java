package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;

/**
 * Contains tests for the {@link ExtensionFunction} class.
 */
public class ExtensionFunction_ESTestTest7 extends ExtensionFunction_ESTest_scaffolding {

    /**
     * Tests that calling compute() on an ExtensionFunction with a circularly referenced
     * argument expression results in a StackOverflowError. This simulates an
     * infinitely recursive expression evaluation.
     */
    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void computeWithCircularArgumentShouldThrowStackOverflowError() {
        // Arrange: Create an expression that contains itself as an argument, forming a circular reference.
        // We use CoreOperationNegate as a concrete Expression subclass for this setup.
        CoreOperationNegate selfReferencingExpression = new CoreOperationNegate(new Constant(0));

        // To create the circular reference, we must manipulate the internal 'args' array.
        // 1. Create an array that will hold the arguments.
        Expression[] arguments = new Expression[1];

        // 2. Point the expression's internal argument list to our new array.
        //    This is a white-box approach necessary for this specific test scenario.
        selfReferencingExpression.args = arguments;

        // 3. Place the expression itself into its own argument list, completing the cycle.
        arguments[0] = selfReferencingExpression;

        // Now, create the ExtensionFunction with this problematic, recursive argument.
        QName functionName = new QName("test", "recursive-function");
        ExtensionFunction extensionFunction = new ExtensionFunction(functionName, arguments);

        // Act: Attempt to compute the value of the function.
        // This will trigger an infinite recursion while trying to evaluate the
        // self-referencing argument, which is expected to throw a StackOverflowError.
        extensionFunction.compute(null); // EvalContext is not relevant for triggering the error.
    }
}