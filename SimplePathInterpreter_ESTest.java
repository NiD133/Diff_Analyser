package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

/**
 * Understandable tests for SimplePathInterpreter that cover:
 * - Simple child navigation on a Java bean
 * - Index-based predicate on a collection property
 * - Index-based predicate applied to the root (expression predicates)
 * - Handling of empty inputs
 * - Basic invalid-index handling in createNullPointer
 */
public class SimplePathInterpreter_UnderstandableTest {

    // A very small bean used in tests
    public static class Person {
        private String name;
        private List<String> children;

        public Person(String name, List<String> children) {
            this.name = name;
            this.children = children;
        }

        public String getName() {
            return name;
        }

        public List<String> getChildren() {
            return children;
        }
    }

    // Helper: create a root NodePointer for a given bean
    private static NodePointer beanRoot(Object bean) {
        return NodePointer.newNodePointer(new QName(null, "root"), bean, Locale.getDefault());
    }

    // Helper: create a simple child Step with a NodeNameTest and optional predicates
    private static Step childStep(String localName, Expression... predicates) {
        Step step = mock(Step.class);
        when(step.getAxis()).thenReturn(Compiler.AXIS_CHILD);
        when(step.getNodeTest()).thenReturn(new NodeNameTest(new QName(null, localName)));
        when(step.getPredicates()).thenReturn(predicates == null ? null : predicates);
        return step;
    }

    @Test
    public void childProperty_step_returnsPropertyValue() {
        Person person = new Person("Alice", Arrays.asList("Tom", "Bob"));
        NodePointer root = beanRoot(person);

        Step[] steps = new Step[] { childStep("name") };

        NodePointer result = SimplePathInterpreter.interpretSimpleLocationPath(null, root, steps);

        assertNotNull("Expected a pointer to the 'name' property", result);
        assertEquals("Alice", result.getValue());
    }

    @Test
    public void collectionProperty_withIndexPredicate_returnsIndexedElement() {
        Person person = new Person("Alice", Arrays.asList("Tom", "Bob", "Zoe"));
        NodePointer root = beanRoot(person);

        // XPath-like: children[2] -> second element ("Bob")
        Expression[] predicates = new Expression[] { new Constant(2) };
        Step[] steps = new Step[] { childStep("children", predicates) };

        NodePointer result = SimplePathInterpreter.interpretSimpleLocationPath(null, root, steps);

        assertNotNull("Expected a pointer to the second element in 'children'", result);
        assertEquals("Bob", result.getValue());
    }

    @Test
    public void rootCollection_withIndexPredicateOnRoot_returnsIndexedElement() {
        // Root is itself a collection: ["x", "y"]
        List<String> items = Arrays.asList("x", "y");
        NodePointer root = NodePointer.newNodePointer(new QName(null, "root"), items, Locale.getDefault());

        // Apply predicate [2] to root, with no further steps
        Expression[] rootPredicates = new Expression[] { new Constant(2) };
        Step[] noSteps = new Step[0];

        NodePointer result = SimplePathInterpreter.interpretSimpleExpressionPath(null, root, rootPredicates, noSteps);

        assertNotNull("Expected a pointer to the second element of the root collection", result);
        assertEquals("y", result.getValue());
    }

    @Test
    public void emptyRootAndEmptyInputs_returnNull() {
        NodePointer result = SimplePathInterpreter.interpretSimpleExpressionPath(
            null,
            null,
            new Expression[0],
            new Step[0]
        );
        assertNull(result);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void createNullPointer_withInvalidStepIndex_throwsArrayIndexOutOfBounds() {
        // No steps; attempting to start at step 0 should throw
        Step[] noSteps = new Step[0];
        SimplePathInterpreter.createNullPointer((EvalContext) null, beanRoot(new Person("p", Arrays.asList())), noSteps, 0);
    }
}