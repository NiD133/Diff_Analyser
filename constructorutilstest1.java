package org.apache.commons.lang3.reflect;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ConstructorUtils}.
 */
public class ConstructorUtilsTest extends AbstractLangTest {

    // --- Test Fixtures ---

    private static class BaseClass {
    }

    private static final class SubClass extends BaseClass {
    }

    static class PrivateClass {
        // This class is not public, so its constructors are not "accessible"
        // by ConstructorUtils's definition, even if the constructor is public.
        @SuppressWarnings("unused")
        public PrivateClass() {
        }

        @SuppressWarnings("unused")
        public static class PublicInnerClass {
            public PublicInnerClass() {
            }
        }
    }

    public static class TestBean {
        private final String toString;
        final String[] varArgs;

        public TestBean() {
            toString = "()";
            varArgs = null;
        }

        public TestBean(final String s) {
            toString = "(String)";
            varArgs = null;
        }

        public TestBean(final Integer i) {
            toString = "(Integer)";
            varArgs = null;
        }

        public TestBean(final int i) {
            toString = "(int)";
            varArgs = null;
        }

        public TestBean(final double d) {
            toString = "(double)";
            varArgs = null;
        }

        public TestBean(final Object o) {
            toString = "(Object)";
            varArgs = null;
        }

        public TestBean(final String... s) {
            toString = "(String...)";
            varArgs = s;
        }

        public TestBean(final BaseClass bc, final String... s) {
            toString = "(BaseClass, String...)";
            varArgs = s;
        }

        public TestBean(final Integer i, final String... s) {
            toString = "(Integer, String...)";
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

    // --- Test Cases ---

    @Test
    public void testPublicConstructor() {
        // The public constructor is for tools, but we should ensure it's instantiable.
        assertNotNull(new ConstructorUtils());
    }

    // --- getMatchingAccessibleConstructor Tests ---

    @Test
    public void getMatchingAccessibleConstructor_withNoArgs_shouldFindNoArgConstructor() {
        final Constructor<TestBean> c = ConstructorUtils.getMatchingAccessibleConstructor(TestBean.class);
        assertNotNull(c, "Should find no-arg constructor");
        assertEquals(0, c.getParameterCount());
    }

    @Test
    public void getMatchingAccessibleConstructor_withExactMatch_shouldFindConstructor() {
        final Constructor<TestBean> c = ConstructorUtils.getMatchingAccessibleConstructor(TestBean.class, String.class);
        assertNotNull(c, "Should find constructor with exact parameter match");
        assertArrayEquals(new Class<?>[]{String.class}, c.getParameterTypes());
    }

    @Test
    public void getMatchingAccessibleConstructor_withAssignableMatch_shouldFindConstructor() {
        // A SubClass instance is assignable to a BaseClass parameter.
        final Constructor<TestBean> c = ConstructorUtils.getMatchingAccessibleConstructor(TestBean.class, SubClass.class, String[].class);
        assertNotNull(c, "Should find constructor with assignable parameter match");
        assertArrayEquals(new Class<?>[]{BaseClass.class, String[].class}, c.getParameterTypes());
    }

    @Test
    public void getMatchingAccessibleConstructor_withPrimitiveMatch_shouldFindConstructor() {
        final Constructor<TestBean> c = ConstructorUtils.getMatchingAccessibleConstructor(TestBean.class, int.class);
        assertNotNull(c, "Should find constructor with primitive parameter");
        assertArrayEquals(new Class<?>[]{int.class}, c.getParameterTypes());
    }

    @Test
    public void getMatchingAccessibleConstructor_withNullType_shouldMatchObject() {
        // A null parameter type is treated as a literal null, which can match any Object-derived parameter.
        final Constructor<TestBean> c = ConstructorUtils.getMatchingAccessibleConstructor(TestBean.class, (Class<?>) null);
        assertNotNull(c, "A null type should match the Object constructor");
        assertArrayEquals(new Class<?>[]{Object.class}, c.getParameterTypes());
    }

    @Test
    public void getMatchingAccessibleConstructor_withVarargs_shouldFindVarargsConstructor() {
        final Constructor<TestBean> c = ConstructorUtils.getMatchingAccessibleConstructor(TestBean.class, String.class, String.class);
        assertNotNull(c, "Should find varargs constructor");
        assertArrayEquals(new Class<?>[]{String[].class}, c.getParameterTypes());
    }

    @Test
    public void getMatchingAccessibleConstructor_forInaccessibleClass_shouldReturnNull() {
        // PrivateClass is not public, so its constructors are not considered accessible.
        assertNull(ConstructorUtils.getMatchingAccessibleConstructor(PrivateClass.class));
        assertNull(ConstructorUtils.getMatchingAccessibleConstructor(PrivateClass.PublicInnerClass.class));
    }

    @Test
    public void getMatchingAccessibleConstructor_withNoMatch_shouldReturnNull() {
        // No constructor for (String, double) exists.
        assertNull(ConstructorUtils.getMatchingAccessibleConstructor(TestBean.class, String.class, double.class));
    }

    // --- invokeConstructor Tests ---

    @Test
    public void invokeConstructor_withNoArgs_shouldCreateInstance() throws Exception {
        final TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class);
        bean.verify("()", null);
    }

    @Test
    public void invokeConstructor_withArgs_shouldCreateInstance() throws Exception {
        final TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class, "test");
        bean.verify("(String)", null);
    }

    @Test
    public void invokeConstructor_withWideningConversion_shouldCreateInstance() throws Exception {
        // An argument of type float should match the constructor taking a double.
        final TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class, 1.0f);
        bean.verify("(double)", null);
    }

    @Test
    public void invokeConstructor_withVarargs_shouldCreateInstance() throws Exception {
        final String[] args = {"a", "b"};
        final TestBean bean = ConstructorUtils.invokeConstructor(TestBean.class, (Object) args);
        bean.verify("(String...)", args);

        final TestBean bean2 = ConstructorUtils.invokeConstructor(TestBean.class, "a", "b");
        bean2.verify("(String...)", new String[]{"a", "b"});
    }

    @Test
    public void invokeConstructor_withAmbiguousNull_shouldThrowException() {
        // A null argument is ambiguous; it could match (Object), (String), or (String[]).
        assertThrows(NoSuchMethodException.class, () -> {
            ConstructorUtils.invokeConstructor(TestBean.class, (Object) null);
        });
    }

    @Test
    public void invokeConstructor_withNoMatch_shouldThrowException() {
        assertThrows(NoSuchMethodException.class, () -> {
            ConstructorUtils.invokeConstructor(TestBean.class, "a", 1.0);
        });
    }

    // --- invokeExactConstructor Tests ---

    @Test
    public void invokeExactConstructor_withExactArgs_shouldCreateInstance() throws Exception {
        final TestBean bean = ConstructorUtils.invokeExactConstructor(TestBean.class, "test");
        bean.verify("(String)", null);
    }

    @Test
    public void invokeExactConstructor_withWideningConversion_shouldThrowException() {
        // invokeExactConstructor requires an exact match, so float -> double is not allowed.
        assertThrows(NoSuchMethodException.class, () -> {
            ConstructorUtils.invokeExactConstructor(TestBean.class, 1.0f);
        });
    }

    @Test
    public void invokeExactConstructor_withSubclass_shouldThrowException() {
        // SubClass is not exactly BaseClass, so this should fail.
        assertThrows(NoSuchMethodException.class, () -> {
            ConstructorUtils.invokeExactConstructor(TestBean.class, new SubClass(), new String[]{"a"});
        });
    }

    @Test
    public void invokeExactConstructor_withExplicitTypes_shouldCreateInstance() throws Exception {
        final String[] args = {"a", "b"};
        // Explicitly specify the String[] parameter type to resolve ambiguity.
        final TestBean bean = ConstructorUtils.invokeExactConstructor(TestBean.class, new Object[]{args}, new Class<?>[]{String[].class});
        bean.verify("(String...)", args);
    }
}