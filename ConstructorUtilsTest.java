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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link ConstructorUtils}.
 */
public class ConstructorUtilsTest {

    // --- Test Fixtures ---

    private static class BaseClass {
    }

    private static final class SubClass extends BaseClass {
    }

    static class PrivateClass {
        public PrivateClass() {
        }

        // Inner class is public, but its enclosing class is not.
        public static class PublicInnerClass {
            public PublicInnerClass() {
            }
        }
    }

    public static class TestBean {
        final String constructorType;
        final String[] varArgs;

        public TestBean() {
            this.constructorType = "()";
            this.varArgs = null;
        }

        public TestBean(final int i) {
            this.constructorType = "(int)";
            this.varArgs = null;
        }

        public TestBean(final Integer i) {
            this.constructorType = "(Integer)";
            this.varArgs = null;
        }

        public TestBean(final double d) {
            this.constructorType = "(double)";
            this.varArgs = null;
        }

        public TestBean(final String s) {
            this.constructorType = "(String)";
            this.varArgs = null;
        }

        public TestBean(final Object o) {
            this.constructorType = "(Object)";
            this.varArgs = null;
        }

        public TestBean(final String... s) {
            this.constructorType = "(String...)";
            this.varArgs = s;
        }

        public TestBean(final Integer i, final String... s) {
            this.constructorType = "(Integer, String...)";
            this.varArgs = s;
        }

        public TestBean(final Integer first, final int... args) {
            this.constructorType = "(Integer, int...)";
            this.varArgs = new String[args.length];
            for (int i = 0; i < args.length; ++i) {
                varArgs[i] = Integer.toString(args[i]);
            }
        }

        public TestBean(final BaseClass bc, final String... s) {
            this.constructorType = "(BaseClass, String...)";
            this.varArgs = s;
        }
    }

    @Test
    void testConstructorIsPublic() {
        // The constructor for the utility class is public.
        assertNotNull(new ConstructorUtils());
    }

    @Nested
    @DisplayName("getAccessibleConstructor(Constructor)")
    class GetAccessibleConstructorByConstructor {

        @Test
        void withPublicConstructor_shouldReturnConstructor() throws Exception {
            final Constructor<Object> constructor = Object.class.getConstructor();
            assertNotNull(ConstructorUtils.getAccessibleConstructor(constructor));
        }

        @Test
        void withNonPublicConstructor_shouldReturnNull() throws Exception {
            final Constructor<PrivateClass> constructor = PrivateClass.class.getConstructor();
            assertNull(ConstructorUtils.getAccessibleConstructor(constructor));
        }
    }

    @Nested
    @DisplayName("getAccessibleConstructor(Class, Class...)")
    class GetAccessibleConstructorByTypes {

        @Test
        void withPublicClass_shouldReturnConstructor() {
            assertNotNull(ConstructorUtils.getAccessibleConstructor(Object.class, ArrayUtils.EMPTY_CLASS_ARRAY));
        }

        @Test
        void withNonPublicClass_shouldReturnNull() {
            assertNull(ConstructorUtils.getAccessibleConstructor(PrivateClass.class, ArrayUtils.EMPTY_CLASS_ARRAY));
        }

        @Test
        void withPublicInnerClassOfNonPublicClass_shouldReturnNull() {
            // The enclosing class is not accessible, so the constructor is not either.
            assertNull(ConstructorUtils.getAccessibleConstructor(PrivateClass.PublicInnerClass.class));
        }
    }

    @Nested
    @DisplayName("getMatchingAccessibleConstructor(Class, Class...)")
    class GetMatchingAccessibleConstructor {

        @Test
        void withNoArgs_shouldFindNoArgConstructor() {
            final Constructor<?> c = ConstructorUtils.getMatchingAccessibleConstructor(TestBean.class, ArrayUtils.EMPTY_CLASS_ARRAY);
            assertNotNull(c);
            assertArrayEquals(ArrayUtils.EMPTY_CLASS_ARRAY, c.getParameterTypes());
        }

        @Test
        void withNullArgTypes_shouldFindNoArgConstructor() {
            final Constructor<?> c = ConstructorUtils.getMatchingAccessibleConstructor(TestBean.class, (Class<?>[]) null);
            assertNotNull(c);
            assertArrayEquals(ArrayUtils.EMPTY_CLASS_ARRAY, c.getParameterTypes());
        }

        @Test
        void withExactMatch_shouldFindCorrectConstructor() {
            final Constructor<?> c = ConstructorUtils.getMatchingAccessibleConstructor(TestBean.class, String.class);
            assertNotNull(c);
            assertArrayEquals(new Class<?>[]{String.class}, c.getParameterTypes());
        }

        @Test
        void withAssignableArg_shouldFindSuperclassConstructor() {
            // SubClass is assignable to BaseClass
            final Constructor<?> c = ConstructorUtils.getMatchingAccessibleConstructor(TestBean.class, SubClass.class, String[].class);
            assertNotNull(c);
            assertArrayEquals(new Class<?>[]{BaseClass.class, String[].class}, c.getParameterTypes());
        }

        @Test
        void withObjectArg_shouldFindObjectConstructor() {
            // Boolean is assignable to Object
            final Constructor<?> c = ConstructorUtils.getMatchingAccessibleConstructor(TestBean.class, Boolean.class);
            assertNotNull(c);
            assertArrayEquals(new Class<?>[]{Object.class}, c.getParameterTypes());
        }

        @Test
        void withNullArg_shouldFindObjectConstructor() {
            final Constructor<?> c = ConstructorUtils.getMatchingAccessibleConstructor(TestBean.class, (Class<?>) null);
            assertNotNull(c);
            assertArrayEquals(new Class<?>[]{Object.class}, c.getParameterTypes());
        }

        @Test
        void withPrimitiveArg_shouldFindWidenedPrimitiveConstructor() {
            // short can be widened to int
            final Constructor<?> c = ConstructorUtils.getMatchingAccessibleConstructor(TestBean.class, Short.TYPE);
            assertNotNull(c);
            assertArrayEquals(new Class<?>[]{Integer.TYPE}, c.getParameterTypes());
        }

        @Test
        void withUnboxingAndWidening_shouldFindCorrectConstructor() {
            // Byte can be unboxed to byte, which can be widened to int
            final Constructor<?> c = ConstructorUtils.getMatchingAccessibleConstructor(TestBean.class, Byte.class);
            assertNotNull(c);
            assertArrayEquals(new Class<?>[]{Integer.TYPE}, c.getParameterTypes());
        }

        @Test
        void withWideningToDouble_shouldFindDoubleConstructor() {
            // Long can be widened to double
            final Constructor<?> c = ConstructorUtils.getMatchingAccessibleConstructor(TestBean.class, Long.class);
            assertNotNull(c);
            assertArrayEquals(new Class<?>[]{Double.TYPE}, c.getParameterTypes());
        }
    }

    @Nested
    @DisplayName("invokeConstructor(Class, Object...)")
    class InvokeConstructor {

        @Test
        void withNoArgs_shouldCreateInstance() throws Exception {
            final TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class);
            assertEquals("()", bean.constructorType);
        }

        @Test
        void withNullArgs_shouldCreateInstanceWithNoArgs() throws Exception {
            final TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class, (Object[]) null);
            assertEquals("()", bean.constructorType);
        }

        @Test
        void withMatchingArgs_shouldCreateInstance() throws Exception {
            final TestBean bean1 = ConstructorUtils.invokeConstructor(TestBean.class, "hello");
            assertEquals("(String)", bean1.constructorType);

            final TestBean bean2 = ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.INTEGER_ONE);
            assertEquals("(Integer)", bean2.constructorType);

            final TestBean bean3 = ConstructorUtils.invokeConstructor(TestBean.class, new Object());
            assertEquals("(Object)", bean3.constructorType);
        }

        @Test
        void withAssignableArg_shouldCreateInstance() throws Exception {
            // Boolean is assignable to Object, so it should invoke the Object constructor
            final TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class, Boolean.TRUE);
            assertEquals("(Object)", bean.constructorType);
        }

        @Test
        void withWideningConversion_shouldCreateInstance() throws Exception {
            // byte is widened to int
            final TestBean bean1 = ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.BYTE_ONE);
            assertEquals("(int)", bean1.constructorType);

            // long is widened to double
            final TestBean bean2 = ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.LONG_ONE);
            assertEquals("(double)", bean2.constructorType);
        }

        @Test
        void withVarArgs_shouldCreateInstance() throws Exception {
            final TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class, "a", "b");
            assertEquals("(String...)", bean.constructorType);
            assertArrayEquals(new String[]{"a", "b"}, bean.varArgs);
        }

        @Test
        void withMixedArgsAndVarArgs_shouldCreateInstance() throws Exception {
            final TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.INTEGER_ONE, "a", "b");
            assertEquals("(Integer, String...)", bean.constructorType);
            assertArrayEquals(new String[]{"a", "b"}, bean.varArgs);
        }

        @Test
        void withAssignableTypeAndVarArgs_shouldCreateInstance() throws Exception {
            final String[] args = {"a", "b"};
            final TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class, new SubClass(), args);
            assertEquals("(BaseClass, String...)", bean.constructorType);
            assertArrayEquals(args, bean.varArgs);
        }

        @Test
        void withUnboxingAndVarArgs_shouldCreateInstance() throws Exception {
            // Invokes TestBean(Integer, int...)
            final TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class, 1, 2, 3);
            assertEquals("(Integer, int...)", bean.constructorType);
            assertArrayEquals(new String[]{"2", "3"}, bean.varArgs);
        }
    }

    @Nested
    @DisplayName("invokeExactConstructor(Class, Object...)")
    class InvokeExactConstructor {

        @Test
        void withExactArgTypes_shouldCreateInstance() throws Exception {
            final TestBean bean1 = ConstructorUtils.invokeExactConstructor(TestBean.class);
            assertEquals("()", bean1.constructorType);

            final TestBean bean2 = ConstructorUtils.invokeExactConstructor(TestBean.class, "");
            assertEquals("(String)", bean2.constructorType);

            final TestBean bean3 = ConstructorUtils.invokeExactConstructor(TestBean.class, new Object());
            assertEquals("(Object)", bean3.constructorType);

            final TestBean bean4 = ConstructorUtils.invokeExactConstructor(TestBean.class, NumberUtils.INTEGER_ONE);
            assertEquals("(Integer)", bean4.constructorType);

            final TestBean bean5 = ConstructorUtils.invokeExactConstructor(TestBean.class,
                new Object[]{NumberUtils.DOUBLE_ONE}, new Class[]{Double.TYPE});
            assertEquals("(double)", bean5.constructorType);
        }

        @Test
        void withoutExactArgTypes_shouldThrowNoSuchMethodException() {
            // No constructor for Byte, requires widening
            assertThrows(NoSuchMethodException.class, () -> ConstructorUtils.invokeExactConstructor(TestBean.class, NumberUtils.BYTE_ONE));

            // No constructor for Long, requires widening
            assertThrows(NoSuchMethodException.class, () -> ConstructorUtils.invokeExactConstructor(TestBean.class, NumberUtils.LONG_ONE));

            // No constructor for Boolean, requires widening to Object
            assertThrows(NoSuchMethodException.class, () -> ConstructorUtils.invokeExactConstructor(TestBean.class, Boolean.TRUE));
        }
    }
}