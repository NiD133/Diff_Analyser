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
 * Unit tests for the {@link ConstructorUtils} class.
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
        private final String description;
        final String[] varArgs;

        public TestBean() {
            description = "()";
            varArgs = null;
        }

        public TestBean(final BaseClass bc, final String... args) {
            description = "(BaseClass, String...)";
            varArgs = args;
        }

        public TestBean(final double d) {
            description = "(double)";
            varArgs = null;
        }

        public TestBean(final int i) {
            description = "(int)";
            varArgs = null;
        }

        public TestBean(final Integer i) {
            description = "(Integer)";
            varArgs = null;
        }

        public TestBean(final Integer first, final int... args) {
            description = "(Integer, int...)";
            varArgs = Arrays.stream(args).mapToObj(String::valueOf).toArray(String[]::new);
        }

        public TestBean(final Integer i, final String... args) {
            description = "(Integer, String...)";
            varArgs = args;
        }

        public TestBean(final Object o) {
            description = "(Object)";
            varArgs = null;
        }

        public TestBean(final String s) {
            description = "(String)";
            varArgs = null;
        }

        public TestBean(final String... args) {
            description = "(String...)";
            varArgs = args;
        }

        @Override
        public String toString() {
            return description;
        }

        void verify(final String expectedDescription, final String[] expectedArgs) {
            assertEquals(expectedDescription, description);
            assertArrayEquals(expectedArgs, varArgs);
        }
    }

    private final Map<Class<?>, Class<?>[]> classCache = new HashMap<>();

    @BeforeEach
    public void setUp() {
        classCache.clear();
    }

    private Class<?>[] getCachedClassArray(final Class<?> clazz) {
        return classCache.computeIfAbsent(clazz, k -> new Class<?>[] { clazz });
    }

    private void verifyMatchingAccessibleConstructor(final Class<?> cls, final Class<?>[] requestTypes, final Class<?>[] expectedTypes) {
        final Constructor<?> constructor = ConstructorUtils.getMatchingAccessibleConstructor(cls, requestTypes);
        assertArrayEquals(expectedTypes, constructor.getParameterTypes(), 
            "Expected: " + Arrays.toString(expectedTypes) + ", but got: " + Arrays.toString(constructor.getParameterTypes()));
    }

    @Test
    void testConstructorInstantiation() throws Exception {
        assertNotNull(MethodUtils.class.getConstructor().newInstance());
    }

    @Test
    void testGetAccessibleConstructor() throws Exception {
        assertNotNull(ConstructorUtils.getAccessibleConstructor(Object.class.getConstructor()));
        assertNull(ConstructorUtils.getAccessibleConstructor(PrivateClass.class.getConstructor()));
        assertNull(ConstructorUtils.getAccessibleConstructor(PrivateClass.PublicInnerClass.class.getConstructor()));
    }

    @Test
    void testGetAccessibleConstructorFromDescription() {
        assertNotNull(ConstructorUtils.getAccessibleConstructor(Object.class, ArrayUtils.EMPTY_CLASS_ARRAY));
        assertNull(ConstructorUtils.getAccessibleConstructor(PrivateClass.class, ArrayUtils.EMPTY_CLASS_ARRAY));
    }

    @Test
    void testGetMatchingAccessibleConstructor() {
        verifyMatchingAccessibleConstructor(TestBean.class, ArrayUtils.EMPTY_CLASS_ARRAY, ArrayUtils.EMPTY_CLASS_ARRAY);
        verifyMatchingAccessibleConstructor(TestBean.class, null, ArrayUtils.EMPTY_CLASS_ARRAY);
        verifyMatchingAccessibleConstructor(TestBean.class, getCachedClassArray(String.class), getCachedClassArray(String.class));
        verifyMatchingAccessibleConstructor(TestBean.class, getCachedClassArray(Object.class), getCachedClassArray(Object.class));
        verifyMatchingAccessibleConstructor(TestBean.class, getCachedClassArray(Boolean.class), getCachedClassArray(Object.class));
        verifyMatchingAccessibleConstructor(TestBean.class, getCachedClassArray(Byte.class), getCachedClassArray(int.class));
        verifyMatchingAccessibleConstructor(TestBean.class, getCachedClassArray(Byte.TYPE), getCachedClassArray(int.class));
        verifyMatchingAccessibleConstructor(TestBean.class, getCachedClassArray(Short.class), getCachedClassArray(int.class));
        verifyMatchingAccessibleConstructor(TestBean.class, getCachedClassArray(Short.TYPE), getCachedClassArray(int.class));
        verifyMatchingAccessibleConstructor(TestBean.class, getCachedClassArray(Character.class), getCachedClassArray(int.class));
        verifyMatchingAccessibleConstructor(TestBean.class, getCachedClassArray(Character.TYPE), getCachedClassArray(int.class));
        verifyMatchingAccessibleConstructor(TestBean.class, getCachedClassArray(Integer.class), getCachedClassArray(Integer.class));
        verifyMatchingAccessibleConstructor(TestBean.class, getCachedClassArray(Integer.TYPE), getCachedClassArray(int.class));
        verifyMatchingAccessibleConstructor(TestBean.class, getCachedClassArray(Long.class), getCachedClassArray(double.class));
        verifyMatchingAccessibleConstructor(TestBean.class, getCachedClassArray(Long.TYPE), getCachedClassArray(double.class));
        verifyMatchingAccessibleConstructor(TestBean.class, getCachedClassArray(Float.class), getCachedClassArray(double.class));
        verifyMatchingAccessibleConstructor(TestBean.class, getCachedClassArray(Float.TYPE), getCachedClassArray(double.class));
        verifyMatchingAccessibleConstructor(TestBean.class, getCachedClassArray(Double.class), getCachedClassArray(double.class));
        verifyMatchingAccessibleConstructor(TestBean.class, getCachedClassArray(Double.TYPE), getCachedClassArray(double.class));
        verifyMatchingAccessibleConstructor(TestBean.class, new Class<?>[] { SubClass.class, String[].class }, new Class<?>[] { BaseClass.class, String[].class });
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
    void testNullArgumentHandling() {
        verifyMatchingAccessibleConstructor(MutableObject.class, getCachedClassArray(null), getCachedClassArray(Object.class));
    }

    @Test
    void testVarArgsUnboxing() throws Exception {
        final TestBean testBean = ConstructorUtils.invokeConstructor(TestBean.class, Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
        assertArrayEquals(new String[] { "2", "3" }, testBean.varArgs);
    }
}