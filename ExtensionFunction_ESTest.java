package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExtensionFunction;
import org.apache.commons.jxpath.ri.compiler.CoreFunction;
import org.apache.commons.jxpath.ri.compiler.NameAttributeTest;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNegate;
import org.apache.commons.jxpath.ri.compiler.CoreOperationMod;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.axes.EvalContext;
import org.apache.commons.jxpath.ri.axes.NodeSetContext;
import org.apache.commons.jxpath.ri.axes.RootContext;
import org.apache.commons.jxpath.ri.model.BeanPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;

/**
 * Test cases for ExtensionFunction class.
 */
public class ExtensionFunctionTest {

    /**
     * Test getFunctionName method with a non-null function name.
     */
    @Test
    public void testGetFunctionNameNonNull() {
        // Arrange
        QName functionName = new QName("testFunction");
        ExtensionFunction extensionFunction = new ExtensionFunction(functionName, null);

        // Act
        QName retrievedFunctionName = extensionFunction.getFunctionName();

        // Assert
        assertSame(functionName, retrievedFunctionName);
    }

    /**
     * Test computeContextDependent method.
     */
    @Test
    public void testComputeContextDependent() {
        // Arrange
        ExtensionFunction extensionFunction = new ExtensionFunction(null, new Expression[0]);

        // Act
        boolean contextDependent = extensionFunction.computeContextDependent();

        // Assert
        assertTrue(contextDependent);
    }

    /**
     * Test toString method with a null function name.
     */
    @Test
    public void testToStringNullFunctionName() {
        // Arrange
        ExtensionFunction extensionFunction = new ExtensionFunction(null, null);

        // Act
        String toStringResult = extensionFunction.toString();

        // Assert
        assertEquals("null()", toStringResult);
    }

    /**
     * Test toString method with a non-null function name and empty arguments.
     */
    @Test
    public void testToStringNonNullFunctionNameEmptyArguments() {
        // Arrange
        ExtensionFunction extensionFunction = new ExtensionFunction(null, new Expression[6]);

        // Act
        String toStringResult = extensionFunction.toString();

        // Assert
        assertEquals("null(null, null, null, null, null, null)", toStringResult);
    }

    /**
     * Test computeValue method with a null context.
     */
    @Test(expected = NullPointerException.class)
    public void testComputeValueNullContext() {
        // Arrange
        ExtensionFunction extensionFunction = new ExtensionFunction(null, null);

        // Act
        extensionFunction.computeValue(null);
    }

    /**
     * Test computeValue method with a null function name.
     */
    @Test(expected = RuntimeException.class)
    public void testComputeValueNullFunctionName() {
        // Arrange
        QName functionName = new QName(null, null);
        ExtensionFunction extensionFunction = new ExtensionFunction(functionName, null);
        RootContext context = new RootContext(null, NodePointer.newNodePointer(functionName, null, null));

        // Act
        extensionFunction.computeValue(context);
    }

    /**
     * Test compute method with a null context.
     */
    @Test(expected = NullPointerException.class)
    public void testComputeNullContext() {
        // Arrange
        Expression[] expressions = new Expression[6];
        CoreFunction coreFunction = new CoreFunction(-185, expressions);
        expressions[0] = coreFunction;
        CoreOperationNegate coreOperationNegate = new CoreOperationNegate(coreFunction);
        expressions[1] = coreOperationNegate;
        CoreOperationMod coreOperationMod = new CoreOperationMod(coreFunction, coreOperationNegate);
        expressions[2] = coreOperationMod;
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressions);

        // Act
        extensionFunction.compute(null);
    }

    /**
     * Test compute method with a function that causes ArithmeticException.
     */
    @Test(expected = ArithmeticException.class)
    public void testComputeArithmeticException() {
        // Arrange
        Expression[] expressions = new Expression[6];
        CoreFunction coreFunction = new CoreFunction(-185, expressions);
        expressions[0] = coreFunction;
        CoreOperationNegate coreOperationNegate = new CoreOperationNegate(coreFunction);
        expressions[1] = coreOperationNegate;
        CoreOperationMod coreOperationMod = new CoreOperationMod(coreFunction, coreOperationNegate);
        expressions[2] = coreOperationMod;
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressions);

        // Act
        extensionFunction.compute(null);
    }

    /**
     * Test getFunctionName method with a null function name.
     */
    @Test
    public void testGetFunctionNameNull() {
        // Arrange
        ExtensionFunction extensionFunction = new ExtensionFunction(null, new Expression[2]);

        // Act
        QName functionName = extensionFunction.getFunctionName();

        // Assert
        assertNull(functionName);
    }
}