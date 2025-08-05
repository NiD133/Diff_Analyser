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
 * Tests the class loading behavior of {@link ClassLoaderUtil}.
 */
class ClassLoaderUtilTest {

    /**
     * Custom class loader that controls class loading behavior:
     * 1. Always throws ClassNotFoundException for EXAMPLE_CLASS_NAME
     * 2. Loads TEST_CASE_CLASS_NAME through bytecode definition
     * 3. Delegates all other classes to parent loader
     */
    private static final class ControlledClassLoader extends ClassLoader {
        private Class<?> testClass;

        ControlledClassLoader(ClassLoader parent) {
            super(parent);
        }

        @Override
        public synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            if (EXAMPLE_CLASS_NAME.equals(name)) {
                throw new ClassNotFoundException("Blocked: " + name);
            }
            
            if (TEST_CASE_CLASS_NAME.equals(name)) {
                return loadAndDefineTestClass();
            }
            
            return super.loadClass(name, resolve);
        }

        private Class<?> loadAndDefineTestClass() throws ClassNotFoundException {
            if (testClass == null) {
                URL classResource = getResource(TEST_CASE_CLASS_PATH);
                try {
                    byte[] bytes = IOUtils.toByteArray(classResource);
                    testClass = defineClass(TEST_CASE_CLASS_NAME, bytes, 0, bytes.length);
                } catch (IOException e) {
                    throw new ClassNotFoundException("Failed loading: " + TEST_CASE_CLASS_NAME, e);
                }
            }
            return testClass;
        }
    }

    // Class name constants (must be string literals for controlled loading)
    private static final String TEST_CASE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoaderUtilTest";
    private static final String TEST_CASE_CLASS_PATH = "org/apache/commons/jxpath/util/ClassLoaderUtilTest.class";
    private static final String EXAMPLE_CLASS_NAME = "org.apache.commons.jxpath.util.ClassLoadingExampleClass";

    /**
     * Verifies JXPath fails to load a class when context classloader is absent.
     */
    static void verifyClassLoadingFailure() {
        JXPathContext context = JXPathContext.newContext(new Object());
        assertThrows(JXPathException.class,
            () -> context.selectSingleNode(EXAMPLE_CLASS_NAME + ".getMessage()"),
            "Should fail to load blocked class: " + EXAMPLE_CLASS_NAME
        );
    }

    /**
     * Verifies JXPath successfully loads a class through proper classloader.
     */
    static void verifyClassLoadingSuccess() {
        JXPathContext context = JXPathContext.newContext(new Object());
        assertEquals("an example class", 
            context.selectSingleNode(EXAMPLE_CLASS_NAME + ".getMessage()"));
    }

    private ClassLoader originalContextClassLoader;

    @BeforeEach
    void backupContextClassLoader() {
        originalContextClassLoader = Thread.currentThread().getContextClassLoader();
    }

    @AfterEach
    void restoreContextClassLoader() {
        Thread.currentThread().setContextClassLoader(originalContextClassLoader);
    }

    /**
     * Invokes a static method in the specified class loader environment.
     */
    private void invokeStaticMethodInClassLoader(ClassLoader loader, String methodName) 
            throws ReflectiveOperationException {
        Class<?> testClass = loader.loadClass(TEST_CASE_CLASS_NAME);
        Method method = testClass.getMethod(methodName, ArrayUtils.EMPTY_CLASS_ARRAY);
        try {
            method.invoke(null);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            // Propagate test failures properly
            if (cause instanceof RuntimeException) throw (RuntimeException) cause;
            if (cause instanceof Error) throw (Error) cause;
            throw new ReflectiveOperationException("Unexpected checked exception", cause);
        }
    }

    /**
     * Tests that JXPath fails to load classes when context class loader is null.
     */
    @Test
    void classLoadingFailsWhenNoContextClassLoader() throws Exception {
        Thread.currentThread().setContextClassLoader(null);
        ControlledClassLoader loader = new ControlledClassLoader(getClass().getClassLoader());
        invokeStaticMethodInClassLoader(loader, "verifyClassLoadingFailure");
    }

    /**
     * Tests that JXPath succeeds when context class loader can load the class.
     */
    @Test
    void classLoadingSucceedsWithValidContextClassLoader() throws Exception {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        ControlledClassLoader loader = new ControlledClassLoader(getClass().getClassLoader());
        invokeStaticMethodInClassLoader(loader, "verifyClassLoadingSuccess");
    }

    /**
     * Tests that JXPath succeeds using current class loader when context loader is absent.
     */
    @Test
    void classLoadingSucceedsWithoutContextClassLoader() {
        Thread.currentThread().setContextClassLoader(null);
        verifyClassLoadingSuccess();
    }

    /**
     * Tests that JXPath falls back to current loader when context loader fails.
     */
    @Test
    void classLoadingUsesCurrentLoaderWhenContextLoaderFails() {
        Thread.currentThread().setContextClassLoader(
            new ControlledClassLoader(getClass().getClassLoader()));
        verifyClassLoadingSuccess();
    }
}