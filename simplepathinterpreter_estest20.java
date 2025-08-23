package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreFunction;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Contains tests for the {@link SimplePathInterpreter} class, focusing on exception handling.
 */
public class SimplePathInterpreter_ESTestTest20 {

    /**
     * Tests that SimplePathInterpreter.createNullPointer throws a JXPathException
     * when a step's predicate contains a function with an incorrect number of arguments.
     * The evaluation of the predicate should fail, leading to an exception.
     */
    @Test
    public void createNullPointerWithInvalidPredicateFunctionThrowsException() {
        // ARRANGE
        // The XPath function "substring()" requires 2 or 3 arguments. We provide zero
        // to create an invalid predicate that will cause an exception during evaluation.
        Expression[] invalidPredicates = {
                new CoreFunction(Compiler.FUNC_SUBSTRING, new Expression[0])
        };

        // Create a mock Step that uses the invalid predicate.
        Step stepWithInvalidPredicate = mock(Step.class);
        when(stepWithInvalidPredicate.getAxis()).thenReturn(Compiler.AXIS_CHILD);
        when(stepWithInvalidPredicate.getPredicates()).thenReturn(invalidPredicates);
        Step[] steps = {stepWithInvalidPredicate};

        NodePointer startPointer = new NullPointer(Locale.ENGLISH, "test-id");
        EvalContext context = null; // The method is called with a null context in the original test

        // ACT & ASSERT
        // Expect a JXPathException because the "substring" function is called incorrectly.
        JXPathException exception = assertThrows(JXPathException.class, () ->
                SimplePathInterpreter.createNullPointer(context, startPointer, steps, 0)
        );

        // Verify the exception message clearly indicates the source of the error.
        String expectedMessageFragment = "Incorrect number of arguments: substring";
        assertTrue(
                "Exception message should indicate incorrect arguments for the 'substring' function.",
                exception.getMessage().contains(expectedMessageFragment)
        );
    }
}