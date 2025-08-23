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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests that JXPath's class loading mechanism (via ClassLoaderUtil) correctly uses the
 * Thread Context Class Loader as a fallback.
 *
 * <p>
 * This test employs a complex setup to simulate a specific class loading environment:
 * <ol>
 *   <li><b>A custom {@link FailingClassLoader}</b> is created. This class loader is designed to
 *       intentionally fail when asked to load a specific class ({@code ClassLoadingExampleClass}).</li>
 *   <li><b>A "remote" test executor</b> (the {@link JXPathExecutor} inner class) is loaded
 *       using this {@code FailingClassLoader}. This means any code inside {@code JXPathExecutor}
 *       runs in an environment where its own class loader cannot find the target class.</li>
 *   <li><b>The Thread Context Class Loader</b> is set to the default application class loader,
 *       which <i>can</i> load the target class.</li>
 * </ol>
 * The test passes if JXPath, when invoked from within the {@code JXPathExecutor}, successfully
 * loads the class. This proves it correctly fell back from the failing current class loader to the
 * working Thread Context Class Loader.
 * </p>
 */
class ClassLoaderUtilContextClassLoaderTest {

    private static final String EXAMPLE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoadingExampleClass";
    private static final String JXPATH_EXECUTOR_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoaderUtilContextClassLoaderTest$JXPathExecutor";

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
     * This class contains the actual JXPath calls. It is loaded and executed under a custom
     * class loader to simulate a specific class loading environment.
     */
    public static class JXPathExecutor {
        /**
         * Performs a JXPath query that requires dynamic class loading and asserts success.
         */
        public static void callExampleMessageMethodAndAssertSuccess() {
            final JXPathContext context = JXPathContext.newContext(new Object());
            final Object result = context.selectSingleNode(EXAMPLE_CLASS_NAME + ".getMessage()");
            assertEquals("an example class", result);
        }

        /**
         * Performs a JXPath query that requires dynamic class loading and asserts it fails.
         */
        public static void callExampleMessageMethodAndAssertFailure() {
            final JXPathContext context = JXPathContext.newContext(new Object());
            assertThrows(JXPathException.class,
                () -> context.selectSingleNode(EXAMPLE_CLASS_NAME + ".getMessage()"),
                "Should not be able to load " + EXAMPLE_CLASS_NAME);
        }
    }

    /**
     * A custom class loader that simulates a specific class loading scenario.
     * <p>
     * It has two special behaviors:
     * <ol>
     *   <li>It intentionally throws a {@link ClassNotFoundException} for {@code ClassLoadingExampleClass}.
     *       This forces JXPath to try its fallback class loading mechanism (the context class loader).</li>
     *   <li>It can load the {@code JXPathExecutor} class. This is necessary to run test logic
     *       within the context of this specific class loader.</li>
     * </ol>
     * All other class loading requests are delegated to its parent.
     */
    private static final class FailingClassLoader extends ClassLoader {
        private Class<?> jxpathExecutorClass;

        FailingClassLoader(final ClassLoader parent) {
            super(parent);
        }

        @Override
        public synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
            if (EXAMPLE_CLASS_NAME.equals(name)) {
                // Intentionally fail to load the example class to test fallback mechanisms.
                throw new ClassNotFoundException("This class loader is designed to fail for: " + name);
            }

            if (JXPATH_EXECUTOR_CLASS_NAME.equals(name)) {
                // Load the executor class ourselves to run code within this loader's context.
                if (jxpathExecutorClass == null) {
                    final String resourcePath = JXPATH_EXECUTOR_CLASS_NAME.replace('.', '/') + ".class";
                    try {
                        final URL classUrl = getParent().getResource(resourcePath);
                        final byte[] classBytes = IOUtils.toByteArray(classUrl);
                        this.jxpathExecutorClass = defineClass(JXPATH_EXECUTOR_CLASS_NAME, classBytes, 0, classBytes.length);
                    } catch (final IOException e) {
                        throw new ClassNotFoundException("Failed to read class bytes for " + name, e);
                    }
                }
                return this.jxpathExecutorClass;
            }

            return getParent().loadClass(name);
        }
    }

    @Test
    void jxpathShouldUseContextClassLoaderAsFallback() throws ReflectiveOperationException {
        // Arrange:
        // 1. Set the context class loader to one that CAN load the target class.
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());

        // 2. Create a custom class loader that CANNOT load the target class.
        final ClassLoader failingLoader = new FailingClassLoader(getClass().getClassLoader());

        // Act:
        // Execute a JXPath call from within the context of the failing class loader.
        // JXPath should fail to load the class with this loader, but then succeed by
        // using the context class loader as a fallback.
        executeStaticMethodInDifferentClassLoader(failingLoader, "callExampleMessageMethodAndAssertSuccess");

        // Assert:
        // The assertion is performed inside the executed method, which will only succeed
        // if the class was loaded correctly via the context class loader.
    }

    /**
     * Executes a static, no-argument method from the {@link JXPathExecutor} class using a
     * provided class loader.
     *
     * @param classLoader the ClassLoader to use for execution.
     * @param methodName  the name of the static method to invoke.
     * @throws ReflectiveOperationException if the class or method cannot be loaded or invoked.
     */
    private void executeStaticMethodInDifferentClassLoader(final ClassLoader classLoader, final String methodName)
            throws ReflectiveOperationException {
        try {
            final Class<?> testClass = classLoader.loadClass(JXPATH_EXECUTOR_CLASS_NAME);
            final Method testMethod = testClass.getMethod(methodName);
            testMethod.invoke(null);
        } catch (final InvocationTargetException e) {
            // Propagate runtime exceptions from the test method itself.
            if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            }
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw e;
        }
    }
}