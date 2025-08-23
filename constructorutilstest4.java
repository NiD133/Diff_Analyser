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

public class ConstructorUtilsTestTest4 extends AbstractLangTest {

    private final Map<Class<?>, Class<?>[]> classCache;

    private void expectMatchingAccessibleConstructorParameterTypes(final Class<?> cls, final Class<?>[] requestTypes, final Class<?>[] actualTypes) {
        final Constructor<?> c = ConstructorUtils.getMatchingAccessibleConstructor(cls, requestTypes);
        assertArrayEquals(actualTypes, c.getParameterTypes(), toString(c.getParameterTypes()) + " not equals " + toString(actualTypes));
    }

    @BeforeEach
    public void setUp() {
        classCache.clear();
    }

    private Class<?>[] singletonArray(final Class<?> c) {
        Class<?>[] result = classCache.get(c);
        if (result == null) {
            result = new Class[] { c };
            classCache.put(c, result);
        }
        return result;
    }

    private String toString(final Class<?>[] c) {
        return Arrays.asList(c).toString();
    }

    private static class BaseClass {
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

    private static final class SubClass extends BaseClass {
    }

    public static class TestBean {

        private final String toString;

        final String[] varArgs;

        public TestBean() {
            toString = "()";
            varArgs = null;
        }

        public TestBean(final BaseClass bc, final String... s) {
            toString = "(BaseClass, String...)";
            varArgs = s;
        }

        public TestBean(final double d) {
            toString = "(double)";
            varArgs = null;
        }

        public TestBean(final int i) {
            toString = "(int)";
            varArgs = null;
        }

        public TestBean(final Integer i) {
            toString = "(Integer)";
            varArgs = null;
        }

        public TestBean(final Integer first, final int... args) {
            toString = "(Integer, String...)";
            varArgs = new String[args.length];
            for (int i = 0; i < args.length; ++i) {
                varArgs[i] = Integer.toString(args[i]);
            }
        }

        public TestBean(final Integer i, final String... s) {
            toString = "(Integer, String...)";
            varArgs = s;
        }

        public TestBean(final Object o) {
            toString = "(Object)";
            varArgs = null;
        }

        public TestBean(final String s) {
            toString = "(String)";
            varArgs = null;
        }

        public TestBean(final String... s) {
            toString = "(String...)";
            varArgs = s;
        }

        @Override
        public String toString() {
            return toString;
        }

        void verify(final String str, final String[] args) {
            assertEquals(str, toString);
            assertArrayEquals(args, varArgs);
        }
    }

    @Test
    void testGetMatchingAccessibleMethod() {
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, ArrayUtils.EMPTY_CLASS_ARRAY, ArrayUtils.EMPTY_CLASS_ARRAY);
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, null, ArrayUtils.EMPTY_CLASS_ARRAY);
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, singletonArray(String.class), singletonArray(String.class));
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, singletonArray(Object.class), singletonArray(Object.class));
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, singletonArray(Boolean.class), singletonArray(Object.class));
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, singletonArray(Byte.class), singletonArray(Integer.TYPE));
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, singletonArray(Byte.TYPE), singletonArray(Integer.TYPE));
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, singletonArray(Short.class), singletonArray(Integer.TYPE));
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, singletonArray(Short.TYPE), singletonArray(Integer.TYPE));
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, singletonArray(Character.class), singletonArray(Integer.TYPE));
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, singletonArray(Character.TYPE), singletonArray(Integer.TYPE));
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, singletonArray(Integer.class), singletonArray(Integer.class));
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, singletonArray(Integer.TYPE), singletonArray(Integer.TYPE));
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, singletonArray(Long.class), singletonArray(Double.TYPE));
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, singletonArray(Long.TYPE), singletonArray(Double.TYPE));
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, singletonArray(Float.class), singletonArray(Double.TYPE));
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, singletonArray(Float.TYPE), singletonArray(Double.TYPE));
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, singletonArray(Double.class), singletonArray(Double.TYPE));
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, singletonArray(Double.TYPE), singletonArray(Double.TYPE));
        expectMatchingAccessibleConstructorParameterTypes(TestBean.class, new Class<?>[] { SubClass.class, String[].class }, new Class<?>[] { BaseClass.class, String[].class });
    }
}
