package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.junit.Test;

import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Contains tests for the static utility method {@code SimplePathInterpreter.createNullPointer}.
 *
 * Note: The original test class name "SimplePathInterpreter_ESTestTest8" was preserved
 * to maintain structural consistency with the original code, though it is unconventional.
 */
public class SimplePathInterpreter_ESTestTest8 {

    /**
     * Tests that createNullPointer throws a NullPointerException when a step's predicate
     * cannot be interpreted and the provided EvalContext is null.
     * <p>
     * The method falls back to an error-handling path that requires a non-null context
     * to generate a descriptive exception message. Passing a null context in this
     * scenario is expected to cause an NPE.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void createNullPointerWithUninterpretablePredicateAndNullContextThrowsNPE() {
        // Arrange: Create a step with a predicate that cannot be simply interpreted
        // (i.e., not a NameAttributeTest and not a number). This forces the method
        // into its fallback error-handling logic.
        Expression[] uninterpretablePredicates = {new Constant("a-string-predicate")};
        Step mockStep = mock(Step.class);

        // The method checks getNodeTest() early, so it must be stubbed to proceed.
        when(mockStep.getNodeTest()).thenReturn(new NodeNameTest(new QName("anyNode")));
        when(mockStep.getPredicates()).thenReturn(uninterpretablePredicates);

        Step[] steps = {mockStep};
        NodePointer parentPointer = new NullPointer(new QName("parent"), Locale.getDefault());

        // Act: Call the method with a null EvalContext.
        // The NPE is expected when the method tries to use the null context to build
        // an error message about the uninterpretable path.
        SimplePathInterpreter.createNullPointer(null, parentPointer, steps, 0);

        // Assert: The 'expected' attribute of the @Test annotation verifies the NPE.
    }
}