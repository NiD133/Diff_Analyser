/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3.reflect;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.AbstractLangTest;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.mutable.MutableObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for ConstructorUtils utility class.
 * 
 * This test suite verifies the functionality of constructor reflection utilities including:
 * - Finding accessible constructors
 * - Matching constructors with compatible parameter types
 * - Invoking constructors with type conversion
 * - Exact constructor matching and invocation
 */
public class ConstructorUtilsTest extends AbstractLangTest {

    // Test helper classes for constructor testing scenarios
    private static class BaseClass {
    }

    private static final class SubClass extends BaseClass {
    }

    static class PrivateClass {
        @SuppressWarnings("unused")
        public static class PublicInnerClass {
            public PublicInnerClass() {
            }
        }

        @SuppressWarnings("unused")
        public PrivateClass() {
        }
    }

    /**
     * Test bean with multiple constructor overloads to test parameter matching.
     * Each constructor sets a toString value to identify which constructor was called.
     */
    public static class TestBean {
        private final String constructorSignature;
        final String[] varArgs;

        public TestBean() {
            constructorSignature = "()";
            varArgs = null;
        }

        public TestBean(final String s) {
            constructorSignature = "(String)";
            varArgs = null;
        }

        public TestBean(final Object o) {
            constructorSignature = "(Object)";
            varArgs = null;
        }

        public TestBean(final int i) {
            constructorSignature = "(int)";
            varArgs = null;
        }

        public TestBean(final Integer i) {
            constructorSignature = "(Integer)";
            varArgs = null;
        }

        public TestBean(final double d) {
            constructorSignature = "(double)";
            varArgs = null;
        }

        public TestBean(final String... s) {
            constructorSignature = "(String...)";
            varArgs = s;
        }

        public TestBean(final Integer i, final String... s) {
            constructorSignature = "(Integer, String...)";
            varArgs = s;
        }

        public TestBean(final Integer first, final int... args) {
            constructorSignature = "(Integer, String...)";
            varArgs = new String[args.length];
            for (int i = 0; i < args.length; ++i) {
                varArgs[i] = Integer.toString(args[i]);
            }
        }

        public TestBean(final BaseClass bc, final String... s) {
            constructorSignature = "(BaseClass, String...)";
            varArgs = s;
        }

        @Override
        public String toString() {
            return constructorSignature;
        }

        /**
         * Verifies that the correct constructor was called with expected parameters.
         */
        void verify(final String expectedSignature, final String[] expectedArgs) {
            assertEquals(expectedSignature, constructorSignature);
            assertArrayEquals(expectedArgs, varArgs);
        }
    }

    // Cache for parameter type arrays to avoid repeated array creation
    private final Map<Class<?>, Class<?>[]> parameterTypeCache;

    public ConstructorUtilsTest() {
        parameterTypeCache = new HashMap<>();
    }

    @BeforeEach
    public void setUp() {
        parameterTypeCache.clear();
    }

    // Helper methods

    /**
     * Creates a singleton array containing the specified class, using cache for efficiency.
     */
    private Class<?>[] createSingleParameterTypeArray(final Class<?> parameterType) {
        Class<?>[] result = parameterTypeCache.get(parameterType);
        if (result == null) {
            result = new Class[] { parameterType };
            parameterTypeCache.put(parameterType, result);
        }
        return result;
    }

    /**
     * Helper method to verify that getMatchingAccessibleConstructor returns a constructor
     * with the expected parameter types.
     */
    private void verifyMatchingConstructorParameterTypes(final Class<?> targetClass, 
            final Class<?>[] requestedTypes, final Class<?>[] expectedTypes) {
        final Constructor<?> constructor = ConstructorUtils.getMatchingAccessibleConstructor(targetClass, requestedTypes);
        assertArrayEquals(expectedTypes, constructor.getParameterTypes(), 
            "Expected parameter types " + Arrays.toString(expectedTypes) + 
            " but got " + Arrays.toString(constructor.getParameterTypes()));
    }

    private String formatClassArray(final Class<?>[] classes) {
        return Arrays.asList(classes).toString();
    }

    // Test cases

    @Test
    void testConstructorUtilsCanBeInstantiated() throws Exception {
        // Verify that ConstructorUtils can be instantiated (though it's a utility class)
        assertNotNull(MethodUtils.class.getConstructor().newInstance());
    }

    @Test
    void testGetAccessibleConstructor_WithConstructorObject() throws Exception {
        // Test getting accessible constructor from a Constructor object
        Constructor<Object> objectConstructor = Object.class.getConstructor(ArrayUtils.EMPTY_CLASS_ARRAY);
        assertNotNull(ConstructorUtils.getAccessibleConstructor(objectConstructor));
        
        // Private class constructor should not be accessible
        Constructor<PrivateClass> privateConstructor = PrivateClass.class.getConstructor(ArrayUtils.EMPTY_CLASS_ARRAY);
        assertNull(ConstructorUtils.getAccessibleConstructor(privateConstructor));
        
        // Public inner class of private class should not be accessible
        assertNull(ConstructorUtils.getAccessibleConstructor(PrivateClass.PublicInnerClass.class));
    }

    @Test
    void testGetAccessibleConstructor_WithClassAndParameterTypes() {
        // Test getting accessible constructor by class and parameter types
        assertNotNull(ConstructorUtils.getAccessibleConstructor(Object.class, ArrayUtils.EMPTY_CLASS_ARRAY));
        
        // Private class constructor should not be accessible
        assertNull(ConstructorUtils.getAccessibleConstructor(PrivateClass.class, ArrayUtils.EMPTY_CLASS_ARRAY));
    }

    @Test
    void testGetMatchingAccessibleConstructor_BasicTypes() {
        // Test no-argument constructor
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            ArrayUtils.EMPTY_CLASS_ARRAY, ArrayUtils.EMPTY_CLASS_ARRAY);
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            null, ArrayUtils.EMPTY_CLASS_ARRAY);

        // Test exact type matches
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            createSingleParameterTypeArray(String.class), createSingleParameterTypeArray(String.class));
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            createSingleParameterTypeArray(Object.class), createSingleParameterTypeArray(Object.class));
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            createSingleParameterTypeArray(Integer.class), createSingleParameterTypeArray(Integer.class));
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            createSingleParameterTypeArray(Integer.TYPE), createSingleParameterTypeArray(Integer.TYPE));
    }

    @Test
    void testGetMatchingAccessibleConstructor_TypeConversions() {
        // Test type conversions - wrapper types that don't have exact matches should fall back to Object
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            createSingleParameterTypeArray(Boolean.class), createSingleParameterTypeArray(Object.class));

        // Test numeric type promotions to int
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            createSingleParameterTypeArray(Byte.class), createSingleParameterTypeArray(Integer.TYPE));
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            createSingleParameterTypeArray(Byte.TYPE), createSingleParameterTypeArray(Integer.TYPE));
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            createSingleParameterTypeArray(Short.class), createSingleParameterTypeArray(Integer.TYPE));
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            createSingleParameterTypeArray(Short.TYPE), createSingleParameterTypeArray(Integer.TYPE));
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            createSingleParameterTypeArray(Character.class), createSingleParameterTypeArray(Integer.TYPE));
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            createSingleParameterTypeArray(Character.TYPE), createSingleParameterTypeArray(Integer.TYPE));

        // Test numeric type promotions to double
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            createSingleParameterTypeArray(Long.class), createSingleParameterTypeArray(Double.TYPE));
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            createSingleParameterTypeArray(Long.TYPE), createSingleParameterTypeArray(Double.TYPE));
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            createSingleParameterTypeArray(Float.class), createSingleParameterTypeArray(Double.TYPE));
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            createSingleParameterTypeArray(Float.TYPE), createSingleParameterTypeArray(Double.TYPE));
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            createSingleParameterTypeArray(Double.class), createSingleParameterTypeArray(Double.TYPE));
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            createSingleParameterTypeArray(Double.TYPE), createSingleParameterTypeArray(Double.TYPE));
    }

    @Test
    void testGetMatchingAccessibleConstructor_InheritanceAndVarArgs() {
        // Test inheritance - SubClass should match BaseClass parameter
        verifyMatchingConstructorParameterTypes(TestBean.class, 
            new Class<?>[] { SubClass.class, String[].class },
            new Class<?>[] { BaseClass.class, String[].class });
    }

    @Test
    void testGetMatchingAccessibleConstructor_NullArgument() {
        // Test null argument matching
        verifyMatchingConstructorParameterTypes(MutableObject.class, 
            createSingleParameterTypeArray(null), createSingleParameterTypeArray(Object.class));
    }

    @Test
    void testInvokeConstructor_NoArguments() throws Exception {
        // Test no-argument constructor invocation
        assertEquals("()", ConstructorUtils.invokeConstructor(TestBean.class, (Object[]) ArrayUtils.EMPTY_CLASS_ARRAY).toString());
        assertEquals("()", ConstructorUtils.invokeConstructor(TestBean.class, (Object[]) null).toString());
        assertEquals("()", ConstructorUtils.invokeConstructor(TestBean.class).toString());
    }

    @Test
    void testInvokeConstructor_SingleArguments() throws Exception {
        // Test single argument constructors with various types
        assertEquals("(String)", ConstructorUtils.invokeConstructor(TestBean.class, "").toString());
        assertEquals("(Object)", ConstructorUtils.invokeConstructor(TestBean.class, new Object()).toString());
        assertEquals("(Object)", ConstructorUtils.invokeConstructor(TestBean.class, Boolean.TRUE).toString());
        assertEquals("(Integer)", ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.INTEGER_ONE).toString());
        
        // Test numeric type conversions
        assertEquals("(int)", ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.BYTE_ONE).toString());
        assertEquals("(double)", ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.LONG_ONE).toString());
        assertEquals("(double)", ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.DOUBLE_ONE).toString());
    }

    @Test
    void testInvokeConstructor_VarArgs() throws Exception {
        // Test variable arguments constructors
        ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.INTEGER_ONE)
            .verify("(Integer)", null);
        
        ConstructorUtils.invokeConstructor(TestBean.class, "a", "b")
            .verify("(String...)", new String[] { "a", "b" });
        
        ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.INTEGER_ONE, "a", "b")
            .verify("(Integer, String...)", new String[] { "a", "b" });
        
        ConstructorUtils.invokeConstructor(TestBean.class, new SubClass(), new String[] { "a", "b" })
            .verify("(BaseClass, String...)", new String[] { "a", "b" });
    }

    @Test
    void testInvokeConstructor_VarArgsUnboxing() throws Exception {
        // Test that varargs with Integer values are properly handled
        final TestBean testBean = ConstructorUtils.invokeConstructor(TestBean.class, 
            Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));

        // First Integer becomes the fixed parameter, remaining become varargs
        assertArrayEquals(new String[] { "2", "3" }, testBean.varArgs);
    }

    @Test
    void testInvokeExactConstructor_ValidCases() throws Exception {
        // Test exact constructor matching (no type conversion)
        assertEquals("()", ConstructorUtils.invokeExactConstructor(TestBean.class, (Object[]) ArrayUtils.EMPTY_CLASS_ARRAY).toString());
        assertEquals("()", ConstructorUtils.invokeExactConstructor(TestBean.class, (Object[]) null).toString());
        assertEquals("(String)", ConstructorUtils.invokeExactConstructor(TestBean.class, "").toString());
        assertEquals("(Object)", ConstructorUtils.invokeExactConstructor(TestBean.class, new Object()).toString());
        assertEquals("(Integer)", ConstructorUtils.invokeExactConstructor(TestBean.class, NumberUtils.INTEGER_ONE).toString());
        
        // Test exact constructor with explicit parameter types
        assertEquals("(double)",
                ConstructorUtils.invokeExactConstructor(TestBean.class, 
                    new Object[] { NumberUtils.DOUBLE_ONE }, 
                    new Class[] { Double.TYPE }).toString());
    }

    @Test
    void testInvokeExactConstructor_NoTypeConversion() {
        // Test that exact constructor matching fails when type conversion would be needed
        assertThrows(NoSuchMethodException.class, 
            () -> ConstructorUtils.invokeExactConstructor(TestBean.class, NumberUtils.BYTE_ONE),
            "Should not find constructor for Byte when only int constructor exists");
        
        assertThrows(NoSuchMethodException.class, 
            () -> ConstructorUtils.invokeExactConstructor(TestBean.class, NumberUtils.LONG_ONE),
            "Should not find constructor for Long when only double constructor exists");
        
        assertThrows(NoSuchMethodException.class, 
            () -> ConstructorUtils.invokeExactConstructor(TestBean.class, Boolean.TRUE),
            "Should not find constructor for Boolean when only Object constructor exists");
    }
}