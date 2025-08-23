package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.CoreOperationMod;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimplePathInterpreter_ESTestTest15 extends SimplePathInterpreter_ESTest_scaffolding {

    /**
     * Tests that an ArithmeticException thrown during predicate evaluation
     * is propagated up from the interpretSimpleLocationPath method.
     */
    @Test(timeout = 4000, expected = ArithmeticException.class)
    public void interpretSimpleLocationPathWithModuloByZeroPredicateThrowsArithmeticException() {
        // Arrange: Create a step with a predicate that will cause a "division by zero" error.
        // The predicate is equivalent to an XPath expression like "[1 mod 0]".
        Expression dividend = new Constant(1);
        Expression divisor = new Constant(0);
        Expression modByZeroPredicate = new CoreOperationMod(dividend, divisor);
        Expression[] predicates = {modByZeroPredicate};

        // Mock a path step that uses this problematic predicate.
        // The axis must be CHILD or SELF for the simple path interpreter to proceed to predicate evaluation.
        Step mockStep = mock(Step.class);
        when(mockStep.getAxis()).thenReturn(Compiler.AXIS_CHILD);
        when(mockStep.getPredicates()).thenReturn(predicates);
        Step[] steps = {mockStep};

        // A simple root node pointer to start the path evaluation.
        QName rootName = new QName("root");
        Object rootObject = new Object();
        NodePointer rootPointer = NodePointer.newNodePointer(rootName, rootObject, Locale.getDefault());

        // Act: Interpret the path. This should trigger the evaluation of the
        // predicate, resulting in an ArithmeticException.
        // The EvalContext can be null for this evaluation path.
        SimplePathInterpreter.interpretSimpleLocationPath(null, rootPointer, steps);

        // Assert: The test succeeds if an ArithmeticException is thrown, as declared
        // in the @Test annotation. It will fail otherwise.
    }
}