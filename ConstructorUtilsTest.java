package org.apache.commons.lang3.reflect;

import static org.junit.jupiter.api.Assertions.*;

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
 * Unit tests for ConstructorUtils.
 */
public class ConstructorUtilsTest extends AbstractLangTest {

    // Test classes for constructor testing
    private static class BaseClass {}

    static class PrivateClass {
        public static class PublicInnerClass {
            public PublicInnerClass() {}
        }

        public PrivateClass() {}
    }

    private static final class SubClass extends BaseClass {}

    public static class TestBean {
        private final String toString;
        final String[] varArgs;

        public TestBean() {
            this.toString = "()";
            this.varArgs = null;
        }

        public TestBean(final BaseClass bc, final String... s) {
            this.toString = "(BaseClass, String...)";
            this.varArgs = s;
        }

        public TestBean(final double d) {
            this.toString = "(double)";
            this.varArgs = null;
        }

        public TestBean(final int i) {
            this.toString = "(int)";
            this.varArgs = null;
        }

        public TestBean(final Integer i) {
            this.toString = "(Integer)";
            this.varArgs = null;
        }

        public TestBean(final Integer first, final int... args) {
            this.toString = "(Integer, String...)";
            this.varArgs = Arrays.stream(args).mapToObj(Integer::toString).toArray(String[]::new);
        }

        public TestBean(final Integer i, final String... s) {
            this.toString = "(Integer, String...)";
            this.varArgs = s;
        }

        public TestBean(final Object o) {
            this.toString = "(Object)";
            this.varArgs = null;
        }

        public TestBean(final String s) {
            this.toString = "(String)";
            this.varArgs = null;
        }

        public TestBean(final String... s) {
            this.toString = "(String...)";
            this.varArgs = s;
        }

        @Override
        public String toString() {
            return this.toString;
        }

        void verify(final String expectedToString, final String[] expectedVarArgs) {
            assertEquals(expectedToString, this.toString);
            assertArrayEquals(expectedVarArgs, this.varArgs);
        }
    }

    private final Map<Class<?>, Class<?>[]> classCache = new HashMap<>();

    @BeforeEach
    public void setUp() {
        classCache.clear();
    }

    private Class<?>[] getCachedClassArray(final Class<?> clazz) {
        return classCache.computeIfAbsent(clazz, k -> new Class[] { k });
    }

    private void assertMatchingConstructorParameterTypes(final Class<?> cls, final Class<?>[] requestTypes, final Class<?>[] expectedTypes) {
        final Constructor<?> constructor = ConstructorUtils.getMatchingAccessibleConstructor(cls, requestTypes);
        assertNotNull(constructor, "Constructor should not be null");
        assertArrayEquals(expectedTypes, constructor.getParameterTypes(), "Parameter types do not match");
    }

    @Test
    void testConstructor() throws Exception {
        assertNotNull(MethodUtils.class.getConstructor().newInstance(), "Constructor should create a new instance");
    }

    @Test
    void testGetAccessibleConstructor() throws Exception {
        assertNotNull(ConstructorUtils.getAccessibleConstructor(Object.class.getConstructor()), "Accessible constructor should not be null");
        assertNull(ConstructorUtils.getAccessibleConstructor(PrivateClass.class.getConstructor()), "Private constructor should be null");
        assertNull(ConstructorUtils.getAccessibleConstructor(PrivateClass.PublicInnerClass.class), "Inner class constructor should be null");
    }

    @Test
    void testGetAccessibleConstructorFromDescription() {
        assertNotNull(ConstructorUtils.getAccessibleConstructor(Object.class, ArrayUtils.EMPTY_CLASS_ARRAY), "Accessible constructor should not be null");
        assertNull(ConstructorUtils.getAccessibleConstructor(PrivateClass.class, ArrayUtils.EMPTY_CLASS_ARRAY), "Private constructor should be null");
    }

    @Test
    void testGetMatchingAccessibleMethod() {
        assertMatchingConstructorParameterTypes(TestBean.class, ArrayUtils.EMPTY_CLASS_ARRAY, ArrayUtils.EMPTY_CLASS_ARRAY);
        assertMatchingConstructorParameterTypes(TestBean.class, null, ArrayUtils.EMPTY_CLASS_ARRAY);
        assertMatchingConstructorParameterTypes(TestBean.class, getCachedClassArray(String.class), getCachedClassArray(String.class));
        assertMatchingConstructorParameterTypes(TestBean.class, getCachedClassArray(Object.class), getCachedClassArray(Object.class));
        assertMatchingConstructorParameterTypes(TestBean.class, getCachedClassArray(Boolean.class), getCachedClassArray(Object.class));
        assertMatchingConstructorParameterTypes(TestBean.class, getCachedClassArray(Byte.class), getCachedClassArray(int.class));
        assertMatchingConstructorParameterTypes(TestBean.class, getCachedClassArray(Byte.TYPE), getCachedClassArray(int.class));
        assertMatchingConstructorParameterTypes(TestBean.class, getCachedClassArray(Short.class), getCachedClassArray(int.class));
        assertMatchingConstructorParameterTypes(TestBean.class, getCachedClassArray(Short.TYPE), getCachedClassArray(int.class));
        assertMatchingConstructorParameterTypes(TestBean.class, getCachedClassArray(Character.class), getCachedClassArray(int.class));
        assertMatchingConstructorParameterTypes(TestBean.class, getCachedClassArray(Character.TYPE), getCachedClassArray(int.class));
        assertMatchingConstructorParameterTypes(TestBean.class, getCachedClassArray(Integer.class), getCachedClassArray(Integer.class));
        assertMatchingConstructorParameterTypes(TestBean.class, getCachedClassArray(Integer.TYPE), getCachedClassArray(int.class));
        assertMatchingConstructorParameterTypes(TestBean.class, getCachedClassArray(Long.class), getCachedClassArray(double.class));
        assertMatchingConstructorParameterTypes(TestBean.class, getCachedClassArray(Long.TYPE), getCachedClassArray(double.class));
        assertMatchingConstructorParameterTypes(TestBean.class, getCachedClassArray(Float.class), getCachedClassArray(double.class));
        assertMatchingConstructorParameterTypes(TestBean.class, getCachedClassArray(Float.TYPE), getCachedClassArray(double.class));
        assertMatchingConstructorParameterTypes(TestBean.class, getCachedClassArray(Double.class), getCachedClassArray(double.class));
        assertMatchingConstructorParameterTypes(TestBean.class, getCachedClassArray(Double.TYPE), getCachedClassArray(double.class));
        assertMatchingConstructorParameterTypes(TestBean.class, new Class<?>[] { SubClass.class, String[].class }, new Class<?>[] { BaseClass.class, String[].class });
    }

    @Test
    void testInvokeConstructor() throws Exception {
        assertEquals("()", ConstructorUtils.invokeConstructor(TestBean.class, (Object[]) ArrayUtils.EMPTY_CLASS_ARRAY).toString());
        assertEquals("()", ConstructorUtils.invokeConstructor(TestBean.class, (Object[]) null).toString());
        assertEquals("()", ConstructorUtils.invokeConstructor(TestBean.class).toString());
        assertEquals("(String)", ConstructorUtils.invokeConstructor(TestBean.class, "").toString());
        assertEquals("(Object)", ConstructorUtils.invokeConstructor(TestBean.class, new Object()).toString());
        assertEquals("(Object)", ConstructorUtils.invokeConstructor(TestBean.class, Boolean.TRUE).toString());
        assertEquals("(Integer)", ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.INTEGER_ONE).toString());
        assertEquals("(int)", ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.BYTE_ONE).toString());
        assertEquals("(double)", ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.LONG_ONE).toString());
        assertEquals("(double)", ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.DOUBLE_ONE).toString());
        ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.INTEGER_ONE).verify("(Integer)", null);
        ConstructorUtils.invokeConstructor(TestBean.class, "a", "b").verify("(String...)", new String[] { "a", "b" });
        ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.INTEGER_ONE, "a", "b").verify("(Integer, String...)", new String[] { "a", "b" });
        ConstructorUtils.invokeConstructor(TestBean.class, new SubClass(), new String[] { "a", "b" }).verify("(BaseClass, String...)", new String[] { "a", "b" });
    }

    @Test
    void testInvokeExactConstructor() throws Exception {
        assertEquals("()", ConstructorUtils.invokeExactConstructor(TestBean.class, (Object[]) ArrayUtils.EMPTY_CLASS_ARRAY).toString());
        assertEquals("()", ConstructorUtils.invokeExactConstructor(TestBean.class, (Object[]) null).toString());
        assertEquals("(String)", ConstructorUtils.invokeExactConstructor(TestBean.class, "").toString());
        assertEquals("(Object)", ConstructorUtils.invokeExactConstructor(TestBean.class, new Object()).toString());
        assertEquals("(Integer)", ConstructorUtils.invokeExactConstructor(TestBean.class, NumberUtils.INTEGER_ONE).toString());
        assertEquals("(double)", ConstructorUtils.invokeExactConstructor(TestBean.class, new Object[] { NumberUtils.DOUBLE_ONE }, new Class[] { Double.TYPE }).toString());

        assertThrows(NoSuchMethodException.class, () -> ConstructorUtils.invokeExactConstructor(TestBean.class, NumberUtils.BYTE_ONE));
        assertThrows(NoSuchMethodException.class, () -> ConstructorUtils.invokeExactConstructor(TestBean.class, NumberUtils.LONG_ONE));
        assertThrows(NoSuchMethodException.class, () -> ConstructorUtils.invokeExactConstructor(TestBean.class, Boolean.TRUE));
    }

    @Test
    void testNullArgument() {
        assertMatchingConstructorParameterTypes(MutableObject.class, getCachedClassArray(null), getCachedClassArray(Object.class));
    }

    @Test
    void testVarArgsUnboxing() throws Exception {
        final TestBean testBean = ConstructorUtils.invokeConstructor(TestBean.class, Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
        assertArrayEquals(new String[] { "2", "3" }, testBean.varArgs);
    }

    private String parameterTypesToString(final Class<?>[] parameterTypes) {
        return Arrays.toString(parameterTypes);
    }
}