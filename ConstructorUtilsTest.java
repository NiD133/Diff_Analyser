package org.apache.commons.lang3.reflect;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.apache.commons.lang3.AbstractLangTest;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.mutable.MutableObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for ConstructorUtils with a focus on clarity:
 * - Small helpers to reduce noise around Class<?>[] construction.
 * - Descriptive test names and comments explaining intent.
 * - Grouped assertions by behavior (accessibility, matching, invocation, varargs, etc.).
 */
public class ConstructorUtilsTest extends AbstractLangTest {

    // ------------ Test Fixtures ------------

    private static class BaseClass { /* marker type for tests */ }

    private static final class SubClass extends BaseClass { /* subclass of BaseClass */ }

    /**
     * A class with public and private visibility scenarios.
     */
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
     * A bean exposing multiple overloaded constructors to exercise matching rules:
     * - Widening and boxing/unboxing
     * - Object vs String vs primitive types
     * - Varargs resolution
     * - Inheritance (BaseClass vs SubClass)
     */
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

        void verify(final String expectedToString, final String[] expectedVarArgs) {
            assertEquals(expectedToString, toString);
            assertArrayEquals(expectedVarArgs, varArgs);
        }
    }

    // ------------ Test State/Helpers ------------

    private static final Class<?>[] NO_PARAMS = new Class<?>[0];

    @BeforeEach
    public void setUp() {
        // No mutable shared state; reserved for future use.
    }

    private static Class<?>[] types(final Class<?>... parameterTypes) {
        return parameterTypes == null ? NO_PARAMS : parameterTypes;
    }

    private static String typeListToString(final Class<?>[] types) {
        return Arrays.asList(types).toString();
    }

    /**
     * Asserts that getMatchingAccessibleConstructor resolves to a constructor
     * whose parameter types equal expectedTypes when requested with requestTypes.
     */
    private static void assertMatchingParamTypes(final Class<?> target,
                                                 final Class<?>[] requestTypes,
                                                 final Class<?>[] expectedTypes) {
        final Constructor<?> ctor = ConstructorUtils.getMatchingAccessibleConstructor(target, requestTypes);
        assertNotNull(ctor, "Expected a matching accessible constructor");
        final Class<?>[] actual = ctor.getParameterTypes();
        assertArrayEquals(
            expectedTypes,
            actual,
            typeListToString(actual) + " != " + typeListToString(expectedTypes)
        );
    }

    // ------------ Tests: Accessibility ------------

    @Test
    @DisplayName("Smoke: public no-arg constructor on a utility class (MethodUtils) is present")
    void testConstructor() throws Exception {
        // Historically these Utils classes expose a public no-arg constructor (even if discouraged).
        assertNotNull(MethodUtils.class.getConstructor().newInstance());
    }

    @Test
    @DisplayName("getAccessibleConstructor(Constructor) respects visibility of declaring types")
    void testGetAccessibleConstructor() throws Exception {
        assertNotNull(ConstructorUtils.getAccessibleConstructor(Object.class.getConstructor(NO_PARAMS)));
        assertNull(ConstructorUtils.getAccessibleConstructor(PrivateClass.class.getConstructor(NO_PARAMS)));
        // Public inner class of a non-public enclosing class should not be accessible here
        assertNull(ConstructorUtils.getAccessibleConstructor(PrivateClass.PublicInnerClass.class));
    }

    @Test
    @DisplayName("getAccessibleConstructor(Class, Class...) respects visibility of declaring types")
    void testGetAccessibleConstructorFromDescription() {
        assertNotNull(ConstructorUtils.getAccessibleConstructor(Object.class, NO_PARAMS));
        assertNull(ConstructorUtils.getAccessibleConstructor(PrivateClass.class, NO_PARAMS));
    }

    // ------------ Tests: Matching (overload resolution, widening, boxing, varargs) ------------

    @Test
    @DisplayName("getMatchingAccessibleConstructor: exact, null, boxing, widening, and inheritance")
    void testGetMatchingAccessibleConstructor() {
        // Exact and null parameter types
        assertMatchingParamTypes(TestBean.class, NO_PARAMS, NO_PARAMS);
        assertMatchingParamTypes(TestBean.class, null, NO_PARAMS);

        // Exact matches
        assertMatchingParamTypes(TestBean.class, types(String.class), types(String.class));
        assertMatchingParamTypes(TestBean.class, types(Object.class), types(Object.class));

        // Non-exact: falls back to Object
        assertMatchingParamTypes(TestBean.class, types(Boolean.class), types(Object.class));

        // Widening/boxing to (int)
        assertMatchingParamTypes(TestBean.class, types(Byte.class), types(Integer.TYPE));
        assertMatchingParamTypes(TestBean.class, types(Byte.TYPE), types(Integer.TYPE));
        assertMatchingParamTypes(TestBean.class, types(Short.class), types(Integer.TYPE));
        assertMatchingParamTypes(TestBean.class, types(Short.TYPE), types(Integer.TYPE));
        assertMatchingParamTypes(TestBean.class, types(Character.class), types(Integer.TYPE));
        assertMatchingParamTypes(TestBean.class, types(Character.TYPE), types(Integer.TYPE));
        assertMatchingParamTypes(TestBean.class, types(Integer.class), types(Integer.class));
        assertMatchingParamTypes(TestBean.class, types(Integer.TYPE), types(Integer.TYPE));

        // Widening to (double)
        assertMatchingParamTypes(TestBean.class, types(Long.class), types(Double.TYPE));
        assertMatchingParamTypes(TestBean.class, types(Long.TYPE), types(Double.TYPE));
        assertMatchingParamTypes(TestBean.class, types(Float.class), types(Double.TYPE));
        assertMatchingParamTypes(TestBean.class, types(Float.TYPE), types(Double.TYPE));
        assertMatchingParamTypes(TestBean.class, types(Double.class), types(Double.TYPE));
        assertMatchingParamTypes(TestBean.class, types(Double.TYPE), types(Double.TYPE));

        // Inheritance + varargs shape: (SubClass, String[]) -> (BaseClass, String[])
        assertMatchingParamTypes(TestBean.class,
            types(SubClass.class, String[].class),
            types(BaseClass.class, String[].class));
    }

    // ------------ Tests: Invocation (compatible match) ------------

    @Test
    @DisplayName("invokeConstructor: compatible match (boxing, widening, varargs, inheritance)")
    void testInvokeConstructor() throws Exception {
        // No-args
        assertEquals("()", ConstructorUtils.invokeConstructor(TestBean.class, (Object[]) NO_PARAMS).toString());
        assertEquals("()", ConstructorUtils.invokeConstructor(TestBean.class, (Object[]) null).toString());
        assertEquals("()", ConstructorUtils.invokeConstructor(TestBean.class).toString());

        // Simple overloads
        assertEquals("(String)", ConstructorUtils.invokeConstructor(TestBean.class, "").toString());
        assertEquals("(Object)", ConstructorUtils.invokeConstructor(TestBean.class, new Object()).toString());

        // Boxing/widening selections
        assertEquals("(Object)", ConstructorUtils.invokeConstructor(TestBean.class, Boolean.TRUE).toString());
        assertEquals("(Integer)", ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.INTEGER_ONE).toString());
        assertEquals("(int)", ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.BYTE_ONE).toString());
        assertEquals("(double)", ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.LONG_ONE).toString());
        assertEquals("(double)", ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.DOUBLE_ONE).toString());

        // Varargs and inheritance
        ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.INTEGER_ONE).verify("(Integer)", null);
        ConstructorUtils.invokeConstructor(TestBean.class, "a", "b").verify("(String...)", new String[] { "a", "b" });
        ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.INTEGER_ONE, "a", "b")
            .verify("(Integer, String...)", new String[] { "a", "b" });
        ConstructorUtils.invokeConstructor(TestBean.class, new SubClass(), new String[] { "a", "b" })
            .verify("(BaseClass, String...)", new String[] { "a", "b" });
    }

    // ------------ Tests: Invocation (exact match) ------------

    @Test
    @DisplayName("invokeExactConstructor: exact match only (no boxing/widening)")
    void testInvokeExactConstructor() throws Exception {
        // No-args
        assertEquals("()", ConstructorUtils.invokeExactConstructor(TestBean.class, (Object[]) NO_PARAMS).toString());
        assertEquals("()", ConstructorUtils.invokeExactConstructor(TestBean.class, (Object[]) null).toString());

        // Exact matches
        assertEquals("(String)", ConstructorUtils.invokeExactConstructor(TestBean.class, "").toString());
        assertEquals("(Object)", ConstructorUtils.invokeExactConstructor(TestBean.class, new Object()).toString());
        assertEquals("(Integer)", ConstructorUtils.invokeExactConstructor(TestBean.class, NumberUtils.INTEGER_ONE).toString());

        // Explicit primitive signature
        assertEquals("(double)",
            ConstructorUtils.invokeExactConstructor(
                TestBean.class,
                new Object[] { NumberUtils.DOUBLE_ONE },
                new Class[] { Double.TYPE }
            ).toString()
        );

        // No widening/boxing allowed in exact mode
        assertThrows(NoSuchMethodException.class, () -> ConstructorUtils.invokeExactConstructor(TestBean.class, NumberUtils.BYTE_ONE));
        assertThrows(NoSuchMethodException.class, () -> ConstructorUtils.invokeExactConstructor(TestBean.class, NumberUtils.LONG_ONE));
        assertThrows(NoSuchMethodException.class, () -> ConstructorUtils.invokeExactConstructor(TestBean.class, Boolean.TRUE));
    }

    // ------------ Edge cases ------------

    @Test
    @DisplayName("Null argument type resolves to Object parameter")
    void testNullArgument() {
        assertMatchingParamTypes(MutableObject.class, types((Class<?>) null), types(Object.class));
    }

    @Test
    @DisplayName("Varargs with unboxing inside the varargs segment")
    void testVarArgsUnboxing() throws Exception {
        final TestBean testBean = ConstructorUtils.invokeConstructor(
            TestBean.class,
            Integer.valueOf(1), // selects (Integer, int...)
            Integer.valueOf(2),
            Integer.valueOf(3)
        );
        assertArrayEquals(new String[] { "2", "3" }, testBean.varArgs);
    }
}