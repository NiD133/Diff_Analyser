package org.apache.commons.jxpath.ri.axes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;

import java.util.Locale;
import org.apache.commons.jxpath.BasicVariables;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.axes.PredicateContext;
import org.apache.commons.jxpath.ri.axes.RootContext;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.CoreFunction;
import org.apache.commons.jxpath.ri.compiler.CoreOperationAnd;
import org.apache.commons.jxpath.ri.compiler.CoreOperationMod;
import org.apache.commons.jxpath.ri.compiler.CoreOperationMultiply;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNegate;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationSubtract;
import org.apache.commons.jxpath.ri.compiler.CoreOperationUnion;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.NameAttributeTest;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.compiler.VariableReference;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.evosuite.runtime.ViolatedAssumptionAnswer;

/**
 * Test suite for SimplePathInterpreter functionality.
 * Tests path interpretation, expression evaluation, and error handling scenarios.
 */
public class SimplePathInterpreter_ESTest {

    // Test Data Constants
    private static final String TEST_QNAME = "testName";
    private static final String UNKNOWN_NAMESPACE = "<<unknown namespace>>";
    private static final String EMPTY_STRING = "";
    private static final Locale TEST_LOCALE = Locale.JAPANESE;

    // ========== Expression Path Interpretation Tests ==========

    @Test(timeout = 4000)
    public void testInterpretSimpleExpressionPath_WithNullSteps_ThrowsNullPointerException() throws Throwable {
        // Arrange
        Object contextObject = new Object();
        JXPathContextReferenceImpl jxPathContext = createJXPathContext(contextObject);
        QName qName = new QName(TEST_QNAME);
        BeanPointer beanPointer = createBeanPointer(qName, jxPathContext, Locale.KOREA);
        RootContext rootContext = new RootContext(jxPathContext, beanPointer);
        
        Step[] stepsWithNulls = new Step[4]; // Contains null elements
        Expression[] expressions = createConstantExpressions(2, TEST_QNAME);

        // Act & Assert
        try {
            SimplePathInterpreter.interpretSimpleExpressionPath(rootContext, beanPointer, expressions, stepsWithNulls);
            fail("Expected NullPointerException when steps array contains null elements");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testInterpretSimpleExpressionPath_WithNullContext_ThrowsNullPointerException() throws Throwable {
        // Arrange
        NullPointer nullPointer = new NullPointer(TEST_LOCALE, EMPTY_STRING);
        Step[] stepsWithNulls = new Step[3];
        Expression[] expressions = new Expression[8];
        expressions[0] = new Constant(UNKNOWN_NAMESPACE);
        expressions[1] = new CoreOperationSubtract(expressions[0], expressions[0]);

        // Act & Assert
        try {
            SimplePathInterpreter.interpretSimpleExpressionPath(null, nullPointer, expressions, stepsWithNulls);
            fail("Expected NullPointerException when context is null");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testInterpretSimpleExpressionPath_WithValidSelfAxisSteps_ReturnsNodePointer() throws Throwable {
        // Arrange
        QName qName = new QName("testNode");
        NodePointer nodePointer = NodePointer.newNodePointer(qName, Locale.KOREA, Locale.KOREA);
        Expression[] expressions = {new Constant(UNKNOWN_NAMESPACE)};
        Step[] steps = createMockStepsWithSelfAxis(2, expressions);

        // Act
        NodePointer result = SimplePathInterpreter.interpretSimpleExpressionPath(null, nodePointer, expressions, steps);

        // Assert
        assertNotNull("Should return a valid NodePointer", result);
        assertEquals("Index should be set correctly", 0, result.getIndex());
    }

    @Test(timeout = 4000)
    public void testInterpretSimpleExpressionPath_WithEmptyExpressions_ReturnsNull() throws Throwable {
        // Arrange
        Step[] emptySteps = new Step[0];
        Expression[] emptyExpressions = new Expression[0];

        // Act
        NodePointer result = SimplePathInterpreter.interpretSimpleExpressionPath(null, null, emptyExpressions, emptySteps);

        // Assert
        assertNull("Should return null for empty expressions and steps", result);
    }

    // ========== Location Path Interpretation Tests ==========

    @Test(timeout = 4000)
    public void testInterpretSimpleLocationPath_WithEmptySteps_ReturnsOriginalPointer() throws Throwable {
        // Arrange
        NullPointer nullPointer = new NullPointer(TEST_LOCALE, EMPTY_STRING);
        nullPointer.setIndex(133);
        Step[] emptySteps = new Step[0];

        // Act
        NullPointer result = (NullPointer) SimplePathInterpreter.interpretSimpleLocationPath(null, nullPointer, emptySteps);

        // Assert
        assertNotNull("Should return a valid pointer", result);
        assertFalse("Dynamic property declaration should not be supported", result.isDynamicPropertyDeclarationSupported());
    }

    @Test(timeout = 4000)
    public void testInterpretSimpleLocationPath_WithSelfAxisStep_ReturnsSamePointer() throws Throwable {
        // Arrange
        QName qName = new QName("testNode");
        NodePointer nodePointer = NodePointer.newNodePointer(qName, Locale.JAPAN, Locale.JAPAN);
        Step[] steps = createMockStepsWithSelfAxis(1, null);

        // Act
        NodePointer result = SimplePathInterpreter.interpretSimpleLocationPath(null, nodePointer, steps);

        // Assert
        assertNotNull("Should return a valid pointer", result);
        assertTrue("Result should be a node", result.isNode());
    }

    @Test(timeout = 4000)
    public void testInterpretSimpleLocationPath_WithUndefinedVariable_ThrowsIllegalArgumentException() throws Throwable {
        // Arrange
        QName undefinedVariable = new QName("undefinedVariable");
        BasicVariables variables = new BasicVariables();
        VariablePointer variablePointer = new VariablePointer(variables, undefinedVariable);
        Step[] steps = new Step[1];

        // Act & Assert
        try {
            SimplePathInterpreter.interpretSimpleLocationPath(null, variablePointer, steps);
            fail("Expected IllegalArgumentException for undefined variable");
        } catch (IllegalArgumentException e) {
            assertTrue("Error message should mention the undefined variable", 
                      e.getMessage().contains("No such variable"));
        }
    }

    // ========== Error Handling Tests ==========

    @Test(timeout = 4000)
    public void testInterpretSimpleLocationPath_WithInvalidNodeTest_ThrowsClassCastException() throws Throwable {
        // Arrange
        QName qName = new QName("testNode");
        NodePointer nodePointer = NodePointer.newNodePointer(qName, Locale.JAPAN, Locale.JAPAN);
        Step[] steps = new Step[1];
        
        // Create a step with ProcessingInstructionTest (invalid for this context)
        ProcessingInstructionTest invalidTest = new ProcessingInstructionTest(UNKNOWN_NAMESPACE);
        Step mockStep = createMockStep(5, invalidTest, null);
        steps[0] = mockStep;

        // Act & Assert
        try {
            SimplePathInterpreter.interpretSimpleLocationPath(null, nodePointer, steps);
            fail("Expected ClassCastException for invalid node test type");
        } catch (ClassCastException e) {
            assertTrue("Error should mention ProcessingInstructionTest", 
                      e.getMessage().contains("ProcessingInstructionTest"));
        }
    }

    @Test(timeout = 4000)
    public void testInterpretSimpleLocationPath_WithDivisionByZero_ThrowsArithmeticException() throws Throwable {
        // Arrange
        QName qName = new QName("testNode");
        NodePointer nodePointer = NodePointer.newNodePointer(qName, Locale.JAPAN, Locale.JAPAN);
        
        // Create expression that will cause division by zero
        Expression[] expressions = new Expression[1];
        CoreFunction coreFunction = new CoreFunction(Integer.MIN_VALUE, expressions);
        CoreOperationMod modOperation = new CoreOperationMod(coreFunction, coreFunction); // This will cause division by zero
        expressions[0] = modOperation;
        
        Step[] steps = createMockStepsWithPredicates(1, expressions);

        // Act & Assert
        try {
            SimplePathInterpreter.interpretSimpleLocationPath(null, nodePointer, steps);
            fail("Expected ArithmeticException for division by zero");
        } catch (ArithmeticException e) {
            assertEquals("Should be division by zero error", "/ by zero", e.getMessage());
        }
    }

    // ========== Null Pointer Creation Tests ==========

    @Test(timeout = 4000)
    public void testCreateNullPointer_WithValidParameters_CreatesNullPointer() throws Throwable {
        // Arrange
        NullPointer nullPointer = new NullPointer(TEST_LOCALE, EMPTY_STRING);
        Step[] steps = new Step[3];
        Expression[] expressions = {new CoreOperationAnd(new Expression[1])};
        steps[0] = createMockStep(0, null, expressions);

        // Act
        SimplePathInterpreter.createNullPointer(null, nullPointer, steps, 0);

        // Assert - Should complete without throwing exception
    }

    @Test(timeout = 4000)
    public void testCreateNullPointer_WithNegativeIndex_ThrowsArrayIndexOutOfBoundsException() throws Throwable {
        // Arrange
        Step[] emptySteps = new Step[0];

        // Act & Assert
        try {
            SimplePathInterpreter.createNullPointer(null, null, emptySteps, -183);
            fail("Expected ArrayIndexOutOfBoundsException for negative index");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("Should show the invalid index", "-183", e.getMessage());
        }
    }

    // ========== Expression Evaluation Tests ==========

    @Test(timeout = 4000)
    public void testExpressionEvaluation_WithNegateOperation_ReturnsCorrectIndex() throws Throwable {
        // Arrange
        QName qName = new QName("testNode");
        NodePointer nodePointer = NodePointer.newNodePointer(qName, Locale.JAPAN, Locale.JAPAN);
        
        Expression[] expressions = new Expression[1];
        Constant constant = new Constant(Integer.MIN_VALUE);
        CoreOperationNegate negateOp = new CoreOperationNegate(constant);
        expressions[0] = negateOp;
        
        Step[] steps = createMockStepsWithPredicates(1, expressions);

        // Act
        NodePointer result = SimplePathInterpreter.interpretSimpleExpressionPath(null, nodePointer, expressions, steps);

        // Assert
        assertNotNull("Should return a valid result", result);
        assertEquals("Should have correct index after negation", 2147483646, result.getIndex());
        assertEquals("Original pointer index should remain unchanged", Integer.MIN_VALUE, nodePointer.getIndex());
    }

    @Test(timeout = 4000)
    public void testExpressionEvaluation_WithComplexExpression_ThrowsRuntimeException() throws Throwable {
        // Arrange
        NodePointer nodePointer = NodePointer.newNodePointer(new QName("testNode"), TEST_LOCALE, TEST_LOCALE);
        
        Expression[] expressions = new Expression[8];
        Constant constant = new Constant("testValue");
        expressions[0] = constant;
        CoreFunction coreFunction = new CoreFunction(14, expressions); // Invalid function call
        NameAttributeTest nameTest = new NameAttributeTest(constant, coreFunction);
        expressions[1] = nameTest;

        // Act & Assert
        try {
            SimplePathInterpreter.interpretSimpleExpressionPath(null, nodePointer, expressions, null);
            fail("Expected RuntimeException for invalid function call");
        } catch (RuntimeException e) {
            assertTrue("Error should mention incorrect number of arguments", 
                      e.getMessage().contains("Incorrect number of arguments"));
        }
    }

    // ========== Helper Methods ==========

    private JXPathContextReferenceImpl createJXPathContext(Object contextObject) {
        return (JXPathContextReferenceImpl) JXPathContext.newContext(contextObject);
    }

    private BeanPointer createBeanPointer(QName qName, JXPathContextReferenceImpl context, Locale locale) {
        return (BeanPointer) NodePointer.newNodePointer(qName, context, locale);
    }

    private Expression[] createConstantExpressions(int count, String value) {
        Expression[] expressions = new Expression[count];
        Constant constant = new Constant(value);
        for (int i = 0; i < count; i++) {
            expressions[i] = constant;
        }
        return expressions;
    }

    private Step[] createMockStepsWithSelfAxis(int count, Expression[] predicates) {
        Step[] steps = new Step[count];
        for (int i = 0; i < count; i++) {
            steps[i] = createMockStep(0, null, predicates); // 0 = self axis
        }
        return steps;
    }

    private Step[] createMockStepsWithPredicates(int count, Expression[] predicates) {
        Step[] steps = new Step[count];
        for (int i = 0; i < count; i++) {
            steps[i] = createMockStep(Integer.MIN_VALUE, null, predicates);
        }
        return steps;
    }

    private Step createMockStep(int axis, Object nodeTest, Expression[] predicates) {
        Step mockStep = mock(Step.class, new ViolatedAssumptionAnswer());
        doReturn(axis).when(mockStep).getAxis();
        if (nodeTest != null) {
            doReturn(nodeTest).when(mockStep).getNodeTest();
        }
        doReturn(predicates).when(mockStep).getPredicates();
        return mockStep;
    }
}