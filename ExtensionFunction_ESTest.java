package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
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
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationMod;
import org.apache.commons.jxpath.ri.compiler.CoreOperationMultiply;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNegate;
import org.apache.commons.jxpath.ri.compiler.CoreOperationOr;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExtensionFunction;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPointer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ExtensionFunction_ESTest extends ExtensionFunction_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testFunctionNameIsNull() throws Throwable {
        Expression[] expressions = new Expression[3];
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressions);
        QName functionName = extensionFunction.getFunctionName();
        assertNull("Function name should be null", functionName);
    }

    @Test(timeout = 4000)
    public void testComputeContextDependent() throws Throwable {
        Expression[] expressions = new Expression[1];
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressions);
        boolean isContextDependent = extensionFunction.computeContextDependent();
        assertTrue("Extension function should be context dependent", isContextDependent);
    }

    @Test(timeout = 4000)
    public void testToStringThrowsNullPointerException() throws Throwable {
        Expression[] expressions = new Expression[1];
        CoreOperationOr coreOperationOr = new CoreOperationOr(expressions);
        expressions[0] = coreOperationOr;
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressions);
        
        try {
            extensionFunction.toString();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.evosuite.runtime.System", e);
        }
    }

    @Test(timeout = 4000)
    public void testComputeValueThrowsRuntimeException() throws Throwable {
        QName functionName = new QName("org.apache.commons.jxpath.ri.Parser");
        ExtensionFunction extensionFunction = new ExtensionFunction(functionName, null);
        BasicNodeSet nodeSet = new BasicNodeSet();
        NodeSetContext nodeSetContext = new NodeSetContext(null, nodeSet);
        JXPathContextReferenceImpl contextReference = (JXPathContextReferenceImpl) JXPathContext.newContext(nodeSetContext);
        Locale locale = Locale.PRC;
        BeanPointer beanPointer = (BeanPointer) NodePointer.newNodePointer(functionName, nodeSetContext, locale);
        RootContext rootContext = new RootContext(contextReference, beanPointer);
        
        try {
            extensionFunction.computeValue(rootContext);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.jxpath.PackageFunctions", e);
        }
    }

    @Test(timeout = 4000)
    public void testComputeValueThrowsArithmeticException() throws Throwable {
        Expression[] expressions = new Expression[8];
        Constant constant = new Constant("$KX%u}cMaD3:");
        expressions[0] = constant;
        CoreOperationMod coreOperationMod = new CoreOperationMod(expressions[0], expressions[0]);
        expressions[1] = coreOperationMod;
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressions);
        
        try {
            extensionFunction.computeValue(null);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.apache.commons.jxpath.ri.compiler.CoreOperationMod", e);
        }
    }

    @Test(timeout = 4000)
    public void testComputeThrowsRuntimeException() throws Throwable {
        QName functionName = new QName("", "");
        Expression[] expressions = new Expression[1];
        Double notANumber = Expression.NOT_A_NUMBER;
        Constant constant = new Constant(notANumber);
        CoreOperationNegate negate = new CoreOperationNegate(constant);
        CoreOperationMultiply multiply = new CoreOperationMultiply(negate, negate);
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(multiply, negate);
        expressions[0] = lessThanOrEqual;
        ExtensionFunction extensionFunction = new ExtensionFunction(functionName, expressions);
        JXPathContextReferenceImpl contextReference = (JXPathContextReferenceImpl) JXPathContext.newContext(null, lessThanOrEqual);
        BeanPointer beanPointer = (BeanPointer) NodePointer.newChildNodePointer(null, functionName, notANumber);
        RootContext rootContext = new RootContext(contextReference, beanPointer);
        Object object = new Object();
        InitialContext initialContext = (InitialContext) rootContext.getConstantContext(object);
        
        try {
            extensionFunction.compute(initialContext);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.jxpath.ri.JXPathContextReferenceImpl", e);
        }
    }

    @Test(timeout = 4000)
    public void testComputeThrowsStackOverflowError() throws Throwable {
        QName functionName = new QName("N(", "N(");
        Double notANumber = Expression.NOT_A_NUMBER;
        Constant constant = new Constant(notANumber);
        CoreOperationNegate negate = new CoreOperationNegate(constant);
        Expression[] expressions = new Expression[1];
        negate.args = expressions;
        expressions[0] = negate;
        ExtensionFunction extensionFunction = new ExtensionFunction(functionName, expressions);
        
        try {
            extensionFunction.compute(null);
            fail("Expected StackOverflowError");
        } catch (StackOverflowError e) {
            // StackOverflowError expected
        }
    }

    @Test(timeout = 4000)
    public void testComputeThrowsArithmeticExceptionWithMod() throws Throwable {
        QName functionName = new QName(",", "");
        Double notANumber = Expression.NOT_A_NUMBER;
        Constant constant = new Constant(notANumber);
        CoreOperationNegate negate = new CoreOperationNegate(constant);
        Expression[] expressions = new Expression[2];
        CoreOperationMod mod = new CoreOperationMod(constant, negate);
        CoreOperationNegate negateMod = new CoreOperationNegate(mod);
        expressions[0] = negateMod;
        ExtensionFunction extensionFunction = new ExtensionFunction(functionName, expressions);
        
        try {
            extensionFunction.compute(null);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.apache.commons.jxpath.ri.compiler.CoreOperationMod", e);
        }
    }

    @Test(timeout = 4000)
    public void testToStringWithNullFunctionName() throws Throwable {
        Expression[] expressions = new Expression[2];
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressions);
        String result = extensionFunction.toString();
        assertEquals("null(null, null)", result);
    }

    @Test(timeout = 4000)
    public void testToStringWithFunctionName() throws Throwable {
        QName functionName = new QName("org.apache.commons.jxpath.ri.Parser");
        ExtensionFunction extensionFunction = new ExtensionFunction(functionName, null);
        String result = extensionFunction.toString();
        assertEquals("org.apache.commons.jxpath.ri.Parser()", result);
    }

    @Test(timeout = 4000)
    public void testComputeValueThrowsNullPointerException() throws Throwable {
        Expression[] expressions = new Expression[8];
        Constant constant = new Constant("$KX%u}cMaD3:");
        expressions[0] = constant;
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressions);
        
        try {
            extensionFunction.computeValue(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.jxpath.ri.compiler.ExtensionFunction", e);
        }
    }

    @Test(timeout = 4000)
    public void testComputeValueThrowsRuntimeExceptionWithMock() throws Throwable {
        Expression[] expressions = new Expression[0];
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressions);
        JXPathContextReferenceImpl contextReference = mock(JXPathContextReferenceImpl.class, new ViolatedAssumptionAnswer());
        doReturn(null).when(contextReference).getFunction(any(QName.class), any(Object[].class));
        doReturn(null).when(contextReference).getNamespaceResolver();
        VariablePointer variablePointer = new VariablePointer(null);
        RootContext rootContext = new RootContext(contextReference, variablePointer);
        
        try {
            extensionFunction.computeValue(rootContext);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.jxpath.ri.compiler.ExtensionFunction", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetFunctionName() throws Throwable {
        QName functionName = new QName("org.apache.commons.jxpath.ri.compiler.ExtensionFunction");
        ExtensionFunction extensionFunction = new ExtensionFunction(functionName, null);
        QName result = extensionFunction.getFunctionName();
        assertSame("Function name should match", functionName, result);
    }

    @Test(timeout = 4000)
    public void testComputeThrowsNullPointerException() throws Throwable {
        Expression[] expressions = new Expression[1];
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressions);
        
        try {
            extensionFunction.compute(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.jxpath.ri.compiler.ExtensionFunction", e);
        }
    }
}