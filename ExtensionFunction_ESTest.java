package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Locale;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.axes.NodeSetContext;
import org.apache.commons.jxpath.ri.axes.RootContext;
import org.apache.commons.jxpath.ri.compiler.CoreFunction;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationMod;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNegate;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExtensionFunction;
import org.apache.commons.jxpath.ri.compiler.NameAttributeTest;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPointer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ExtensionFunction_ESTest extends ExtensionFunction_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testFunctionNameRetrieval() throws Throwable {
        // Test that the function name is correctly retrieved
        QName expectedQName = new QName("\".C>Cr 066:/ZH{");
        ExtensionFunction extensionFunction = new ExtensionFunction(expectedQName, null);
        QName actualQName = extensionFunction.getFunctionName();
        assertSame(expectedQName, actualQName);
    }

    @Test(timeout = 4000)
    public void testContextDependency() throws Throwable {
        // Test that the function is context dependent
        Expression[] expressions = new Expression[0];
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressions);
        boolean isContextDependent = extensionFunction.computeContextDependent();
        assertTrue(isContextDependent);
    }

    @Test(timeout = 4000)
    public void testToStringWithNullFunctionName() throws Throwable {
        // Test toString method when function name is null, expecting NullPointerException
        Expression[] expressions = new Expression[2];
        CoreFunction coreFunction = new CoreFunction(40, expressions);
        NameAttributeTest nameAttributeTest = new NameAttributeTest(null, coreFunction);
        expressions[1] = nameAttributeTest;
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressions);

        try {
            extensionFunction.toString();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.evosuite.runtime.System", e);
        }
    }

    @Test(timeout = 4000)
    public void testComputeValueWithUndefinedFunction() throws Throwable {
        // Test computeValue method with undefined function, expecting RuntimeException
        QName qName = new QName(null, null);
        ExtensionFunction extensionFunction = new ExtensionFunction(qName, null);
        JXPathContextReferenceImpl contextReference = (JXPathContextReferenceImpl) JXPathContext.newContext(null);
        BeanPointer beanPointer = (BeanPointer) NodePointer.newNodePointer(qName, extensionFunction, null);
        RootContext rootContext = new RootContext(contextReference, beanPointer);

        try {
            extensionFunction.computeValue(rootContext);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.jxpath.ri.JXPathContextReferenceImpl", e);
        }
    }

    @Test(timeout = 4000)
    public void testComputeWithArithmeticException() throws Throwable {
        // Test compute method with division by zero, expecting ArithmeticException
        Expression[] expressions = new Expression[6];
        CoreFunction coreFunction = new CoreFunction(-185, expressions);
        expressions[0] = coreFunction;
        CoreOperationNegate coreOperationNegate = new CoreOperationNegate(coreFunction);
        expressions[1] = coreOperationNegate;
        CoreOperationMod coreOperationMod = new CoreOperationMod(expressions[0], expressions[1]);
        expressions[2] = coreOperationMod;
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressions);

        try {
            extensionFunction.compute(null);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.apache.commons.jxpath.ri.compiler.CoreOperationMod", e);
        }
    }

    @Test(timeout = 4000)
    public void testComputeValueWithNullContext() throws Throwable {
        // Test computeValue method with null context, expecting NullPointerException
        ExtensionFunction extensionFunction = new ExtensionFunction(null, null);

        try {
            extensionFunction.computeValue(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.jxpath.ri.compiler.ExtensionFunction", e);
        }
    }

    @Test(timeout = 4000)
    public void testToStringWithNullExpressions() throws Throwable {
        // Test toString method with null expressions
        Expression[] expressions = new Expression[6];
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressions);
        String result = extensionFunction.toString();
        assertEquals("null(null, null, null, null, null, null)", result);
    }

    @Test(timeout = 4000)
    public void testToStringWithNoExpressions() throws Throwable {
        // Test toString method with no expressions
        ExtensionFunction extensionFunction = new ExtensionFunction(null, null);
        String result = extensionFunction.toString();
        assertEquals("null()", result);
    }

    @Test(timeout = 4000)
    public void testComputeValueWithNullPointerException() throws Throwable {
        // Test computeValue method with null pointer exception
        Expression[] expressions = new Expression[2];
        CoreFunction coreFunction = new CoreFunction(-206, expressions);
        expressions[0] = coreFunction;
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(coreFunction, expressions[0]);
        expressions[1] = coreOperationLessThan;
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressions);
        BasicNodeSet basicNodeSet = new BasicNodeSet();
        NodeSetContext nodeSetContext = new NodeSetContext(null, basicNodeSet);

        try {
            extensionFunction.computeValue(nodeSetContext);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.jxpath.ri.EvalContext", e);
        }
    }

    @Test(timeout = 4000)
    public void testComputeWithUndefinedFunction() throws Throwable {
        // Test compute method with undefined function, expecting RuntimeException
        QName qName = new QName("4t>H", "4t>H");
        ExtensionFunction extensionFunction = new ExtensionFunction(qName, null);
        Object object = new Object();
        Locale locale = new Locale("4t>H", "N\bff(SMuy^PQ9{6,G-");
        NodePointer nodePointer = NodePointer.newNodePointer(qName, object, locale);
        JXPathContextReferenceImpl contextReference = new JXPathContextReferenceImpl(null, qName, nodePointer);
        RootContext rootContext = new RootContext(contextReference, nodePointer);

        try {
            extensionFunction.compute(rootContext);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.jxpath.ri.JXPathContextReferenceImpl", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetFunctionNameWithNull() throws Throwable {
        // Test getFunctionName method when function name is null
        Expression[] expressions = new Expression[2];
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressions);
        QName functionName = extensionFunction.getFunctionName();
        assertNull(functionName);
    }

    @Test(timeout = 4000)
    public void testComputeWithNullPointerException() throws Throwable {
        // Test compute method with null pointer exception
        Expression[] expressions = new Expression[6];
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressions);

        try {
            extensionFunction.compute(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.jxpath.ri.compiler.ExtensionFunction", e);
        }
    }
}