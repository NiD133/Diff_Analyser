package org.apache.commons.jxpath.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.jxpath.JXPathContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the fallback behavior of JXPath's class loading mechanism.
 *
 * <p>
 * This test verifies that if the Thread's context class loader is unable to
 * find a class, JXPath will successfully fall back to using its own class
 * loader (the one that loaded the JXPath classes themselves), as handled by
 * {@link ClassLoaderUtil}.
 * </p>
 */
public class ClassLoaderUtilFallbackTest {

    private static final String EXAMPLE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoadingExampleClass";
    private static final String EXPECTED_MESSAGE = "an example class";

    private ClassLoader originalContextClassLoader;

    /**
     * Saves the original context class loader before each test.
     */
    @BeforeEach
    public void setUp() {
        this.originalContextClassLoader = Thread.currentThread().getContextClassLoader();
    }

    /**
     * Restores the original context class loader after each test.
     */
    @AfterEach
    public void tearDown() {
        Thread.currentThread().setContextClassLoader(this.originalContextClassLoader);
    }

    /**
     * A custom class loader designed to fail when asked to load a specific class.
     * For all other classes, it follows the standard delegation model.
     */
    private static class FailingContextClassLoader extends ClassLoader {

        public FailingContextClassLoader(final ClassLoader parent) {
            super(parent);
        }

        @Override
        public Class<?> loadClass(final String name) throws ClassNotFoundException {
            // Intentionally fail to load the example class to trigger the fallback mechanism.
            if (EXAMPLE_CLASS_NAME.equals(name)) {
                throw new ClassNotFoundException("This class loader is designed to fail for: " + name);
            }
            return super.loadClass(name);
        }
    }

    /**
     * Verifies that JXPath successfully loads a class using its own class loader
     * when the thread's context class loader is configured to fail.
     */
    @Test
    void jxpathShouldUseCurrentClassLoaderWhenContextClassLoaderFails() {
        // Arrange: Set a context class loader that will fail to load the target class.
        final ClassLoader failingClassLoader = new FailingContextClassLoader(getClass().getClassLoader());
        Thread.currentThread().setContextClassLoader(failingClassLoader);

        // Act: Attempt to execute a JXPath expression that requires dynamic class loading.
        // JXPath should first try the failing context class loader, then fall back
        // to its own class loader, which can find the class.
        final JXPathContext context = JXPathContext.newContext(new Object());
        final String xpath = EXAMPLE_CLASS_NAME + ".getMessage()";
        final Object result = context.selectSingleNode(xpath);

        // Assert: The expression was evaluated successfully, proving the fallback worked.
        assertEquals(EXPECTED_MESSAGE, result, "JXPath should have fallen back to its own class loader.");
    }
}