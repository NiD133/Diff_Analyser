package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.CoreOperationSubtract;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link SimplePathInterpreter}.
 * This test focuses on edge cases and invalid arguments.
 */
public class SimplePathInterpreterTest {

    /**
     * Verifies that interpretSimpleExpressionPath throws a NullPointerException
     * when called with a null EvalContext.
     *
     * This test case was derived from an auto-generated test and uses a specific,
     * complex setup that is necessary to trigger the NullPointerException instead of
     * another exception type. The combination of predicates and a null context
     * exposes a specific code path that fails to handle the null context correctly.
     */
    @Test
    public void interpretSimpleExpressionPath_withNullContext_shouldThrowNullPointerException() {
        // ARRANGE
        // A root pointer to start the path evaluation from.
        NodePointer rootPointer = new NullPropertyPointer(new NullPointer(Locale.JAPANESE, ""));

        // An array of path steps. In this specific scenario, the elements are null.
        Step[] steps = new Step[3];

        // A specific sequence of predicates. The combination of a Constant followed by
        // a CoreOperation is crucial for triggering the desired execution path.
        Expression[] predicates = new Expression[8];
        Constant constant = new Constant("some_string");
        predicates[0] = constant;
        predicates[1] = new CoreOperationSubtract(constant, constant);

        // ACT & ASSERT
        // The method is expected to throw a NullPointerException because the provided
        // evaluation context is null, and the internal logic attempts to dereference it
        // under the conditions set up above.
        assertThrows(NullPointerException.class, () -> {
            SimplePathInterpreter.interpretSimpleExpressionPath(null, rootPointer, predicates, steps);
        });
    }
}