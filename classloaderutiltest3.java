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
 * Tests the class loading behavior of JXPath via ClassLoaderUtil, focusing on scenarios
 * involving the Thread's context class loader.
 * <p>
 * This class is part of a suite and contains helpers (e.g., a custom ClassLoader)
 * that may be used by other tests to simulate complex class loading environments.
 */
public class ClassLoaderUtilTestTest3 {

    /**
     * The main test driver class, which is reflectively loaded by a custom classloader
     * in some test scenarios to execute test logic within an isolated environment.
     */
    private static final String TEST_CASE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoaderUtilTest";

    /** A simple class used for dynamic loading tests via JXPath expressions. */
    private static final String EXAMPLE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoadingExampleClass";

    private ClassLoader originalContextClassLoader;

    @BeforeEach
    void setUp() {
        this.originalContextClassLoader = Thread.currentThread().getContextClassLoader();
    }



    @AfterEach
    void tearDown() {
        Thread.currentThread().setContextClassLoader(this.originalContextClassLoader);
    }

    /**
     * Tests that JXPath successfully loads a class using its own class loader when the
     * thread context class loader is set to null.
     */
    @Test
    void jxpathShouldLoadClassSuccessfully_whenContextClassLoaderIsNull() {
        // Arrange: Simulate an environment where no context class loader is available.
        Thread.currentThread().setContextClassLoader(null);

        // Act & Assert: JXPath should fall back to its own class loader and find the class.
        callExampleMessageMethodAndAssertSuccess();
    }

    //<editor-fold desc="Helper Methods for Test Scenarios">

    /**
     * Executes a JXPath query that requires dynamic class loading and asserts it fails.
     * This is used in scenarios where the class is expected to be unloadable.
     */
    public static void callExampleMessageMethodAndAssertClassNotFoundJXPathException() {
        final JXPathContext context = JXPathContext.newContext(new Object());
        final String expression = EXAMPLE_CLASS_NAME + ".getMessage()";
        
        assertThrows(JXPathException.class,
                () -> context.selectSingleNode(expression),
                "JXPath should throw an exception when the class cannot be loaded.");
    }

    /**
     * Executes a JXPath query that requires dynamic class loading and asserts it succeeds.
     */
    public static void callExampleMessageMethodAndAssertSuccess() {
        final JXPathContext context = JXPathContext.newContext(new Object());
        final String expression = EXAMPLE_CLASS_NAME + ".getMessage()";
        final Object result = context.selectSingleNode(expression);
        
        assertEquals("an example class", result);
    }

    /**
     * Executes a static, no-argument method within the context of a specific ClassLoader.
     * <p>
     * This utility works by loading the test class using the provided class loader and then
     * reflectively invoking the specified method. This is essential for testing how JXPath
     * behaves when its execution environment is managed by a non-standard class loader.
     *
     * @param cl         the ClassLoader to use for execution.
     * @param methodName the name of the static, no-argument method to invoke.
     * @throws ReflectiveOperationException if class/method loading or invocation fails.
     */
    private void executeTestMethodUnderClassLoader(final ClassLoader cl, final String methodName) throws ReflectiveOperationException {
        final Class<?> testClassInCustomLoader = cl.loadClass(TEST_CASE_CLASS_NAME);
        final Method testMethod = testClassInCustomLoader.getMethod(methodName, ArrayUtils.EMPTY_CLASS_ARRAY);
        
        try {
            testMethod.invoke(null, (Object[]) null);
        } catch (final InvocationTargetException e) {
            // The actual exception thrown by the test method is wrapped in an InvocationTargetException.
            // We unwrap it to allow the original assertion failures (like AssertionError) to propagate.
            if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            }
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            // Wrap other checked exceptions if necessary, or re-throw as is.
            throw e;
        }
    }
    //</editor-fold>

    /**
     * A custom ClassLoader designed to create specific, isolated class loading scenarios.
     * It follows two primary rules:
     * <ol>
     *   <li>It prevents the loading of {@code ClassLoadingExampleClass}.</li>
     *   <li>It forces itself to load {@code ClassLoaderUtilTest}, isolating it from the parent loader.</li>
     * </ol>
     * All other classes are delegated to the parent loader.
     */
    private static final class SpecificClassIsolatingClassLoader extends ClassLoader {

        private Class<?> testCaseClass;

        public SpecificClassIsolatingClassLoader(final ClassLoader parent) {
            super(parent);
        }

        @Override
        public synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
            // Rule 1: Explicitly fail to load the example class to test failure scenarios.
            if (EXAMPLE_CLASS_NAME.equals(name)) {
                throw new ClassNotFoundException("This class loader is configured to block " + name);
            }

            // Rule 2: Force this class loader to load the main test driver class.
            // This ensures that any code running within it operates in an isolated environment.
            if (TEST_CASE_CLASS_NAME.equals(name)) {
                if (testCaseClass == null) {
                    testCaseClass = findAndDefineClass(name);
                }
                return testCaseClass;
            }

            // Rule 3: Delegate all other requests to the parent classloader (standard behavior).
            return getParent().loadClass(name);
        }
        
        private Class<?> findAndDefineClass(String name) throws ClassNotFoundException {
            final String resourcePath = name.replace('.', '/') + ".class";
            final URL classUrl = getParent().getResource(resourcePath);
            if (classUrl == null) {
                throw new ClassNotFoundException("Could not find class resource: " + resourcePath);
            }

            try {
                final byte[] classBytes = IOUtils.toByteArray(classUrl);
                return this.defineClass(name, classBytes, 0, classBytes.length);
            } catch (final IOException e) {
                throw new ClassNotFoundException("Failed to read class bytes from " + classUrl, e);
            }
        }
    }
}