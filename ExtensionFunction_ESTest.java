package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Locale;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.NodeSetContext;
import org.apache.commons.jxpath.ri.axes.RootContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPointer;

/**
 * Test suite for ExtensionFunction class.
 * Tests function name handling, context dependency, string representation,
 * and various computation scenarios including error cases.
 */
public class ExtensionFunctionTest {

    private static final String SAMPLE_FUNCTION_NAME = "org.apache.commons.jxpath.ri.Parser";
    private static final String SAMPLE_STRING_VALUE = "$KX%u}cMaD3:";

    @Test
    public void shouldReturnNullWhenFunctionNameIsNull() {
        // Given
        Expression[] args = new Expression[3];
        ExtensionFunction function = new ExtensionFunction(null, args);
        
        // When
        QName result = function.getFunctionName();
        
        // Then
        assertNull("Function name should be null when initialized with null", result);
    }

    @Test
    public void shouldReturnProvidedFunctionName() {
        // Given
        QName expectedName = new QName("org.apache.commons.jxpath.ri.compiler.ExtensionFunction");
        ExtensionFunction function = new ExtensionFunction(expectedName, null);
        
        // When
        QName actualName = function.getFunctionName();
        
        // Then
        assertSame("Function should return the same QName instance that was provided", 
                   expectedName, actualName);
    }

    @Test
    public void shouldAlwaysBeContextDependent() {
        // Given
        Expression[] args = new Expression[1];
        ExtensionFunction function = new ExtensionFunction(null, args);
        
        // When
        boolean isContextDependent = function.computeContextDependent();
        
        // Then
        assertTrue("Extension functions should always be context dependent", isContextDependent);
    }

    @Test
    public void shouldGenerateStringRepresentationWithNullFunctionName() {
        // Given
        Expression[] args = new Expression[2];
        ExtensionFunction function = new ExtensionFunction(null, args);
        
        // When
        String result = function.toString();
        
        // Then
        assertEquals("Should format null function name correctly", "null(null, null)", result);
    }

    @Test
    public void shouldGenerateStringRepresentationWithValidFunctionName() {
        // Given
        QName functionName = new QName(SAMPLE_FUNCTION_NAME);
        ExtensionFunction function = new ExtensionFunction(functionName, null);
        
        // When
        String result = function.toString();
        
        // Then
        assertEquals("Should format function name with empty args correctly", 
                     SAMPLE_FUNCTION_NAME + "()", result);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenToStringCalledWithComplexNullArgs() {
        // Given
        Expression[] args = new Expression[1];
        CoreOperationOr operation = new CoreOperationOr(new Expression[1]);
        args[0] = operation;
        ExtensionFunction function = new ExtensionFunction(null, args);
        
        // When
        function.toString(); // Should throw NPE
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionWhenFunctionNotFound() {
        // Given
        QName functionName = new QName(SAMPLE_FUNCTION_NAME);
        ExtensionFunction function = new ExtensionFunction(functionName, null);
        EvalContext context = createBasicEvalContext();
        
        // When
        function.computeValue(context); // Should throw RuntimeException about function not found
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionForUndefinedFunction() {
        // Given
        QName functionName = new QName("", "");
        Expression[] args = createComplexExpressionArray();
        ExtensionFunction function = new ExtensionFunction(functionName, args);
        EvalContext context = createComplexEvalContext(functionName);
        
        // When
        function.compute(context); // Should throw RuntimeException about undefined function
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionWhenNoFunctionFoundInMockedContext() {
        // Given
        ExtensionFunction function = new ExtensionFunction(null, new Expression[0]);
        EvalContext context = createMockedEvalContext();
        
        // When
        function.computeValue(context); // Should throw RuntimeException about no such function
    }

    @Test(expected = ArithmeticException.class)
    public void shouldPropagateArithmeticExceptionFromArguments() {
        // Given - Create function with argument that will cause division by zero
        Expression[] args = createArithmeticExceptionArgs();
        ExtensionFunction function = new ExtensionFunction(null, args);
        
        // When
        function.computeValue(null); // Should throw ArithmeticException from mod operation
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenComputingWithNullContext() {
        // Given
        Expression[] args = new Expression[1];
        ExtensionFunction function = new ExtensionFunction(null, args);
        
        // When
        function.compute(null); // Should throw NPE
    }

    @Test(expected = StackOverflowError.class)
    public void shouldThrowStackOverflowForCircularExpressionReference() {
        // Given - Create circular reference in expressions
        QName functionName = new QName("N(", "N(");
        Expression[] args = createCircularReferenceArgs();
        ExtensionFunction function = new ExtensionFunction(functionName, args);
        
        // When
        function.compute(null); // Should cause StackOverflowError
    }

    // Helper methods to create test data and contexts

    private EvalContext createBasicEvalContext() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        NodeSetContext nodeSetContext = new NodeSetContext(null, nodeSet);
        JXPathContextReferenceImpl jxPathContext = 
            (JXPathContextReferenceImpl) JXPathContext.newContext(nodeSetContext);
        
        QName qName = new QName(SAMPLE_FUNCTION_NAME);
        BeanPointer beanPointer = (BeanPointer) NodePointer.newNodePointer(
            qName, nodeSetContext, Locale.PRC);
        
        return new RootContext(jxPathContext, beanPointer);
    }

    private EvalContext createComplexEvalContext(QName functionName) {
        Double notANumber = Expression.NOT_A_NUMBER;
        JXPathContextReferenceImpl jxPathContext = 
            (JXPathContextReferenceImpl) JXPathContext.newContext(null, new Object());
        BeanPointer beanPointer = (BeanPointer) NodePointer.newChildNodePointer(
            null, functionName, notANumber);
        RootContext rootContext = new RootContext(jxPathContext, beanPointer);
        
        return rootContext.getConstantContext(new Object());
    }

    private EvalContext createMockedEvalContext() {
        JXPathContextReferenceImpl mockContext = mock(JXPathContextReferenceImpl.class);
        when(mockContext.getFunction(any(QName.class), any(Object[].class))).thenReturn(null);
        when(mockContext.getNamespaceResolver()).thenReturn(null);
        
        VariablePointer variablePointer = new VariablePointer(null);
        return new RootContext(mockContext, variablePointer);
    }

    private Expression[] createArithmeticExceptionArgs() {
        Expression[] args = new Expression[8];
        Constant stringConstant = new Constant(SAMPLE_STRING_VALUE);
        args[0] = stringConstant;
        
        // Create mod operation that will cause division by zero
        CoreOperationMod modOperation = new CoreOperationMod(args[0], args[0]);
        args[1] = modOperation;
        
        return args;
    }

    private Expression[] createComplexExpressionArray() {
        Double notANumber = Expression.NOT_A_NUMBER;
        Constant constant = new Constant(notANumber);
        CoreOperationNegate negate = new CoreOperationNegate(constant);
        CoreOperationMultiply multiply = new CoreOperationMultiply(negate, negate);
        CoreOperationLessThanOrEqual lessThanOrEqual = 
            new CoreOperationLessThanOrEqual(multiply, negate);
        
        return new Expression[] { lessThanOrEqual };
    }

    private Expression[] createCircularReferenceArgs() {
        Double notANumber = Expression.NOT_A_NUMBER;
        Constant constant = new Constant(notANumber);
        CoreOperationNegate negate = new CoreOperationNegate(constant);
        
        Expression[] circularArgs = new Expression[1];
        circularArgs[0] = negate;
        negate.args = circularArgs; // Create circular reference
        
        return circularArgs;
    }
}