package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// The original test class name and inheritance are kept for context.
public class SimplePathInterpreter_ESTestTest12 extends SimplePathInterpreter_ESTest_scaffolding {

    /**
     * Verifies that interpretSimpleLocationPath throws a NullPointerException
     * when it must evaluate predicates but is given a null EvalContext.
     * <p>
     * The predicate evaluation logic requires a valid context to function, and
     * providing null should result in an immediate failure.
     */
    @Test(expected = NullPointerException.class)
    public void interpretSimpleLocationPath_whenStepHasPredicatesAndContextIsNull_throwsNullPointerException() {
        // ARRANGE
        // A root node to start the path interpretation from. Its specific value is not important.
        NodePointer rootPointer = NodePointer.newNodePointer(new QName("root"), new Object(), Locale.getDefault());

        // A mock step that has at least one predicate. This is crucial to force
        // the interpreter to enter the predicate evaluation code path.
        Expression[] predicates = {mock(Expression.class)};
        Step mockStepWithPredicate = mock(Step.class);
        when(mockStepWithPredicate.getPredicates()).thenReturn(predicates);

        Step[] steps = {mockStepWithPredicate};

        // ACT
        // Call the method under test with a null EvalContext.
        // This should trigger an NPE when the method attempts to process the predicates.
        SimplePathInterpreter.interpretSimpleLocationPath(null, rootPointer, steps);

        // ASSERT
        // The @Test(expected = NullPointerException.class) annotation asserts that the
        // expected exception is thrown, so no further assertion is needed.
    }
}