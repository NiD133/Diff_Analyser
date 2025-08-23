package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.BasicVariables;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.CoreOperationMultiply;
import org.apache.commons.jxpath.ri.compiler.CoreOperationUnion;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.Test;

import java.util.Locale;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * This test was improved for understandability.
 * The original class was an EvoSuite-generated class named SimplePathInterpreter_ESTestTest6.
 */
public class SimplePathInterpreterImprovedTest {

    /**
     * Tests that interpretSimpleExpressionPath throws a NullPointerException
     * when a step in the path returns null for its predicates.
     *
     * The test sets up a path with multiple steps using the same mock Step instance.
     * This mock is configured to return a valid (empty) predicate array on the
     * first call, but null on the second call, which triggers the expected exception.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void interpretSimpleExpressionPathShouldThrowNPEWhenStepPredicatesAreNull() {
        // ARRANGE

        // 1. Set up a mock EvalContext. The specific implementation details are not
        //    critical for this test; it just needs to be a non-null context.
        Expression[] noExpressions = new Expression[0];
        CoreOperationUnion dummyUnion = new CoreOperationUnion(noExpressions);
        CoreOperationMultiply dummyMultiply = new CoreOperationMultiply(dummyUnion, dummyUnion);
        EvalContext mockContext = new PredicateContext(null, dummyMultiply);

        // 2. Set up a starting NodePointer. The complex object graph is constructed
        //    to ensure the method under test is exercised correctly.
        QName dummyQName = new QName(null, "");
        VariablePointer variablePointer = new VariablePointer(new BasicVariables(), dummyQName);
        NodePointer childOfVariable = NodePointer.newChildNodePointer(variablePointer, dummyQName, dummyQName);
        NullPropertyPointer nullProperty = new NullPropertyPointer(childOfVariable);
        NodePointer startNode = NodePointer.newNodePointer(dummyQName, nullProperty, Locale.KOREA);

        // 3. Create a mock Step that will cause the failure.
        Step mockStep = mock(Step.class, new ViolatedAssumptionAnswer());
        NodeNameTest nodeNameTest = new NodeNameTest(dummyQName);
        Expression[] emptyPredicates = new Expression[0];

        // Configure the mock's behavior to change on subsequent calls:
        // - getAxis() returns arbitrary valid axis codes.
        // - getNodeTest() returns a valid test.
        // - getPredicates() returns an empty array first, then NULL on the second call.
        //   This is the specific condition that is expected to cause the NullPointerException.
        doReturn(5, 0).when(mockStep).getAxis();
        doReturn(nodeNameTest).when(mockStep).getNodeTest();
        doReturn(emptyPredicates, null).when(mockStep).getPredicates();

        // 4. Define the path steps. The path has multiple steps, all using the same
        //    mock instance. The failure will occur when processing the second step.
        Step[] steps = {mockStep, mockStep, mockStep, mockStep};

        // ACT & ASSERT
        // The method is expected to throw a NullPointerException when it encounters
        // the step with null predicates. The @Test(expected=...) annotation handles the assertion.
        SimplePathInterpreter.interpretSimpleExpressionPath(mockContext, startNode, noExpressions, steps);
    }
}