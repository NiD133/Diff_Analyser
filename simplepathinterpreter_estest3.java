package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;

import java.util.Locale;

import static org.apache.commons.jxpath.ri.Compiler.AXIS_SELF;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// The EvoRunner is kept as it was part of the original test setup,
// though the refactored test no longer relies on its specific behaviors.
@RunWith(EvoRunner.class)
public class SimplePathInterpreter_ESTestTest3 {

    /**
     * Tests that interpreting a path with a 'self' axis and a non-numeric string
     * predicate on a non-existent node correctly creates a null pointer with an
     * index of 0.
     * <p>
     * This behavior occurs because JXPath attempts to convert the string predicate
     * to a numeric index. A non-numeric string evaluates to NaN (Not a Number),
     * which is then cast to an integer index of 0.
     */
    @Test(timeout = 4000)
    public void interpretSimpleLocationPathWithSelfStepAndStringPredicateCreatesNullPointerWithIndexZero() {
        // Arrange: Set up a path for a non-existent node.
        // The path is self::someName["a-string-predicate"]
        
        // 1. Define the starting point: a pointer to a non-existent node.
        NodePointer rootNullPointer = new NullPointer(Locale.ENGLISH, "testID");

        // 2. Create the string-based predicate: ["a-string-predicate"]
        Expression[] stringPredicate = {new Constant("a-string-predicate")};

        // 3. Mock a 'self::someName' step containing the predicate.
        Step mockSelfStep = mock(Step.class);
        when(mockSelfStep.getAxis()).thenReturn(AXIS_SELF);
        when(mockSelfStep.getNodeTest()).thenReturn(new NodeNameTest(new QName("someName")));
        when(mockSelfStep.getPredicates()).thenReturn(stringPredicate);
        
        Step[] pathSteps = {mockSelfStep};

        // Act: Interpret the path starting from the null pointer.
        // The context can be null because the simple interpreter does not use it for this path.
        NodePointer resultPointer = SimplePathInterpreter.interpretSimpleLocationPath(
                (EvalContext) null, rootNullPointer, pathSteps);

        // Assert: The resulting pointer should represent the unresolved path,
        // with the string predicate converted to an index of 0.
        assertEquals("Resulting pointer index should be 0", 0, resultPointer.getIndex());
    }
}