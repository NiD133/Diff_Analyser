/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the class loading behavior of {@link ClassLoaderUtil}, which is used by JXPath
 * for dynamic class resolution in XPath expressions.
 *
 * The tests verify the fallback mechanism between the Thread's Context ClassLoader
 * and JXPath's own class loader.
 */
@DisplayName("ClassLoaderUtil Tests")
class ClassLoaderUtilTest {

    // These must be string literals, not Class.getName(), because these tests
    // manipulate class loaders in ways that might prevent class resolution.
    private static final String TEST_CASE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoaderUtilTest";
    private static final String EXAMPLE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoadingExampleClass";
    private static final String XPATH_EXPRESSION = EXAMPLE_CLASS_NAME + ".getMessage()";
    private static final String EXPECTED_RESULT = "an example class";

    private ClassLoader originalContextClassLoader;

    @BeforeEach
    void setUp() {
        this.originalContextClassLoader = Thread.currentThread().getContextClassLoader();
    }

    @AfterEach
    void tearDown() {
        Thread.currentThread().setContextClassLoader(this.originalContextClassLoader);
    }

    // --- Direct Tests (Simplified) ---

    @Test
    void testLoadClass_Succeeds_WhenClassIsOnJXPathClassLoaderAndContextLoaderIsNull() {
        // Arrange: The class is available to JXPath's default class loader,
        // but no context class loader is set.
        Thread.currentThread().setContextClassLoader(null);

        // Act
        final JXPathContext context = JXPathContext.newContext(new Object());
        final Object result = context.selectSingleNode(XPATH_EXPRESSION);

        // Assert: JXPath's own class loader should find the class.
        assertEquals(EXPECTED_RESULT, result);
    }

    @Test
    void testLoadClass_Succeeds_ByFallingBackToJXPathClassLoaderWhenContextLoaderFails() {
        // Arrange: The context class loader is set to one that explicitly
        // CANNOT find the required class.
        final ClassLoader failingClassLoader = new ClassCantBeFoundClassLoader(EXAMPLE_CLASS_NAME, getClass().getClassLoader());
        Thread.currentThread().setContextClassLoader(failingClassLoader);

        // Act
        final JXPathContext context = JXPathContext.newContext(new Object());
        final Object result = context.selectSingleNode(XPATH_EXPRESSION);

        // Assert: JXPath should fail with the context class loader, but succeed
        // by falling back to its own class loader.
        assertEquals(EXPECTED_RESULT, result);
    }

    // --- Sandboxed Tests (for advanced scenarios) ---

    @Test
    void testLoadClass_Fails_WhenClassIsUnavailableToAllClassLoaders() throws ReflectiveOperationException {
        // Arrange: We create a sandboxed environment where JXPath's own class loader
        // cannot find the class. We also set the context class loader to null.
        Thread.currentThread().setContextClassLoader(null);
        final ClassLoader sandboxClassLoader = new SandboxClassLoader(getClass().getClassLoader());

        // Act & Assert: We run the test logic inside the sandbox and expect a
        // JXPathException because the class cannot be found by any loader.
        runTestInSandbox(sandboxClassLoader, "assertXPathLoadFails");
    }

    @Test
    void testLoadClass_Succeeds_WhenClassIsAvailableOnlyToContextClassLoader() throws ReflectiveOperationException {
        // Arrange: We create a sandboxed environment where JXPath's own class loader
        // cannot find the class. However, we set the context class loader to one that CAN.
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        final ClassLoader sandboxClassLoader = new SandboxClassLoader(getClass().getClassLoader());

        // Act & Assert: We run the test logic inside the sandbox. JXPath should
        // use the context class loader to successfully find the class.
        runTestInSandbox(sandboxClassLoader, "assertXPathLoadSucceeds");
    }

    // --- Helper Methods and Classes for Sandboxed Tests ---

    /**
     * A helper method for sandboxed tests. It must be public static to be invoked via reflection.
     * This method asserts that JXPath can successfully load the example class and get the message.
     */
    public static void assertXPathLoadSucceeds() {
        final JXPathContext context = JXPathContext.newContext(new Object());
        assertEquals(EXPECTED_RESULT, context.selectSingleNode(XPATH_EXPRESSION));
    }

    /**
     * A helper method for sandboxed tests. It must be public static to be invoked via reflection.
     * This method asserts that JXPath fails with a JXPathException when trying to load the class.
     */
    public static void assertXPathLoadFails() {
        final JXPathContext context = JXPathContext.newContext(new Object());
        assertThrows(JXPathException.class,
                () -> context.selectSingleNode(XPATH_EXPRESSION),
                "JXPath should not have been able to load " + EXAMPLE_CLASS_NAME);
    }

    /**
     * Executes a static test method from this class within a specific, "sandboxed" class loader.
     * <p>
     * <b>Why is this complex setup needed?</b>
     * To properly test {@link ClassLoaderUtil}, we need to control the class loader that
     * JXPath itself uses (i.e., {@code ClassLoaderUtil.class.getClassLoader()}). This cannot be
     * changed at runtime.
     * This method works around that by loading the entire test class within a custom
     * {@link SandboxClassLoader}. When the test logic is invoked via reflection, any classes
     * it uses (like JXPathContext and ClassLoaderUtil) are also loaded by the sandbox loader.
     * This gives us full control over the class loading environment to simulate scenarios
     * that are otherwise impossible to test.
     *
     * @param sandboxClassLoader the custom class loader to run under.
     * @param methodName         the name of the public static method in this class to invoke.
     * @throws ReflectiveOperationException on test failures.
     */
    private void runTestInSandbox(final ClassLoader sandboxClassLoader, final String methodName) throws ReflectiveOperationException {
        final Class<?> testClassInSandbox = sandboxClassLoader.loadClass(TEST_CASE_CLASS_NAME);
        final Method testMethod = testClassInSandbox.getMethod(methodName);
        try {
            testMethod.invoke(null);
        } catch (final InvocationTargetException e) {
            // Propagate runtime exceptions and assertion errors from the sandboxed execution
            if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            }
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw e;
        }
    }

    /**
     * A custom ClassLoader that simulates a "sandboxed" environment. It is designed to:
     * <ol>
     *   <li>Intentionally fail to load {@code ClassLoadingExampleClass} to simulate it being unavailable.</li>
     *   <li>Force-load the {@code ClassLoaderUtilTest} class itself, ensuring the test logic runs within this sandbox.</li>
     *   <li>Delegate all other class loading to its parent.</li>
     * </ol>
     */
    private static final class SandboxClassLoader extends ClassLoader {
        private Class<?> testCaseClass = null;

        SandboxClassLoader(final ClassLoader parent) {
            super(parent);
        }

        @Override
        public synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
            if (EXAMPLE_CLASS_NAME.equals(name)) {
                throw new ClassNotFoundException("Class is not available in this sandbox: " + name);
            }
            if (TEST_CASE_CLASS_NAME.equals(name)) {
                if (testCaseClass == null) {
                    final String resourceName = TEST_CASE_CLASS_NAME.replace('.', '/') + ".class";
                    final URL classUrl = getParent().getResource(resourceName);
                    try {
                        final byte[] clazzBytes = IOUtils.toByteArray(classUrl);
                        this.testCaseClass = this.defineClass(TEST_CASE_CLASS_NAME, clazzBytes, 0, clazzBytes.length);
                    } catch (final IOException e) {
                        throw new ClassNotFoundException("Could not read class bytes for " + name, e);
                    }
                }
                return this.testCaseClass;
            }
            return getParent().loadClass(name);
        }
    }

    /**
     * A simple ClassLoader that intentionally fails to load a specific class,
     * used for testing fallback behavior.
     */
    private static final class ClassCantBeFoundClassLoader extends ClassLoader {
        private final String classNameToFail;

        ClassCantBeFoundClassLoader(final String classNameToFail, final ClassLoader parent) {
            super(parent);
            this.classNameToFail = classNameToFail;
        }

        @Override
        public Class<?> loadClass(final String name) throws ClassNotFoundException {
            if (classNameToFail.equals(name)) {
                throw new ClassNotFoundException("Simulating failure to find " + name);
            }
            return super.loadClass(name);
        }
    }
}