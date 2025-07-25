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

    private static final String TEST_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoaderUtilTest";
    private static final String EXAMPLE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoadingExampleClass";

    private ClassLoader originalContextClassLoader;

    /**
     * Custom class loader for testing purposes.
     */
    private static final class TestClassLoader extends ClassLoader {

        private Class<?> testClass = null;

        public TestClassLoader(final ClassLoader parentClassLoader) {
            super(parentClassLoader);
        }

        @Override
        public synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
            if (EXAMPLE_CLASS_NAME.equals(name)) {
                throw new ClassNotFoundException();
            }
            if (TEST_CLASS_NAME.equals(name)) {
                if (testClass == null) {
                    final URL classUrl = getParent().getResource("org/apache/commons/jxpath/util/ClassLoaderUtilTest.class");
                    try {
                        byte[] classBytes = IOUtils.toByteArray(classUrl);
                        testClass = defineClass(TEST_CLASS_NAME, classBytes, 0, classBytes.length);
                    } catch (IOException e) {
                        throw new ClassNotFoundException("Failed to load class from URL: " + classUrl, e);
                    }
                }
                return testClass;
            }
            return super.loadClass(name, resolve);
        }
    }

    /**
     * Sets up the original context class loader before each test.
     */
    @BeforeEach
    public void setUp() {
        originalContextClassLoader = Thread.currentThread().getContextClassLoader();
    }

    /**
     * Restores the original context class loader after each test.
     */
    @AfterEach
    public void tearDown() {
        Thread.currentThread().setContextClassLoader(originalContextClassLoader);
    }

    /**
     * Tests that a class not visible to the context class loader cannot be loaded.
     */
    @Test
    void testClassLoadFailWithoutContextClassLoader() throws ReflectiveOperationException {
        Thread.currentThread().setContextClassLoader(null);
        ClassLoader testClassLoader = new TestClassLoader(getClass().getClassLoader());
        executeTestMethod(testClassLoader, "assertClassNotFoundWhenLoadingExampleClass");
    }

    /**
     * Tests that a class can be loaded when the context class loader is set.
     */
    @Test
    void testClassLoadSuccessWithContextClassLoader() throws ReflectiveOperationException {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        ClassLoader testClassLoader = new TestClassLoader(getClass().getClassLoader());
        executeTestMethod(testClassLoader, "assertExampleClassLoadsSuccessfully");
    }

    /**
     * Tests that a class can be loaded without a context class loader.
     */
    @Test
    void testClassLoadSuccessWithoutContextClassLoader() {
        Thread.currentThread().setContextClassLoader(null);
        assertExampleClassLoadsSuccessfully();
    }

    /**
     * Tests that the current class loader is used as a fallback.
     */
    @Test
    void testCurrentClassLoaderFallback() {
        ClassLoader testClassLoader = new TestClassLoader(getClass().getClassLoader());
        Thread.currentThread().setContextClassLoader(testClassLoader);
        assertExampleClassLoadsSuccessfully();
    }

    /**
     * Executes a static method on this class using a specified class loader.
     *
     * @param classLoader the class loader to use
     * @param methodName  the name of the method to invoke
     * @throws ReflectiveOperationException if an error occurs during reflection
     */
    private void executeTestMethod(final ClassLoader classLoader, final String methodName) throws ReflectiveOperationException {
        Class<?> testClass = classLoader.loadClass(TEST_CLASS_NAME);
        Method method = testClass.getMethod(methodName, ArrayUtils.EMPTY_CLASS_ARRAY);
        try {
            method.invoke(null, (Object[]) null);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
        }
    }

    /**
     * Asserts that loading the example class results in a JXPathException.
     */
    public static void assertClassNotFoundWhenLoadingExampleClass() {
        JXPathContext context = JXPathContext.newContext(new Object());
        assertThrows(JXPathException.class, () -> context.selectSingleNode(EXAMPLE_CLASS_NAME + ".getMessage()"),
                "Expected JXPathException when loading " + EXAMPLE_CLASS_NAME);
    }

    /**
     * Asserts that the example class loads successfully.
     */
    public static void assertExampleClassLoadsSuccessfully() {
        JXPathContext context = JXPathContext.newContext(new Object());
        assertEquals("an example class", context.selectSingleNode(EXAMPLE_CLASS_NAME + ".getMessage()"));
    }
}