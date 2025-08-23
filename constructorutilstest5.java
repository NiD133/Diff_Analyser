package org.apache.commons.lang3.reflect;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link ConstructorUtils#invokeConstructor(Class, Object...)}.
 */
class ConstructorUtilsInvokeConstructorTest {

    // --- Test Fixture ---

    private static class BaseClass {
    }

    private static final class SubClass extends BaseClass {
    }

    /**
     * A test class with multiple constructors for testing constructor resolution.
     */
    public static class TestBean {

        private final String constructorSignature;
        private final String[] varArgs;

        public TestBean() {
            this.constructorSignature = "()";
            this.varArgs = null;
        }

        public TestBean(final String s) {
            this.constructorSignature = "(String)";
            this.varArgs = null;
        }

        public TestBean(final Integer i) {
            this.constructorSignature = "(Integer)";
            this.varArgs = null;
        }

        public TestBean(final int i) {
            this.constructorSignature = "(int)";
            this.varArgs = null;
        }

        public TestBean(final double d) {
            this.constructorSignature = "(double)";
            this.varArgs = null;
        }

        public TestBean(final Object o) {
            this.constructorSignature = "(Object)";
            this.varArgs = null;
        }

        public TestBean(final String... s) {
            this.constructorSignature = "(String...)";
            this.varArgs = s;
        }

        public TestBean(final Integer i, final String... s) {
            this.constructorSignature = "(Integer, String...)";
            this.varArgs = s;
        }

        public TestBean(final BaseClass bc, final String... s) {
            this.constructorSignature = "(BaseClass, String...)";
            this.varArgs = s;
        }

        /**
         * Verifies that the bean was created with the expected constructor and varargs.
         */
        void verify(final String expectedSignature, final String[] expectedVarArgs) {
            assertEquals(expectedSignature, this.constructorSignature);
            assertArrayEquals(expectedVarArgs, this.varArgs);
        }
    }

    // --- Test Cases ---

    @Test
    @DisplayName("invokeConstructor with no arguments should call the default constructor")
    void invokeConstructor_withNoArguments_callsDefaultConstructor() throws Exception {
        // Test the dedicated no-arg overload
        final TestBean bean1 = ConstructorUtils.invokeConstructor(TestBean.class);
        bean1.verify("()", null);

        // Test with an empty object array
        final TestBean bean2 = ConstructorUtils.invokeConstructor(TestBean.class, ArrayUtils.EMPTY_OBJECT_ARRAY);
        bean2.verify("()", null);

        // Test with a null argument array
        final TestBean bean3 = ConstructorUtils.invokeConstructor(TestBean.class, (Object[]) null);
        bean3.verify("()", null);
    }

    @Test
    @DisplayName("invokeConstructor should select constructor with an exact parameter type match")
    void invokeConstructor_withExactMatch_selectsCorrectConstructor() throws Exception {
        final TestBean stringBean = ConstructorUtils.invokeConstructor(TestBean.class, "Hello");
        stringBean.verify("(String)", null);

        final TestBean integerBean = ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.INTEGER_ONE);
        integerBean.verify("(Integer)", null);
    }

    @Test
    @DisplayName("invokeConstructor should select the Object constructor for a non-matching reference type")
    void invokeConstructor_withObjectType_selectsObjectConstructor() throws Exception {
        final TestBean objectBean = ConstructorUtils.invokeConstructor(TestBean.class, new Object());
        objectBean.verify("(Object)", null);

        // Boolean has no direct constructor match, so it should resolve to the Object constructor.
        final TestBean booleanBean = ConstructorUtils.invokeConstructor(TestBean.class, Boolean.TRUE);
        booleanBean.verify("(Object)", null);
    }

    @Test
    @DisplayName("invokeConstructor should select constructor by widening primitive types")
    void invokeConstructor_withPrimitiveWidening_selectsWiderTypeConstructor() throws Exception {
        // A byte argument should match the int constructor via widening
        final TestBean intBean = ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.BYTE_ONE);
        intBean.verify("(int)", null);

        // A long argument should match the double constructor via widening
        final TestBean doubleBean = ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.LONG_ONE);
        doubleBean.verify("(double)", null);

        // A double argument should match the double constructor (exact match)
        final TestBean doubleBean2 = ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.DOUBLE_ONE);
        doubleBean2.verify("(double)", null);
    }

    @Test
    @DisplayName("invokeConstructor should select constructor with a superclass parameter type")
    void invokeConstructor_withSubclassArgument_selectsSuperclassConstructor() throws Exception {
        final String[] args = {"a", "b"};
        final TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class, new SubClass(), args);
        bean.verify("(BaseClass, String...)", args);
    }

    @Test
    @DisplayName("invokeConstructor should handle simple varargs parameters")
    void invokeConstructor_withSimpleVarargs_createsObjectWithVarargs() throws Exception {
        final String[] args = {"a", "b"};
        final TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class, (Object[]) args);
        bean.verify("(String...)", args);
    }

    @Test
    @DisplayName("invokeConstructor should handle mixed fixed and varargs parameters")
    void invokeConstructor_withMixedVarargs_createsObjectWithAllArgs() throws Exception {
        final String[] varargs = {"a", "b"};
        final TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class, NumberUtils.INTEGER_ONE, varargs);
        bean.verify("(Integer, String...)", varargs);
    }
}