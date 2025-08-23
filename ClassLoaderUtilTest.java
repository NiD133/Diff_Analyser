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
 * Tests class loading behavior leveraged by ClassLoaderUtil through JXPath.
 *
 * The scenarios verify how classes are resolved when:
 * - the thread context class loader (TCCL) is null,
 * - the TCCL can load the target class, or
 * - the TCCL cannot load the target class and JXPath must fall back.
 */
public class ClassLoaderUtilTest {

    // These must be string literals (not Class#getName()) so they can be used
    // from versions of this class loaded by a custom class loader.
    private static final String TEST_CLASS_FQCN = "org.apache.commons.jxpath.util.ClassLoaderUtilTest";
    private static final String EXAMPLE_CLASS_FQCN = "org.apache.commons.jxpath.util.ClassLoadingExampleClass";
    private static final String TEST_CLASS_RESOURCE = "org/apache/commons/jxpath/util/ClassLoaderUtilTest.class";

    // Method names called reflectively from a copy of this class loaded by a custom ClassLoader.
    private static final String M_ASSERT_FAIL = "callExampleMessageMethodAndAssertClassNotFoundJXPathException";
    private static final String M_ASSERT_SUCCESS = "callExampleMessageMethodAndAssertSuccess";

    private ClassLoader originalContextClassLoader;

    /**
     * Custom class loader that:
     * - Always fails to load ClassLoadingExampleClass (simulates class not being visible to this loader).
     * - Always loads a private copy of this test class itself (ensures reflective calls run under this loader).
     * - Delegates all other class loads to its parent.
     */
    private static final class SelectiveDelegatingClassLoader extends ClassLoader {

        private Class<?> selfLoadedTestClass;

        SelectiveDelegatingClassLoader(final ClassLoader parent) {
            super(parent);
        }

        @Override
        protected synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
            // Intentionally hide the example class from this loader
            if (EXAMPLE_CLASS_FQCN.equals(name)) {
                throw new ClassNotFoundException(name);
            }

            // Always load our own copy of the test class under this loader
            if (TEST_CLASS_FQCN.equals(name)) {
                if (selfLoadedTestClass == null) {
                    final URL classUrl = getParent().getResource(TEST_CLASS_RESOURCE);
                    if (classUrl == null) {
                        throw new ClassNotFoundException("Resource not found: " + TEST_CLASS_RESOURCE);
                    }
                    final byte[] bytes;
                    try {
                        bytes = IOUtils.toByteArray(classUrl);
                    } catch (final IOException e) {
                        throw new ClassNotFoundException("Unable to read " + TEST_CLASS_RESOURCE, e);
                    }
                    selfLoadedTestClass = defineClass(TEST_CLASS_FQCN, bytes, 0, bytes.length);
                }
                if (resolve) {
                    resolveClass(selfLoadedTestClass);
                }
                return selfLoadedTestClass;
            }

            // Everything else comes from the parent
            return super.loadClass(name, resolve);
        }
    }

    /**
     * Creates a fresh JXPathContext and asserts the target class cannot be dynamically loaded.
     * This method is invoked reflectively under a custom class loader in some tests.
     */
    public static void callExampleMessageMethodAndAssertClassNotFoundJXPathException() {
        final JXPathContext context = JXPathContext.newContext(new Object());
        assertThrows(
                JXPathException.class,
                () -> context.selectSingleNode(EXAMPLE_CLASS_FQCN + ".getMessage()"),
                "Expected dynamic class loading to fail for " + EXAMPLE_CLASS_FQCN
        );
    }

    /**
     * Creates a fresh JXPathContext and asserts the target class is dynamically loaded successfully.
     * This method is invoked directly or reflectively depending on the test.
     */
    public static void callExampleMessageMethodAndAssertSuccess() {
        final JXPathContext context = JXPathContext.newContext(new Object());
        assertEquals("an example class", context.selectSingleNode(EXAMPLE_CLASS_FQCN + ".getMessage()"));
    }

    @BeforeEach
    void setUp() {
        originalContextClassLoader = Thread.currentThread().getContextClassLoader();
    }

    @AfterEach
    void tearDown() {
        Thread.currentThread().setContextClassLoader(originalContextClassLoader);
    }

    /**
     * Helper that loads this test class via the given class loader and invokes a static, no-arg method by name.
     */
    private void invokeSelfUnderLoader(final ClassLoader cl, final String methodName) throws ReflectiveOperationException {
        final Class<?> testClassUnderCl = cl.loadClass(TEST_CLASS_FQCN);
        final Method method = testClassUnderCl.getMethod(methodName, new Class<?>[0]);
        try {
            method.invoke(null, new Object[0]);
        } catch (final InvocationTargetException e) {
            // Unwrap and rethrow runtime exceptions to preserve assertion failures.
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw e;
        }
    }

    @Test
    @DisplayName("When TCCL is null and JXPath's loader can't see the class, dynamic loading fails")
    void testClassLoadFailWithoutContextClassLoader() throws ReflectiveOperationException {
        Thread.currentThread().setContextClassLoader(null);

        // Run the assertion method from a copy of this class loaded by a loader
        // that cannot see the example class at all.
        final ClassLoader cl = new SelectiveDelegatingClassLoader(getClass().getClassLoader());
        invokeSelfUnderLoader(cl, M_ASSERT_FAIL);
    }

    @Test
    @DisplayName("When TCCL is set and can load the class, dynamic loading succeeds")
    void testClassLoadSuccessWithContextClassLoader() throws ReflectiveOperationException {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());

        // Run the assertion method from a copy of this class; since the TCCL can
        // load the example class, it should succeed.
        final ClassLoader cl = new SelectiveDelegatingClassLoader(getClass().getClassLoader());
        invokeSelfUnderLoader(cl, M_ASSERT_SUCCESS);
    }

    @Test
    @DisplayName("When TCCL is null but JXPath's own loader can see the class, loading succeeds")
    void testClassLoadSuccessWithoutContextClassLoader() {
        Thread.currentThread().setContextClassLoader(null);
        callExampleMessageMethodAndAssertSuccess();
    }

    @Test
    @DisplayName("When TCCL cannot load the class, JXPath falls back to its own loader and succeeds")
    void testCurrentClassLoaderFallback() {
        // Make the TCCL unable to load the example class
        final ClassLoader hidingCl = new SelectiveDelegatingClassLoader(getClass().getClassLoader());
        Thread.currentThread().setContextClassLoader(hidingCl);

        // Even though the TCCL cannot load the example class, JXPath should
        // fall back to its own loader and succeed.
        callExampleMessageMethodAndAssertSuccess();
    }
}