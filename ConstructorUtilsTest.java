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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ConstructorUtils}.
 */
public class ConstructorUtilsTest extends AbstractLangTest {
    private static class BaseClass {
    }

    static class PrivateClass {
        public static class PublicInnerClass {
            public PublicInnerClass() {
            }
        }

        public PrivateClass() {
        }
    }

    private static final class SubClass extends BaseClass {
    }

    public static class TestBean {
        private final String toString;
        final String[] varArgs;

        public TestBean() {
            toString = "()";
            varArgs = null;
        }

        public TestBean(BaseClass bc, String... s) {
            toString = "(BaseClass, String...)";
            varArgs = s;
        }

        public TestBean(double d) {
            toString = "(double)";
            varArgs = null;
        }

        public TestBean(int i) {
            toString = "(int)";
            varArgs = null;
        }

        public TestBean(Integer i) {
            toString = "(Integer)";
            varArgs = null;
        }

        public TestBean(Integer first, int... args) {
            toString = "(Integer, int...)";
            varArgs = new String[args.length];
            for (int i = 0; i < args.length; ++i) {
                varArgs[i] = Integer.toString(args[i]);
            }
        }

        public TestBean(Integer i, String... s) {
            toString = "(Integer, String...)";
            varArgs = s;
        }

        public TestBean(Object o) {
            toString = "(Object)";
            varArgs = null;
        }

        public TestBean(String s) {
            toString = "(String)";
            varArgs = null;
        }

        public TestBean(String... s) {
            toString = "(String...)";
            varArgs = s;
        }

        @Override
        public String toString() {
            return toString;
        }

        void verify(String expectedToString, String[] expectedVarArgs) {
            assertEquals(expectedToString, toString, "toString mismatch");
            assertArrayEquals(expectedVarArgs, varArgs, "varArgs content mismatch");
        }
    }

    private final Map<Class<?>, Class<?>[]> classCache = new HashMap<>();

    @BeforeEach
    public void setUp() {
        classCache.clear();
    }

    /**
     * Helper method to create singleton array with caching.
     */
    private Class<?>[] singletonArray(Class<?> c) {
        return classCache.computeIfAbsent(c, k -> new Class<?>[]{k});
    }

    /**
     * Helper to verify matching accessible constructor parameter types.
     */
    private void assertMatchingConstructorParameters(Class<?> cls, Class<?>[] requestedTypes, Class<?>[] expectedTypes) {
        final Constructor<?> constructor = ConstructorUtils.getMatchingAccessibleConstructor(cls, requestedTypes);
        assertNotNull(constructor, "No matching accessible constructor found");
        assertArrayEquals(expectedTypes, constructor.getParameterTypes(),
            () -> String.format("Expected parameter types %s but found %s",
                Arrays.toString(expectedTypes), Arrays.toString(constructor.getParameterTypes())));
    }

    @Test
    void testConstructorUtilsInstantiation() throws Exception {
        assertNotNull(ConstructorUtils.class.getConstructor().newInstance());
    }

    @Nested
    class GetAccessibleConstructorTests {
        @Test
        void whenPublicConstructorExists_shouldReturnConstructor() throws Exception {
            Constructor<?> constructor = Object.class.getConstructor();
            assertNotNull(ConstructorUtils.getAccessibleConstructor(constructor));
        }

        @Test
        void whenPrivateConstructor_shouldReturnNull() throws Exception {
            Constructor<?> constructor = PrivateClass.class.getConstructor();
            assertNull(ConstructorUtils.getAccessibleConstructor(constructor));
        }

        @Test
        void whenInnerClassConstructor_shouldReturnNull() {
            assertNull(ConstructorUtils.getAccessibleConstructor(PrivateClass.PublicInnerClass.class));
        }

        @Test
        void whenPublicConstructorExists_shouldReturnConstructorFromDescription() {
            assertNotNull(ConstructorUtils.getAccessibleConstructor(Object.class, ArrayUtils.EMPTY_CLASS_ARRAY));
        }

        @Test
        void whenPrivateConstructor_shouldReturnNullFromDescription() {
            assertNull(ConstructorUtils.getAccessibleConstructor(PrivateClass.class, ArrayUtils.EMPTY_CLASS_ARRAY));
        }
    }

    @Nested
    class GetMatchingAccessibleConstructorTests {
        @Test
        void shouldMatchEmptyConstructor() {
            assertMatchingConstructorParameters(TestBean.class, ArrayUtils.EMPTY_CLASS_ARRAY, ArrayUtils.EMPTY_CLASS_ARRAY);
        }

        @Test
        void shouldTreatNullAsEmptyConstructor() {
            assertMatchingConstructorParameters(TestBean.class, null, ArrayUtils.EMPTY_CLASS_ARRAY);
        }

        @Test
        void shouldMatchExactParameterType() {
            assertMatchingConstructorParameters(TestBean.class, singletonArray(String.class), singletonArray(String.class));
        }

        @Test
        void shouldMatchSuperclassParameter() {
            assertMatchingConstructorParameters(TestBean.class, new Class<?>[]{SubClass.class, String[].class},
                new Class<?>[]{BaseClass.class, String[].class});
        }

        @Test
        void shouldMatchObjectParameter() {
            assertMatchingConstructorParameters(TestBean.class, singletonArray(Object.class), singletonArray(Object.class));
        }

        @Test
        void shouldMatchBoxedTypesToPrimitive() {
            // Should match (int) constructor
            assertMatchingConstructorParameters(TestBean.class, singletonArray(Byte.class), singletonArray(int.class));
            assertMatchingConstructorParameters(TestBean.class, singletonArray(Short.class), singletonArray(int.class));
            assertMatchingConstructorParameters(TestBean.class, singletonArray(Character.class), singletonArray(int.class));
            
            // Should match (double) constructor
            assertMatchingConstructorParameters(TestBean.class, singletonArray(Long.class), singletonArray(double.class));
            assertMatchingConstructorParameters(TestBean.class, singletonArray(Float.class), singletonArray(double.class));
        }

        @Test
        void shouldMatchPrimitiveTypes() {
            assertMatchingConstructorParameters(TestBean.class, singletonArray(byte.class), singletonArray(int.class));
            assertMatchingConstructorParameters(TestBean.class, singletonArray(short.class), singletonArray(int.class));
            assertMatchingConstructorParameters(TestBean.class, singletonArray(char.class), singletonArray(int.class));
            assertMatchingConstructorParameters(TestBean.class, singletonArray(int.class), singletonArray(int.class));
            assertMatchingConstructorParameters(TestBean.class, singletonArray(long.class), singletonArray(double.class));
            assertMatchingConstructorParameters(TestBean.class, singletonArray(float.class), singletonArray(double.class));
            assertMatchingConstructorParameters(TestBean.class, singletonArray(double.class), singletonArray(double.class));
        }

        @Test
        void shouldMatchBoxedTypes() {
            assertMatchingConstructorParameters(TestBean.class, singletonArray(Integer.class), singletonArray(Integer.class));
        }

        @Test
        void shouldHandleNullArgument() {
            assertMatchingConstructorParameters(MutableObject.class, new Class<?>[]{null}, singletonArray(Object.class));
        }
    }

    @Nested
    class InvokeConstructorTests {
        @Test
        void shouldInvokeEmptyConstructor() throws Exception {
            assertEquals("()", ConstructorUtils.invokeConstructor(TestBean.class).toString());
            assertEquals("()", ConstructorUtils.invokeConstructor(TestBean.class, (Object[]) null).toString());
            assertEquals("()", ConstructorUtils.invokeConstructor(TestBean.class, ArrayUtils.EMPTY_OBJECT_ARRAY).toString());
        }

        @Test
        void shouldInvokeStringConstructor() throws Exception {
            assertEquals("(String)", ConstructorUtils.invokeConstructor(TestBean.class, "").toString());
        }

        @Test
        void shouldInvokeObjectConstructor() throws Exception {
            assertEquals("(Object)", ConstructorUtils.invokeConstructor(TestBean.class, new Object()).toString());
            assertEquals("(Object)", ConstructorUtils.invokeConstructor(TestBean.class, Boolean.TRUE).toString());
        }

        @Test
        void shouldInvokeIntegerConstructor() throws Exception {
            TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.INTEGER_ONE);
            bean.verify("(Integer)", null);
        }

        @Test
        void shouldInvokePrimitiveConstructor() throws Exception {
            assertEquals("(int)", ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.BYTE_ONE).toString());
            assertEquals("(double)", ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.LONG_ONE).toString());
            assertEquals("(double)", ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.DOUBLE_ONE).toString());
        }

        @Test
        void shouldInvokeVarargsConstructor() throws Exception {
            TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class, "a", "b");
            bean.verify("(String...)", new String[]{"a", "b"});
        }

        @Test
        void shouldInvokeMixedParametersConstructor() throws Exception {
            TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.INTEGER_ONE, "a", "b");
            bean.verify("(Integer, String...)", new String[]{"a", "b"});
        }

        @Test
        void shouldInvokeSubclassParameterConstructor() throws Exception {
            TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class, new SubClass(), "a", "b");
            bean.verify("(BaseClass, String...)", new String[]{"a", "b"});
        }

        @Test
        void shouldUnboxVarargs() throws Exception {
            TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class, 
                Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
            assertArrayEquals(new String[]{"2", "3"}, bean.varArgs);
        }
    }

    @Nested
    class InvokeExactConstructorTests {
        @Test
        void shouldInvokeExactEmptyConstructor() throws Exception {
            assertEquals("()", ConstructorUtils.invokeExactConstructor(TestBean.class, ArrayUtils.EMPTY_OBJECT_ARRAY).toString());
            assertEquals("()", ConstructorUtils.invokeExactConstructor(TestBean.class, (Object[]) null).toString());
        }

        @Test
        void shouldInvokeExactStringConstructor() throws Exception {
            assertEquals("(String)", ConstructorUtils.invokeExactConstructor(TestBean.class, "").toString());
        }

        @Test
        void shouldInvokeExactObjectConstructor() throws Exception {
            assertEquals("(Object)", ConstructorUtils.invokeExactConstructor(TestBean.class, new Object()).toString());
        }

        @Test
        void shouldInvokeExactIntegerConstructor() throws Exception {
            assertEquals("(Integer)", ConstructorUtils.invokeExactConstructor(TestBean.class, NumberUtils.INTEGER_ONE).toString());
        }

        @Test
        void shouldInvokeExactPrimitiveConstructor() throws Exception {
            assertEquals("(double)", ConstructorUtils.invokeExactConstructor(TestBean.class, 
                new Object[]{NumberUtils.DOUBLE_ONE}, new Class[]{Double.TYPE}).toString());
        }

        @Test
        void shouldFailOnInexactMatch() {
            assertThrows(NoSuchMethodException.class, 
                () -> ConstructorUtils.invokeExactConstructor(TestBean.class, NumberUtils.BYTE_ONE));
            
            assertThrows(NoSuchMethodException.class, 
                () -> ConstructorUtils.invokeExactConstructor(TestBean.class, NumberUtils.LONG_ONE));
            
            assertThrows(NoSuchMethodException.class, 
                () -> ConstructorUtils.invokeExactConstructor(TestBean.class, Boolean.TRUE));
        }
    }
}