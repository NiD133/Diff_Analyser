package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathFunctionNotFoundException;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.axes.NodeSetContext;
import org.apache.commons.jxpath.ri.axes.RootContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPointer;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExtensionFunctionTest {

    @Test
    public void testGetFunctionNameReturnsCorrectName() {
        QName expectedName = new QName("namespace", "functionName");
        ExtensionFunction extensionFunction = new ExtensionFunction(expectedName, new Expression[0]);
        QName actualName = extensionFunction.getFunctionName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testComputeContextDependentReturnsTrue() {
        ExtensionFunction extensionFunction = new ExtensionFunction(null, new Expression[0]);
        assertTrue(extensionFunction.computeContextDependent());
    }

    @Test
    public void testToStringWithArguments() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);

        when(arg1.toString()).thenReturn("arg1");
        when(arg2.toString()).thenReturn("arg2");

        Expression[] args = {arg1, arg2};
        ExtensionFunction extensionFunction = new ExtensionFunction(new QName("ns", "func"), args);
        String expectedString = "ns:func(arg1, arg2)";
        assertEquals(expectedString, extensionFunction.toString());
    }

    @Test
    public void testToStringWithoutArguments() {
        ExtensionFunction extensionFunction = new ExtensionFunction(new QName("ns", "func"), new Expression[0]);
        String expectedString = "ns:func()";
        assertEquals(expectedString, extensionFunction.toString());
    }

    @Test(expected = RuntimeException.class)
    public void testComputeValueThrowsRuntimeExceptionWhenFunctionNotFound() {
        // Arrange: Create an ExtensionFunction with a name that doesn't correspond to a registered function.
        QName functionName = new QName("nonExistentNamespace", "nonExistentFunction");
        ExtensionFunction extensionFunction = new ExtensionFunction(functionName, null);

        // Arrange: Create a JXPathContext and a RootContext.
        JXPathContextReferenceImpl jXPathContext = (JXPathContextReferenceImpl) JXPathContext.newContext(null);
        RootContext rootContext = new RootContext(jXPathContext, null);

        // Act: Call computeValue on the ExtensionFunction.  This should throw a RuntimeException.
        extensionFunction.computeValue(rootContext);
    }

    @Test
    public void testGetFunctionNameReturnsNullWhenNameIsNull() {
        ExtensionFunction extensionFunction = new ExtensionFunction(null, new Expression[0]);
        assertNull(extensionFunction.getFunctionName());
    }

    @Test(expected = NullPointerException.class)
    public void testComputeThrowsNPEWhenEvalContextIsNull() {
        Expression[] expressionArray = new Expression[6];
        ExtensionFunction extensionFunction = new ExtensionFunction(null, expressionArray);
        extensionFunction.compute(null);
    }

    @Test
    public void testComputeCallsComputeValue() {
        QName qName = new QName("test", "test");
        ExtensionFunction extensionFunction = new ExtensionFunction(qName, null);
        Object object = new Object();
        Locale locale = new Locale("test", "test");
        NodePointer nodePointer = NodePointer.newNodePointer(qName, object, locale);
        JXPathContextReferenceImpl jXPathContextReference = new JXPathContextReferenceImpl((JXPathContext) null, qName, nodePointer);
        RootContext rootContext = new RootContext(jXPathContextReference, nodePointer);

        try {
            extensionFunction.compute(rootContext);
            fail("RuntimeException expected: Undefined function: test:test");
        } catch (RuntimeException e) {
            assertEquals("Undefined function: test:test", e.getMessage());
        }
    }

    @Test
    public void testComputeValueThrowsRuntimeException_whenFunctionNameIsNull() {
        ExtensionFunction extensionFunction = new ExtensionFunction(null, null);
        JXPathContextReferenceImpl jXPathContextReference = (JXPathContextReferenceImpl) JXPathContext.newContext(null);
        RootContext rootContext = new RootContext(jXPathContextReference, null);

        try {
            extensionFunction.computeValue(rootContext);
            fail("RuntimeException expected: Undefined function: null");
        } catch (RuntimeException e) {
            assertEquals("Undefined function: null", e.getMessage());
        }
    }
}