package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.CoreFunction;
import org.apache.commons.jxpath.ri.compiler.CoreOperationSubtract;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimplePathInterpreterTest {

    /**
     * Tests that interpretSimpleLocationPath throws a RuntimeException when a step
     * contains a predicate with a function that has an incorrect number of arguments.
     */
    @Test
    public void interpretSimpleLocationPathThrowsExceptionForPredicateWithInvalidFunctionArguments() {
        // ARRANGE
        // 1. Set up a root node pointer for the path evaluation to start from.
        QName rootQName = new QName("root");
        NodePointer rootPointer = NodePointer.newNodePointer(rootQName, new Object(), Locale.getDefault());

        // 2. Create a deliberately invalid predicate expression to trigger an error.
        // We will use the 'substring-after' function, which requires two arguments,
        // but construct the expression tree to provide only one.
        Expression[] predicates = new Expression[1];

        // The function code 13 corresponds to 'substring-after'.
        final int SUBSTRING_AFTER_FUNCTION_CODE = 13;
        CoreFunction substringAfterFunction = new CoreFunction(SUBSTRING_AFTER_FUNCTION_CODE, predicates);

        // Create a subtraction operation. This operation will serve as the single argument
        // to the 'substring-after' function.
        CoreOperationSubtract subtractOperation = new CoreOperationSubtract(substringAfterFunction, substringAfterFunction);

        // Set the step's predicate to be this operation. When the path interpreter evaluates
        // this predicate, it will first compute the subtraction, then pass the single
        // result to 'substring-after', causing an "Incorrect number of arguments" error.
        predicates[0] = subtractOperation;

        // 3. Mock a path Step that uses this invalid predicate.
        Step mockStepWithInvalidPredicate = mock(Step.class);
        when(mockStepWithInvalidPredicate.getAxis()).thenReturn(Compiler.AXIS_CHILD);
        when(mockStepWithInvalidPredicate.getPredicates()).thenReturn(predicates);

        // The path consists of our mocked step. The second step is not needed as the
        // evaluation will fail on the first step's predicate.
        Step[] steps = {mockStepWithInvalidPredicate, null};

        // ACT & ASSERT
        try {
            // The method under test is expected to evaluate the predicate and throw an exception.
            SimplePathInterpreter.interpretSimpleLocationPath((EvalContext) null, rootPointer, steps);
            fail("Expected a RuntimeException due to an invalid function call in the predicate.");
        } catch (RuntimeException e) {
            // VERIFY
            // Check that the exception is the one we expect from CoreFunction's argument validation.
            String expectedMessagePrefix = "Incorrect number of arguments: substring-after";
            assertTrue(
                "Exception message should indicate incorrect argument count for the function.",
                e.getMessage().startsWith(expectedMessagePrefix)
            );
        }
    }
}