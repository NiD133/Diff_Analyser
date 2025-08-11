package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.axes.RootContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPointer;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Readable, maintainable tests for ExtensionFunction.
 * 
 * What we cover:
 * - The function name passed to the constructor is returned by getFunctionName().
 * - computeContextDependent() always returns true.
 * - toString() produces a readable and predictable representation.
 * - computeValue() signals an error if the function cannot be resolved.
 */
public class ExtensionFunctionTest {

    /**
     * Creates a RootContext backed by a simple bean to evaluate functions against.
     * Keeps test setup noise out of the individual test methods.
     */
    private static RootContext newRootContext(final Object rootBean) {
        final JXPathContextReferenceImpl ctx =
            (JXPathContextReferenceImpl) JXPathContext.newContext(rootBean);
        final QName rootName = new QName("root");
        final BeanPointer rootPtr =
            (BeanPointer) NodePointer.newNodePointer(rootName, rootBean, Locale.getDefault());
        return new RootContext(ctx, rootPtr);
    }

    @Test
    public void getFunctionName_returnsConstructorArgument() {
        // Arrange
        final QName name = new QName("ns", "myFunc");

        // Act
        final ExtensionFunction fn = new ExtensionFunction(name, new Expression[0]);

        // Assert
        assertSame(name, fn.getFunctionName());
    }

    @Test
    public void computeContextDependent_alwaysTrue() {
        // Arrange
        final ExtensionFunction fn = new ExtensionFunction(null, new Expression[0]);

        // Act + Assert
        assertTrue(fn.computeContextDependent());
    }

    @Test
    public void toString_withNullNameAndTwoArgs_isReadable() {
        // Arrange
        final ExtensionFunction fn = new ExtensionFunction(null, new Expression[2]);

        // Act
        final String s = fn.toString();

        // Assert
        // Matches behavior: renders null name and two null arguments.
        assertEquals("null(null, null)", s);
    }

    @Test
    public void toString_withNameAndNoArgs_showsEmptyArgList() {
        // Arrange
        final QName name = new QName("org.apache.commons.jxpath.ri.Parser");

        // Act
        final ExtensionFunction fn = new ExtensionFunction(name, null);
        final String s = fn.toString();

        // Assert
        assertEquals("org.apache.commons.jxpath.ri.Parser()", s);
    }

    @Test
    public void computeValue_whenFunctionIsUnknown_throws() {
        // Arrange
        final QName unknown = new QName("custom", "doesNotExist");
        final ExtensionFunction fn = new ExtensionFunction(unknown, new Expression[0]);
        final RootContext ctx = newRootContext(new Object());

        // Act + Assert
        try {
            fn.computeValue(ctx);
            fail("Expected an exception because the function is not registered");
        } catch (RuntimeException ex) {
            // Expected: JXPath reports unknown/undefined function resolution failures
            // via a RuntimeException/JXPathFunctionNotFoundException family.
        }
    }
}