package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNegate;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Contains tests for edge cases in {@link SimplePathInterpreter}.
 */
public class SimplePathInterpreterTest {

    /**
     * Tests that {@code interpretSimpleExpressionPath} correctly handles a predicate
     * with an index derived from negating {@code Integer.MIN_VALUE}.
     *
     * <p>This test covers a specific edge case in index calculation. The expression
     * {@code - (Integer.MIN_VALUE)} is evaluated using floating-point arithmetic,
     * resulting in {@code 2.147483648E9}. When converted to an integer, this becomes
     * {@code Integer.MAX_VALUE}. The path interpreter then subtracts 1 to convert
     * the 1-based XPath index to a 0-based index, resulting in {@code Integer.MAX_VALUE - 1}.
     *
     * <p>This test verifies that the final NodePointer has this correctly calculated large index.
     */
    @Test
    public void interpretPathWithLargeIndexFromNegatedMinValuePredicate() {
        // ARRANGE
        // 1. Create the predicate expression: - (Integer.MIN_VALUE)
        final Constant minValueConstant = new Constant(Integer.MIN_VALUE);
        final Expression largeIndexPredicate = new CoreOperationNegate(minValueConstant);
        final Expression[] predicates = {largeIndexPredicate};

        // 2. Mock a path step that uses this predicate. The axis value is arbitrary
        // but necessary for the mock setup to test this specific execution path.
        final Step mockStep = mock(Step.class);
        doReturn(Integer.MIN_VALUE).when(mockStep).getAxis();
        doReturn(predicates).when(mockStep).getPredicates();
        final Step[] pathSteps = {mockStep};

        // 3. Set up the root node for the path evaluation.
        final QName rootQName = new QName("root");
        final NodePointer rootPointer = NodePointer.newNodePointer(rootQName, "someBean", Locale.getDefault());
        final int initialRootIndex = rootPointer.getIndex();

        // A new NodePointer's index defaults to WHOLE_COLLECTION. This assertion
        // confirms our starting state and makes the final assertion clearer.
        assertEquals("Pre-condition: Root pointer index should be WHOLE_COLLECTION",
                NodePointer.WHOLE_COLLECTION, initialRootIndex);

        // ACT
        // The method is called with the same expression array for both the top-level
        // predicates and for the predicates within the mock step.
        final NodePointer resultPointer = SimplePathInterpreter.interpretSimpleExpressionPath(
                null, rootPointer, predicates, pathSteps);

        // ASSERT
        // The expected index is Integer.MAX_VALUE - 1. See test Javadoc for the calculation.
        final int expectedIndex = Integer.MAX_VALUE - 1;
        assertEquals("The resulting pointer should have an index calculated from the large value predicate.",
                expectedIndex, resultPointer.getIndex());

        // Also, verify that the original root pointer was not modified.
        assertEquals("The root pointer's index should remain unchanged.",
                initialRootIndex, rootPointer.getIndex());
    }
}