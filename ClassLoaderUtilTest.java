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
 * Tests ClassLoaderUtil's ability to load classes using different class loader strategies.
 * 
 * This test verifies that JXPath can dynamically load classes by:
 * 1. Using the context class loader when available
 * 2. Falling back to the current class loader when context class loader fails
 * 3. Properly handling ClassNotFoundException when no loader can find the class
 */
public class ClassLoaderUtilTest {

    // Class names as string literals to avoid loading issues during testing
    private static final String TEST_CASE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoaderUtilTest";
    private static final String EXAMPLE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoadingExampleClass";
    private static final String EXPECTED_MESSAGE = "an example class";

    private ClassLoader originalContextClassLoader;

    /**
     * Custom class loader that simulates different class loading scenarios:
     * - Blocks loading of EXAMPLE_CLASS_NAME (simulates class not found)
     * - Loads TEST_CASE_CLASS_NAME in isolation (simulates different class loader context)
     * - Delegates all other class loading to parent
     */
    private static final class TestClassLoader extends ClassLoader {

        private Class<?> isolatedTestCaseClass = null;

        public TestClassLoader(final ClassLoader parentClassLoader) {
            super(parentClassLoader);
        }

        @Override
        public synchronized Class<?> loadClass(final String className, final boolean resolved) throws ClassNotFoundException {
            if (EXAMPLE_CLASS_NAME.equals(className)) {
                // Simulate class not found in this class loader
                throw new ClassNotFoundException("Intentionally blocked: " + className);
            }
            
            if (TEST_CASE_CLASS_NAME.equals(className)) {
                return loadTestCaseClassInIsolation(className);
            }
            
            return getParent().loadClass(className);
        }

        private Class<?> loadTestCaseClassInIsolation(final String className) throws ClassNotFoundException {
            if (isolatedTestCaseClass == null) {
                final URL classUrl = getParent().getResource("org/apache/commons/jxpath/util/ClassLoaderUtilTest.class");
                byte[] classBytes = readClassBytes(classUrl);
                this.isolatedTestCaseClass = this.defineClass(className, classBytes, 0, classBytes.length);
            }
            return this.isolatedTestCaseClass;
        }

        private byte[] readClassBytes(final URL classUrl) throws ClassNotFoundException {
            try {
                return IOUtils.toByteArray(classUrl);
            } catch (final IOException e) {
                throw new ClassNotFoundException("Failed to read class bytes from: " + classUrl, e);
            }
        }
    }

    @BeforeEach
    void saveOriginalContextClassLoader() {
        this.originalContextClassLoader = Thread.currentThread().getContextClassLoader();
    }

    @AfterEach
    void restoreOriginalContextClassLoader() {
        Thread.currentThread().setContextClassLoader(this.originalContextClassLoader);
    }

    @Test
    void shouldLoadClassSuccessfully_WhenClassIsVisibleToCurrentClassLoader() {
        // Given: No context class loader is set
        Thread.currentThread().setContextClassLoader(null);
        
        // When & Then: JXPath should successfully load and call the example class
        assertExampleClassCanBeLoadedAndCalled();
    }

    @Test
    void shouldLoadClassSuccessfully_WhenContextClassLoaderCanLoadClass() throws ReflectiveOperationException {
        // Given: Context class loader is set to one that can load the example class
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        
        // And: We're running under a test class loader that blocks the example class
        final ClassLoader blockingClassLoader = new TestClassLoader(getClass().getClassLoader());
        
        // When & Then: JXPath should use context class loader to load the class successfully
        executeTestMethodInClassLoader(blockingClassLoader, "assertExampleClassCanBeLoadedAndCalled");
    }

    @Test
    void shouldFallBackToCurrentClassLoader_WhenContextClassLoaderCannotLoadClass() {
        // Given: Context class loader is set to one that blocks the example class
        final ClassLoader blockingClassLoader = new TestClassLoader(getClass().getClassLoader());
        Thread.currentThread().setContextClassLoader(blockingClassLoader);
        
        // When & Then: JXPath should fall back to current class loader and succeed
        assertExampleClassCanBeLoadedAndCalled();
    }

    @Test
    void shouldThrowException_WhenNoClassLoaderCanLoadClass() throws ReflectiveOperationException {
        // Given: No context class loader is available
        Thread.currentThread().setContextClassLoader(null);
        
        // And: We're running under a class loader that blocks the example class
        final ClassLoader blockingClassLoader = new TestClassLoader(getClass().getClassLoader());
        
        // When & Then: JXPath should throw ClassNotFoundException wrapped in JXPathException
        executeTestMethodInClassLoader(blockingClassLoader, "assertExampleClassCannotBeLoaded");
    }

    /**
     * Helper method that attempts to load and call the example class via JXPath.
     * This method is called via reflection from different class loader contexts.
     */
    public static void assertExampleClassCanBeLoadedAndCalled() {
        final JXPathContext context = JXPathContext.newContext(new Object());
        final String actualMessage = (String) context.selectSingleNode(EXAMPLE_CLASS_NAME + ".getMessage()");
        assertEquals(EXPECTED_MESSAGE, actualMessage, 
            "Example class should be loaded and its getMessage() method should return expected value");
    }

    /**
     * Helper method that verifies the example class cannot be loaded via JXPath.
     * This method is called via reflection from different class loader contexts.
     */
    public static void assertExampleClassCannotBeLoaded() {
        final JXPathContext context = JXPathContext.newContext(new Object());
        
        JXPathException exception = assertThrows(JXPathException.class, 
            () -> context.selectSingleNode(EXAMPLE_CLASS_NAME + ".getMessage()"),
            "JXPath should throw exception when class cannot be loaded");
            
        // Additional verification could check exception message contains class name
    }

    /**
     * Loads this test class using the specified class loader and invokes the named static method.
     * This simulates running JXPath under different class loader contexts.
     */
    private void executeTestMethodInClassLoader(final ClassLoader classLoader, final String methodName) 
            throws ReflectiveOperationException {
        
        final Class<?> testClass = classLoader.loadClass(TEST_CASE_CLASS_NAME);
        final Method testMethod = testClass.getMethod(methodName, ArrayUtils.EMPTY_CLASS_ARRAY);
        
        try {
            testMethod.invoke(null, (Object[]) null);
        } catch (final InvocationTargetException e) {
            // Re-throw the underlying exception to preserve test failure information
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw new RuntimeException("Unexpected exception during test method execution", e.getCause());
        }
    }
}