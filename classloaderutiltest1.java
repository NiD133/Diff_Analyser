package org.apache.commons.jxpath.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests JXPath's class loading behavior under specific ClassLoader configurations.
 * <p>
 * This test employs a complex setup to simulate environments where classes may not be
 * visible to all class loaders:
 * <ol>
 *   <li>A custom {@link HidingClassLoader} is used to prevent the loading of a specific class.</li>
 *   <li>The test logic is placed in a {@code public static} method.</li>
 *   <li>A helper method ({@code runTestInCustomClassLoader}) uses reflection to load this
 *       test class with the {@link HidingClassLoader} and then invoke the static test logic method.</li>
 * </ol>
 * This ensures that the JXPath call originates from code managed by our custom class loader,
 * allowing us to accurately test class resolution fallback mechanisms.
 */
public class ClassLoaderUtilTest {

    private static final String TEST_CLASS_NAME = ClassLoaderUtilTest.class.getName();
    private static final String EXAMPLE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoadingExampleClass";
    private static final String EXAMPLE_CLASS_XPATH = EXAMPLE_CLASS_NAME + ".getMessage()";

    private ClassLoader originalContextClassLoader;

    @BeforeEach
    void saveOriginalContextClassLoader() {
        this.originalContextClassLoader = Thread.currentThread().getContextClassLoader();
    }

    @AfterEach
    void restoreOriginalContextClassLoader() {
        Thread.currentThread().setContextClassLoader(this.originalContextClassLoader);
    }

    /**
     * This is the actual test logic, designed to be called via reflection from
     * a class loaded by {@link HidingClassLoader}.
     * <p>
     * It asserts that JXPath throws a {@link JXPathException} when it tries to
     * access a class that the {@link HidingClassLoader} is configured to hide.
     */
    public static void assertJXPathFailsToLoadHiddenClass() {
        final JXPathContext context = JXPathContext.newContext(new Object());

        // We expect a JXPathException because HidingClassLoader will throw
        // ClassNotFoundException when JXPath tries to load EXAMPLE_CLASS_NAME.
        assertThrows(JXPathException.class,
                () -> context.selectSingleNode(EXAMPLE_CLASS_XPATH),
                "JXPath should have failed to load the hidden class " + EXAMPLE_CLASS_NAME);
    }

    /**
     * Loads this test class using the provided custom class loader and invokes a
     * static, no-argument method on it by name.
     *
     * @param customClassLoader the ClassLoader to use.
     * @param methodName        the name of the static method to invoke.
     * @throws Throwable       if the invoked method throws any exception or error.
     */
    private void runTestInCustomClassLoader(final ClassLoader customClassLoader, final String methodName) throws Throwable {
        final Class<?> testClass = customClassLoader.loadClass(TEST_CLASS_NAME);
        final Method testMethod = testClass.getMethod(methodName, new Class[0]);
        try {
            testMethod.invoke(null, (Object[]) null);
        } catch (final InvocationTargetException e) {
            // Unwrap and rethrow the actual exception from the test logic.
            // This is critical for propagating assertion failures (which are Errors).
            throw e.getCause();
        }
    }

    /**
     * A custom ClassLoader that modifies standard behavior for testing purposes.
     * <ol>
     *   <li>It explicitly prevents loading {@code ClassLoadingExampleClass} by always
     *       throwing {@link ClassNotFoundException}, simulating a missing class.</li>
     *   <li>It forces the test class itself ({@code ClassLoaderUtilTest}) to be loaded
     *       by this ClassLoader instance. This ensures that the test logic (which
     *       calls JXPath) is executed within the context of this custom loader.</li>
     * </ol>
     * All other class loading requests are delegated to the parent loader.
     */
    private static final class HidingClassLoader extends ClassLoader {

        private Class<?> testCaseClass;

        public HidingClassLoader(final ClassLoader parent) {
            super(parent);
        }

        @Override
        public synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
            if (EXAMPLE_CLASS_NAME.equals(name)) {
                throw new ClassNotFoundException("This class loader is configured to hide " + name);
            }

            if (TEST_CLASS_NAME.equals(name)) {
                if (testCaseClass == null) {
                    final String classResource = TEST_CLASS_NAME.replace('.', '/') + ".class";
                    final URL classUrl = getParent().getResource(classResource);
                    try {
                        final byte[] classBytes = IOUtils.toByteArray(classUrl);
                        this.testCaseClass = defineClass(TEST_CLASS_NAME, classBytes, 0, classBytes.length);
                    } catch (final IOException e) {
                        throw new ClassNotFoundException("Could not read class bytes from " + classUrl, e);
                    }
                }
                return this.testCaseClass;
            }
            return getParent().loadClass(name);
        }
    }

    @Test
    void jxpathFailsToLoadClassWhenContextClassLoaderIsNullAndClassIsHidden() throws Throwable {
        // Arrange: Set up an environment where the context class loader is null
        // and our custom loader is responsible for loading the test logic. This
        // custom loader is designed to hide EXAMPLE_CLASS_NAME.
        Thread.currentThread().setContextClassLoader(null);
        final ClassLoader hidingClassLoader = new HidingClassLoader(getClass().getClassLoader());

        // Act & Assert: Run the test logic within the custom class loader's context.
        // The assertion that the class load fails is inside the invoked method.
        runTestInCustomClassLoader(hidingClassLoader, "assertJXPathFailsToLoadHiddenClass");
    }
}