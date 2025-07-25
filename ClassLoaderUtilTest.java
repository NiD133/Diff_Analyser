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
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link ClassLoaderUtil}.
 *
 * <p>
 * This test suite focuses on verifying that {@link ClassLoaderUtil} correctly handles different class loading scenarios,
 * particularly when a context class loader is present or absent, and when the target class is visible or not visible
 * to the primary class loader.
 * </p>
 */
public class ClassLoaderUtilTest {

    private static final String TEST_CASE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoaderUtilTest";
    private static final String EXAMPLE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoadingExampleClass";

    private ClassLoader originalContextClassLoader;

    /**
     * A custom class loader designed to simulate scenarios where a class is either not found or loaded differently.
     *
     * <p>
     * Specifically:
     * <ul>
     * <li>Attempts to load {@link ClassLoadingExampleClass} will always result in a {@link ClassNotFoundException}.</li>
     * <li>Attempts to load {@link ClassLoaderUtilTest} will be loaded by *this* class loader rather than the parent.</li>
     * </ul>
     * </p>
     */
    private static final class TestClassLoader extends ClassLoader {

        private Class<?> testCaseClass = null;

        public TestClassLoader(final ClassLoader classLoader) {
            super(classLoader);
        }

        @Override
        public synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
            if (EXAMPLE_CLASS_NAME.equals(name)) {
                // Simulate class not found for the example class.
                throw new ClassNotFoundException("Simulating ClassNotFoundException for " + EXAMPLE_CLASS_NAME);
            }
            if (TEST_CASE_CLASS_NAME.equals(name)) {
                // Simulate loading the test class itself.
                if (testCaseClass == null) {
                    final URL classUrl = getParent().getResource("org/apache/commons/jxpath/util/ClassLoaderUtilTest.class");
                    byte[] clazzBytes;
                    try {
                        clazzBytes = IOUtils.toByteArray(classUrl);
                    } catch (final IOException e) {
                        throw new ClassNotFoundException("Could not read bytes for " + TEST_CASE_CLASS_NAME, e);
                    }
                    this.testCaseClass = this.defineClass(TEST_CASE_CLASS_NAME, clazzBytes, 0, clazzBytes.length);
                }
                return this.testCaseClass;
            }
            return getParent().loadClass(name);
        }
    }

    /**
     * Performs a JXPath query that attempts to load {@link ClassLoadingExampleClass} dynamically and asserts that a
     * {@link JXPathException} wrapping a {@link ClassNotFoundException} is thrown.
     *
     * <p>
     * This simulates a scenario where JXPath tries to load a class that's intentionally made unavailable.
     * </p>
     */
    public static void callExampleMessageMethodAndAssertClassNotFoundJXPathException() {
        final JXPathContext context = JXPathContext.newContext(new Object());
        assertThrows(JXPathException.class, () -> context.selectSingleNode(EXAMPLE_CLASS_NAME + ".getMessage()"),
                "Expected JXPathException due to ClassNotFoundException for " + EXAMPLE_CLASS_NAME);
    }

    /**
     * Performs a JXPath query that attempts to load {@link ClassLoadingExampleClass} dynamically and asserts that the query
     * successfully returns the expected message.
     *
     * <p>
     * This simulates a scenario where JXPath can successfully load the class.
     * </p>
     */
    public static void callExampleMessageMethodAndAssertSuccess() {
        final JXPathContext context = JXPathContext.newContext(new Object());
        assertEquals("an example class", context.selectSingleNode(EXAMPLE_CLASS_NAME + ".getMessage()"));
    }

    /**
     * Invokes a static method on this test class using the specified class loader. This is used to execute test logic
     * under different class loading contexts.
     *
     * @param classLoader The class loader to use.
     * @param methodName  The name of the static method to invoke (without arguments).
     * @throws ReflectiveOperationException if reflection fails.
     */
    private void executeTestMethodUnderClassLoader(final ClassLoader classLoader, final String methodName) throws ReflectiveOperationException {
        final Class<?> testClass = classLoader.loadClass(TEST_CASE_CLASS_NAME);
        final Method testMethod = testClass.getMethod(methodName, ArrayUtils.EMPTY_CLASS_ARRAY);
        try {
            testMethod.invoke(null, (Object[]) null);
        } catch (final InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                // Re-throw runtime exceptions directly.
                throw (RuntimeException) e.getCause();
            }
            // Wrap other exceptions in a RuntimeException for easy handling.
            throw new RuntimeException(e.getCause());
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
     * Tests that JXPath fails to dynamically load a class when:
     * <ul>
     * <li>The class is not visible to the class loader associated with JXPath.</li>
     * <li>The context class loader is null.</li>
     * </ul>
     */
    @Test
    void testClassLoadFailWithoutContextClassLoader() throws ReflectiveOperationException {
        Thread.currentThread().setContextClassLoader(null);
        final ClassLoader cl = new TestClassLoader(getClass().getClassLoader());
        executeTestMethodUnderClassLoader(cl, "callExampleMessageMethodAndAssertClassNotFoundJXPathException");
    }

    /**
     * Tests that JXPath successfully dynamically loads a class when:
     * <ul>
     * <li>The class is not visible to the class loader associated with JXPath.</li>
     * <li>A context class loader is set, and the context class loader *can* load the class.</li>
     * </ul>
     */
    @Test
    void testClassLoadSuccessWithContextClassLoader() throws ReflectiveOperationException {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        final ClassLoader cl = new TestClassLoader(getClass().getClassLoader());
        executeTestMethodUnderClassLoader(cl, "callExampleMessageMethodAndAssertSuccess");
    }

    /**
     * Tests that JXPath successfully dynamically loads a class when:
     * <ul>
     * <li>The class *is* visible to the class loader associated with JXPath.</li>
     * <li>There is no context class loader set.</li>
     * </ul>
     */
    @Test
    void testClassLoadSuccessWithoutContextClassLoader() {
        Thread.currentThread().setContextClassLoader(null);
        callExampleMessageMethodAndAssertSuccess();
    }

    /**
     * Tests that JXPath falls back to using its own class loader when:
     * <ul>
     * <li>A context class loader *is* set.</li>
     * <li>The context class loader *cannot* load the class.</li>
     * <li>The class *is* visible to the class loader associated with JXPath.</li>
     * </ul>
     */
    @Test
    void testCurrentClassLoaderFallback() {
        final ClassLoader cl = new TestClassLoader(getClass().getClassLoader());
        Thread.currentThread().setContextClassLoader(cl);
        callExampleMessageMethodAndAssertSuccess();
    }
}