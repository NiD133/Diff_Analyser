package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNegate;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullElementPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.apache.commons.jxpath.ri.Compiler.AXIS_ATTRIBUTE;
import static org.apache.commons.jxpath.ri.Compiler.AXIS_CHILD;
import static org.apache.commons.jxpath.ri.Compiler.AXIS_SELF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SimplePathInterpreter}.
 *
 * This class focuses on verifying the behavior of the simplified XPath evaluation logic,
 * which handles common, simple path patterns for performance. The tests cover path
 * interpretation for both location paths and expression paths, as well as the creation
 * of "null" pointers for non-existent paths.
 */
@DisplayName("SimplePathInterpreter")
class SimplePathInterpreterTest {

    private EvalContext evalContext;
    private NodePointer rootPointer;

    @BeforeEach
    void setUp() {
        // A null context is sufficient for many SimplePathInterpreter operations
        // that don't require actual data lookup.
        JXPathContext context = JXPathContext.newContext(null);
        evalContext = context.getCompiler().compile("/", new Expression[0]).getRootContext(context);
        rootPointer = NodePointer.newNodePointer(new QName("root"), new Object(), Locale.ENGLISH);
    }

    /**
     * Creates a mock Step with specified properties.
     *
     * @param axis       The step's axis (e.g., AXIS_CHILD).
     * @param nodeTest   The test for the node (e.g., a NodeNameTest).
     * @param predicates An array of predicate expressions.
     * @return A configured mock Step object.
     */
    private Step createMockStep(int axis, NodeTest nodeTest, Expression[] predicates) {
        Step mockStep = mock(Step.class);
        doReturn(axis).when(mockStep).getAxis();
        doReturn(nodeTest).when(mockStep).getNodeTest();
        doReturn(predicates).when(mockStep).getPredicates();
        return mockStep;
    }

    @Nested
    @DisplayName("interpretSimpleLocationPath")
    class InterpretSimpleLocationPath {

        @Test
        void withEmptySteps_shouldReturnRootPointer() {
            // Arrange
            Step[] emptySteps = new Step[0];

            // Act
            NodePointer result = SimplePathInterpreter.interpretSimpleLocationPath(evalContext, rootPointer, emptySteps);

            // Assert
            assertSame(rootPointer, result, "With no steps, the original root pointer should be returned.");
        }

        @Test
        void withSelfAxisStep_shouldReturnRootPointer() {
            // Arrange
            Step selfStep = createMockStep(AXIS_SELF, null, null);
            Step[] steps = {selfStep};

            // Act
            NodePointer result = SimplePathInterpreter.interpretSimpleLocationPath(evalContext, rootPointer, steps);

            // Assert
            assertSame(rootPointer, result, "A 'self::' step should return the context node itself.");
        }

        @Test
        void forAttributeOnNullPointer_shouldReturnNull() {
            // Arrange
            NodePointer nullRoot = new NullPointer(Locale.ENGLISH, "id");
            NodeNameTest attributeTest = new NodeNameTest(new QName("someAttribute"));
            Step attributeStep = createMockStep(AXIS_ATTRIBUTE, attributeTest, null);
            Step[] steps = {attributeStep};

            // Act
            // Attempting to find an attribute on a NullPointer should not find anything.
            NodePointer result = SimplePathInterpreter.interpretSimpleLocationPath(evalContext, nullRoot, steps);

            // Assert
            assertNull(result, "Should return null when trying to access an attribute on a NullPointer.");
        }

        @Test
        void withNullStepInArray_shouldThrowNullPointerException() {
            // Arrange
            Step[] steps = new Step[1]; // The single element is null

            // Act & Assert
            assertThrows(NullPointerException.class, () ->
                    SimplePathInterpreter.interpretSimpleLocationPath(evalContext, rootPointer, steps),
                "Interpreter should throw NPE when encountering a null step in the path.");
        }

        @Test
        void withUnsupportedNodeTestType_shouldThrowClassCastException() {
            // Arrange
            // SimplePathInterpreter only supports NodeNameTest for child/attribute axes.
            ProcessingInstructionTest unsupportedTest = new ProcessingInstructionTest("target");
            Step step = createMockStep(AXIS_CHILD, unsupportedTest, null);
            Step[] steps = {step};

            // Act & Assert
            assertThrows(ClassCastException.class, () ->
                    SimplePathInterpreter.interpretSimpleLocationPath(evalContext, rootPointer, steps),
                "Should throw ClassCastException for unsupported NodeTest types like ProcessingInstructionTest.");
        }
    }

    @Nested
    @DisplayName("interpretSimpleExpressionPath")
    class InterpretSimpleExpressionPath {

        @Test
        void withNullStepInArray_shouldThrowNullPointerException() {
            // Arrange
            Step[] stepsWithNull = new Step[1]; // The single element is null
            Expression[] noPredicates = new Expression[0];

            // Act & Assert
            assertThrows(NullPointerException.class, () ->
                    SimplePathInterpreter.interpretSimpleExpressionPath(evalContext, rootPointer, noPredicates, stepsWithNull),
                "Interpreter should throw NPE when encountering a null step.");
        }

        @Test
        void withNumericPredicateOnNonCollection_shouldCreateNullElementPointer() {
            // Arrange
            // A predicate like [number], e.g., /books[2].
            // We use a complex expression that evaluates to a large number: - (Integer.MIN_VALUE)
            Expression[] predicates = {
                new CoreOperationNegate(new Constant(Integer.MIN_VALUE))
            };
            // The expression evaluates to 2147483648.0, which as an int is Integer.MAX_VALUE (2147483647).
            int expectedIndexInPointer = Integer.MAX_VALUE - 1; // XPath indices are 1-based.

            // No further steps after the predicate.
            Step[] noSteps = new Step[0];

            // Act
            // Since the root is not a collection, the predicate will not match an existing element.
            // The interpreter should create a NullElementPointer to represent the non-existent element.
            NodePointer result = SimplePathInterpreter.interpretSimpleExpressionPath(
                evalContext, rootPointer, predicates, noSteps);

            // Assert
            assertNotNull(result, "Should return a NullElementPointer, not null.");
            assertInstanceOf(NullElementPointer.class, result, "Pointer should be of type NullElementPointer.");
            assertEquals(expectedIndexInPointer, result.getIndex(), "Pointer index should be the predicate value minus one.");
        }
    }

    @Nested
    @DisplayName("createNullPointer")
    class CreateNullPointer {

        @Test
        void withChildStep_shouldCreateNullPropertyPointer() {
            // Arrange
            QName childName = new QName("nonExistentChild");
            NodeNameTest nameTest = new NodeNameTest(childName);
            Step childStep = createMockStep(AXIS_CHILD, nameTest, null);
            Step[] steps = {childStep};

            // Act
            // Create a pointer for a path that does not exist.
            NullPropertyPointer nullPointer = (NullPropertyPointer) SimplePathInterpreter.createNullPointer(
                evalContext, rootPointer, steps, 0);

            // Assert
            assertNotNull(nullPointer);
            assertEquals("*", nullPointer.getPropertyName(), "A NullPropertyPointer for a specific name should be created.");
            // The actual name is stored differently in the pointer's internal structure, but the property name is wildcarded.
        }

        @Test
        void withInvalidStepIndex_shouldThrowArrayIndexOutOfBoundsException() {
            // Arrange
            Step[] noSteps = new Step[0];
            int invalidIndex = -1;

            // Act & Assert
            assertThrows(ArrayIndexOutOfBoundsException.class, () ->
                    SimplePathInterpreter.createNullPointer(evalContext, rootPointer, noSteps, invalidIndex),
                "Accessing a step at a negative index should throw an exception.");
        }
    }
}