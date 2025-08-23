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

public class ClassLoaderUtilTestTest1 {

    // special class loader which may be unable to load those classes.
    private static final String TEST_CASE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoaderUtilTest";

    private static final String EXAMPLE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoadingExampleClass";

    private ClassLoader orginalContextClassLoader;

    /**
     * Performs a basic query that requires a class be loaded dynamically by JXPath and asserts the dynamic class load fails.
     */
    public static void callExampleMessageMethodAndAssertClassNotFoundJXPathException() {
        final JXPathContext context = JXPathContext.newContext(new Object());
        assertThrows(JXPathException.class, () -> context.selectSingleNode(EXAMPLE_CLASS_NAME + ".getMessage()"), "We should not be able to load " + EXAMPLE_CLASS_NAME + ".");
    }

    /**
     * Performs a basic query that requires a class be loaded dynamically by JXPath and asserts the dynamic class load succeeds.
     */
    public static void callExampleMessageMethodAndAssertSuccess() {
        final JXPathContext context = JXPathContext.newContext(new Object());
        assertEquals("an example class", context.selectSingleNode(EXAMPLE_CLASS_NAME + ".getMessage()"));
    }

    /**
     * Loads this class through the given class loader and then invokes the indicated no argument static method of the class.
     *
     * @param cl         the class loader under which to invoke the method.
     * @param methodName the name of the static no argument method on this class to invoke.
     * @throws ReflectiveOperationException on test failures.
     */
    private void executeTestMethodUnderClassLoader(final ClassLoader cl, final String methodName) throws ReflectiveOperationException {
        final Class<?> testClass = cl.loadClass(TEST_CASE_CLASS_NAME);
        final Method testMethod = testClass.getMethod(methodName, ArrayUtils.EMPTY_CLASS_ARRAY);
        try {
            testMethod.invoke(null, (Object[]) null);
        } catch (final InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                // Allow the runtime exception to propagate up.
                throw (RuntimeException) e.getCause();
            }
        }
    }

    /**
     * Setup for the tests.
     */
    @BeforeEach
    public void setUp() {
        this.orginalContextClassLoader = Thread.currentThread().getContextClassLoader();
    }

    /**
     * Cleanup for the tests.
     */
    @AfterEach
    public void tearDown() {
        Thread.currentThread().setContextClassLoader(this.orginalContextClassLoader);
    }

    /**
     * A simple class loader which delegates all class loading to its parent with two exceptions. First, attempts to load the class
     * {@code org.apache.commons.jxpath.util.ClassLoaderUtilTest} will always result in a ClassNotFoundException. Second, loading the class
     * {@code org.apache.commons.jxpath.util.ClassLoadingExampleClass} will result in the class being loaded by this class loader, regardless of whether the
     * parent can/has loaded it.
     */
    private static final class TestClassLoader extends ClassLoader {

        private Class<?> testCaseClass = null;

        public TestClassLoader(final ClassLoader classLoader) {
            super(classLoader);
        }

        @Override
        public synchronized Class<?> loadClass(final String name, final boolean resolved) throws ClassNotFoundException {
            if (EXAMPLE_CLASS_NAME.equals(name)) {
                throw new ClassNotFoundException();
            }
            if (TEST_CASE_CLASS_NAME.equals(name)) {
                if (testCaseClass == null) {
                    final URL classUrl = getParent().getResource("org/apache/commons/jxpath/util/ClassLoaderUtilTest.class");
                    byte[] clazzBytes;
                    try {
                        clazzBytes = IOUtils.toByteArray(classUrl);
                    } catch (final IOException e) {
                        throw new ClassNotFoundException(classUrl.toString(), e);
                    }
                    this.testCaseClass = this.defineClass(TEST_CASE_CLASS_NAME, clazzBytes, 0, clazzBytes.length);
                }
                return this.testCaseClass;
            }
            return getParent().loadClass(name);
        }
    }

    /**
     * Tests that JXPath cannot dynamically load a class, which is not visible to its class loader, when the context class loader is null.
     *
     * @throws ReflectiveOperationException on test failures.
     */
    @Test
    void testClassLoadFailWithoutContextClassLoader() throws ReflectiveOperationException {
        Thread.currentThread().setContextClassLoader(null);
        final ClassLoader cl = new TestClassLoader(getClass().getClassLoader());
        executeTestMethodUnderClassLoader(cl, "callExampleMessageMethodAndAssertClassNotFoundJXPathException");
    }
}
