package org.apache.commons.lang3.reflect;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ConstructorUtils#invokeExactConstructor(Class, Object...)}
 * and {@link ConstructorUtils#invokeExactConstructor(Class, Object[], Class[])}.
 */
class ConstructorUtilsInvokeExactConstructorTest {

    // region Test Fixtures

    private static class BaseClass {
    }

    private static final class SubClass extends BaseClass {
    }

    /**
     * A test bean with various constructors, used to verify that the correct
     * one is invoked. The toString() method returns a string representation
     * of the constructor's signature that was called.
     */
    public static class TestBean {

        private final String toString;
        final String[] varArgs;

        public TestBean() {
            this.toString = "()";
            this.varArgs = null;
        }

        public TestBean(final String s) {
            this.toString = "(String)";
            this.varArgs = null;
        }

        public TestBean(final Integer i) {
            this.toString = "(Integer)";
            this.varArgs = null;
        }

        public TestBean(final double d) {
            this.toString = "(double)";
            this.varArgs = null;
        }

        public TestBean(final Object o) {
            this.toString = "(Object)";
            this.varArgs = null;
        }

        // Constructors with varargs, not used in these specific tests but part of the fixture
        public TestBean(final String... s) {
            this.toString = "(String...)";
            this.varArgs = s;
        }

        public TestBean(final Integer first, final int... args) {
            this.toString = "(Integer, String...)";
            this.varArgs = new String[args.length];
            for (int i = 0; i < args.length; ++i) {
                varArgs[i] = Integer.toString(args[i]);
            }
        }

        public TestBean(final Integer i, final String... s) {
            this.toString = "(Integer, String...)";
            this.varArgs = s;
        }

        public TestBean(final BaseClass bc, final String... s) {
            this.toString = "(BaseClass, String...)";
            this.varArgs = s;
        }

        @Override
        public String toString() {
            return toString;
        }
    }
    // endregion

    @Test
    @DisplayName("invokeExactConstructor() with no arguments should invoke the no-arg constructor")
    void invokeWithNoArgsShouldCallNoArgConstructor() throws Exception {
        final TestBean instance = ConstructorUtils.invokeExactConstructor(TestBean.class);
        assertEquals("()", instance.toString());
    }

    @Test
    @DisplayName("invokeExactConstructor() with a null arguments array should invoke the no-arg constructor")
    void invokeWithNullArgsShouldCallNoArgConstructor() throws Exception {
        final TestBean instance = ConstructorUtils.invokeExactConstructor(TestBean.class, (Object[]) null);
        assertEquals("()", instance.toString());
    }

    @Test
    @DisplayName("invokeExactConstructor() should find a constructor with an exact String argument match")
    void invokeWithExactStringArg() throws Exception {
        final TestBean instance = ConstructorUtils.invokeExactConstructor(TestBean.class, "test");
        assertEquals("(String)", instance.toString());
    }

    @Test
    @DisplayName("invokeExactConstructor() should find a constructor with an exact Object argument match")
    void invokeWithExactObjectArg() throws Exception {
        final TestBean instance = ConstructorUtils.invokeExactConstructor(TestBean.class, new Object());
        assertEquals("(Object)", instance.toString());
    }

    @Test
    @DisplayName("invokeExactConstructor() should find a constructor with an exact Integer argument match")
    void invokeWithExactIntegerArg() throws Exception {
        final TestBean instance = ConstructorUtils.invokeExactConstructor(TestBean.class, 1);
        assertEquals("(Integer)", instance.toString());
    }

    @Test
    @DisplayName("invokeExactConstructor() should find a constructor with a primitive double argument")
    void invokeWithPrimitiveDoubleArg() throws Exception {
        final Object[] args = {1.0d};
        final Class<?>[] parameterTypes = {double.class};
        final TestBean instance = ConstructorUtils.invokeExactConstructor(TestBean.class, args, parameterTypes);
        assertEquals("(double)", instance.toString());
    }

    @Test
    @DisplayName("invokeExactConstructor() should throw NoSuchMethodException for non-matching argument types")
    void invokeWithNonMatchingArgsThrowsException() {
        // TestBean has no constructor for byte, long, or boolean
        assertThrows(NoSuchMethodException.class, () -> ConstructorUtils.invokeExactConstructor(TestBean.class, (byte) 1));
        assertThrows(NoSuchMethodException.class, () -> ConstructorUtils.invokeExactConstructor(TestBean.class, 1L));
        assertThrows(NoSuchMethodException.class, () -> ConstructorUtils.invokeExactConstructor(TestBean.class, true));
    }
}