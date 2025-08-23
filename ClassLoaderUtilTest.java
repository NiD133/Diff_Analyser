package org.apache.commons.jxpath.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ClassLoaderUtil.
 */
public class ClassLoaderUtilTest {

    private static final String TEST_CASE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoaderUtilTest";
    private static final String EXAMPLE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoadingExampleClass";

    private ClassLoader originalContextClassLoader;

    /**
     * Custom ClassLoader that simulates specific class loading behavior for testing.
     */
    private static final class TestClassLoader extends ClassLoader {

        private Class<?> testCaseClass = null;

        public TestClassLoader(final ClassLoader parentClassLoader) {
            super(parentClassLoader);
        }

        @Override
        public synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
            if (EXAMPLE_CLASS_NAME.equals(name)) {
                throw new ClassNotFoundException("Simulated class not found for: " + name);
            }
            if (TEST_CASE_CLASS_NAME.equals(name)) {
                if (testCaseClass == null) {
                    final URL classUrl = getParent().getResource("org/apache/commons/jxpath/util/ClassLoaderUtilTest.class");
                    byte[] classBytes;
                    try {
                        classBytes = IOUtils.toByteArray(classUrl);
                    } catch (final IOException e) {
                        throw new ClassNotFoundException("Failed to load class bytes from URL: " + classUrl, e);
                    }
                    this.testCaseClass = this.defineClass(TEST_CASE_CLASS_NAME, classBytes, 0, classBytes.length);
                }
                return this.testCaseClass;
            }
            return super.loadClass(name, resolve);
        }
    }

    @BeforeEach
    public void setUp() {
        this.originalContextClassLoader = Thread.currentThread().getContextClassLoader();
    }

    @AfterEach
    public void tearDown() {
        Thread.currentThread().setContextClassLoader(this.originalContextClassLoader);
    }

    /**
     * Test that JXPath fails to load a class when the context class loader is null.
     */
    @Test
    void testClassLoadFailWithoutContextClassLoader() throws ReflectiveOperationException {
        Thread.currentThread().setContextClassLoader(null);
        final ClassLoader testClassLoader = new TestClassLoader(getClass().getClassLoader());
        executeTestMethodUnderClassLoader(testClassLoader, "callExampleMessageMethodAndAssertClassNotFoundJXPathException");
    }

    /**
     * Test that JXPath successfully loads a class when the context class loader is set.
     */
    @Test
    void testClassLoadSuccessWithContextClassLoader() throws ReflectiveOperationException {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        final ClassLoader testClassLoader = new TestClassLoader(getClass().getClassLoader());
        executeTestMethodUnderClassLoader(testClassLoader, "callExampleMessageMethodAndAssertSuccess");
    }

    /**
     * Test that JXPath successfully loads a class without a context class loader.
     */
    @Test
    void testClassLoadSuccessWithoutContextClassLoader() {
        Thread.currentThread().setContextClassLoader(null);
        callExampleMessageMethodAndAssertSuccess();
    }

    /**
     * Test that JXPath uses its class loader when the context class loader cannot load the class.
     */
    @Test
    void testCurrentClassLoaderFallback() {
        final ClassLoader testClassLoader = new TestClassLoader(getClass().getClassLoader());
        Thread.currentThread().setContextClassLoader(testClassLoader);
        callExampleMessageMethodAndAssertSuccess();
    }

    /**
     * Executes a static method on this class using a specific class loader.
     *
     * @param classLoader the class loader to use
     * @param methodName  the name of the method to invoke
     * @throws ReflectiveOperationException if an error occurs during reflection
     */
    private void executeTestMethodUnderClassLoader(final ClassLoader classLoader, final String methodName) throws ReflectiveOperationException {
        final Class<?> testClass = classLoader.loadClass(TEST_CASE_CLASS_NAME);
        final Method testMethod = testClass.getMethod(methodName, ArrayUtils.EMPTY_CLASS_ARRAY);
        try {
            testMethod.invoke(null, (Object[]) null);
        } catch (final InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
        }
    }

    /**
     * Asserts that a JXPathException is thrown when attempting to load a class that should not be found.
     */
    public static void callExampleMessageMethodAndAssertClassNotFoundJXPathException() {
        final JXPathContext context = JXPathContext.newContext(new Object());
        assertThrows(JXPathException.class, () -> context.selectSingleNode(EXAMPLE_CLASS_NAME + ".getMessage()"),
                "Expected JXPathException due to class not found: " + EXAMPLE_CLASS_NAME);
    }

    /**
     * Asserts that the example message method is successfully called.
     */
    public static void callExampleMessageMethodAndAssertSuccess() {
        final JXPathContext context = JXPathContext.newContext(new Object());
        assertEquals("an example class", context.selectSingleNode(EXAMPLE_CLASS_NAME + ".getMessage()"),
                "Expected successful class load and method call.");
    }
}