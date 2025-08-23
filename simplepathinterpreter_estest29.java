package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class SimplePathInterpreter_ESTestTest29 extends SimplePathInterpreter_ESTest_scaffolding {

    /**
     * Tests that `interpretSimpleLocationPath` throws a NullPointerException
     * when a step in the path contains a null predicate expression.
     * <p>
     * The method attempts to evaluate predicates, and a null value in the
     * predicate array leads to an NPE when its methods are invoked.
     */
    @Test
    public void interpretSimpleLocationPathWithNullPredicateThrowsNPE() {
        // Arrange
        // Create a step containing an array with a single null predicate expression.
        // This is the condition that should trigger the exception.
        Expression[] predicatesWithNull = { null };
        Step mockStep = mock(Step.class);
        doReturn(predicatesWithNull).when(mockStep).getPredicates();

        Step[] steps = { mockStep };

        // A starting node is required for the method signature.
        NullPointer startNode = new NullPointer(Locale.FRENCH, "id");

        // Act & Assert
        // The method is expected to throw a NullPointerException when it tries to
        // dereference the null predicate expression. The EvalContext can be null for
        // this test, as the exception occurs before it would be used by a valid predicate.
        assertThrows(NullPointerException.class, () -> {
            SimplePathInterpreter.interpretSimpleLocationPath(null, startNode, steps);
        });
    }
}