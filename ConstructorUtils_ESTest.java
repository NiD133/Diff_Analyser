package org.apache.commons.lang3.reflect;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link ConstructorUtils}.
 */
public class ConstructorUtilsTest {

    /** A helper class for testing constructor accessibility. */
    private static class TestBean {
        private final String value;

        // Public constructor
        public TestBean(String value) {
            this.value = value;
        }

        // Private constructor
        private TestBean(int value) {
            this.value = String.valueOf(value);
        }

        // Public no-arg constructor
        public TestBean() {
            this.value = "default";
        }
    }

    // ==========================================================================
    // Tests for getAccessibleConstructor(Class, Class...)
    // ==========================================================================

    @Test
    public void getAccessibleConstructor_forPublicConstructor_shouldReturnConstructor() {
        Constructor<TestBean> constructor = ConstructorUtils.getAccessibleConstructor(TestBean.class, String.class);
        assertNotNull("Public constructor should be found", constructor);
    }

    @Test
    public void getAccessibleConstructor_forInaccessibleConstructor_shouldReturnNull() {
        // The constructor TestBean(int) is private and should not be returned.
        Constructor<TestBean> constructor = ConstructorUtils.getAccessibleConstructor(TestBean.class, int.class);
        assertNull("Private constructor should not be found", constructor);
    }

    @Test
    public void getAccessibleConstructor_forNonExistentConstructor_shouldReturnNull() {
        Constructor<String> constructor = ConstructorUtils.getAccessibleConstructor(String.class, Double.class);
        assertNull("A constructor with non-existent parameter types should not be found", constructor);
    }

    @Test(expected = NullPointerException.class)
    public void getAccessibleConstructor_withNullClass_shouldThrowNullPointerException() {
        ConstructorUtils.getAccessibleConstructor(null, String.class);
    }

    // ==========================================================================
    // Tests for getAccessibleConstructor(Constructor)
    // ==========================================================================

    @Test
    public void getAccessibleConstructor_forAlreadyPublicConstructor_shouldReturnSameInstance() throws Exception {
        Constructor<Object> constructor = Object.class.getConstructor();
        assertTrue("Constructor should already be accessible", constructor.isAccessible());

        Constructor<Object> result = ConstructorUtils.getAccessibleConstructor(constructor);
        assertSame("Should return the same constructor instance", constructor, result);
    }

    @Test
    public void getAccessibleConstructor_forPrivateConstructor_shouldReturnAccessibleInstance() throws Exception {
        // This test relies on the security context allowing setAccessible(true).
        Constructor<TestBean> privateConstructor = TestBean.class.getDeclaredConstructor(int.class);
        assertFalse("Constructor should initially be inaccessible", privateConstructor.isAccessible());

        Constructor<TestBean> accessibleConstructor = ConstructorUtils.getAccessibleConstructor(privateConstructor);
        assertNotNull("An accessible version of the constructor should be returned", accessibleConstructor);
        assertTrue("Constructor should now be accessible", accessibleConstructor.isAccessible());
    }

    @Test(expected = NullPointerException.class)
    public void getAccessibleConstructor_withNullConstructor_shouldThrowNullPointerException() {
        ConstructorUtils.getAccessibleConstructor(null);
    }

    // ==========================================================================
    // Tests for getMatchingAccessibleConstructor(Class, Class...)
    // ==========================================================================

    @Test
    public void getMatchingAccessibleConstructor_withExactMatch_shouldReturnConstructor() {
        Constructor<Integer> constructor = ConstructorUtils.getMatchingAccessibleConstructor(Integer.class, String.class);
        assertNotNull("Constructor with exact parameter match should be found", constructor);
    }

    @Test
    public void getMatchingAccessibleConstructor_withAssignableMatch_shouldReturnConstructor() {
        // Find a constructor for Number(Integer), which should match a constructor Number(int) via autoboxing.
        Constructor<Number> constructor = ConstructorUtils.getMatchingAccessibleConstructor(Number.class, Integer.class);
        assertNotNull("Constructor with assignable parameter types should be found", constructor);
    }

    @Test
    public void getMatchingAccessibleConstructor_forNonExistentConstructor_shouldReturnNull() {
        Constructor<String> constructor = ConstructorUtils.getMatchingAccessibleConstructor(String.class, Double.class);
        assertNull("Should not find a matching constructor", constructor);
    }

    @Test(expected = NullPointerException.class)
    public void getMatchingAccessibleConstructor_withNullClass_shouldThrowNullPointerException() {
        ConstructorUtils.getMatchingAccessibleConstructor(null, String.class);
    }

    // ==========================================================================
    // Tests for invokeConstructor(Class, args...)
    // ==========================================================================

    @Test
    public void invokeConstructor_withNoArgs_shouldCreateInstance() throws Exception {
        Object instance = ConstructorUtils.invokeConstructor(Object.class);
        assertNotNull("Instance should be created", instance);
    }

    @Test
    public void invokeConstructor_withArgs_shouldCreateInstance() throws Exception {
        String instance = ConstructorUtils.invokeConstructor(String.class, "test");
        assertEquals("Instance should be initialized correctly", "test", instance);
    }

    @Test(expected = NoSuchMethodException.class)
    public void invokeConstructor_forNonExistentConstructor_shouldThrowNoSuchMethodException() throws Exception {
        // Object has no constructor that takes a String.
        ConstructorUtils.invokeConstructor(Object.class, "some-arg");
    }

    @Test(expected = InvocationTargetException.class)
    public void invokeConstructor_whenUnderlyingConstructorThrowsException_shouldThrowInvocationTargetException() throws Exception {
        // The Integer(String) constructor throws NumberFormatException for invalid input.
        ConstructorUtils.invokeConstructor(Integer.class, "not a number");
    }

    @Test(expected = NullPointerException.class)
    public void invokeConstructor_withNullClass_shouldThrowNullPointerException() throws Exception {
        ConstructorUtils.invokeConstructor(null, "arg1");
    }

    // ==========================================================================
    // Tests for invokeExactConstructor(Class, args...)
    // ==========================================================================

    @Test
    public void invokeExactConstructor_withExactArgs_shouldCreateInstance() throws Exception {
        String instance = ConstructorUtils.invokeExactConstructor(String.class, "test");
        assertEquals("Instance should be initialized correctly", "test", instance);
    }

    @Test(expected = NoSuchMethodException.class)
    public void invokeExactConstructor_withAssignableButNotExactType_shouldThrowNoSuchMethodException() throws Exception {
        // There is no constructor for Integer(Object), so an exact match fails.
        // The assignable-match method `invokeConstructor` would find Integer(String).
        ConstructorUtils.invokeExactConstructor(Integer.class, new Object());
    }

    @Test(expected = NullPointerException.class)
    public void invokeExactConstructor_withNullClass_shouldThrowNullPointerException() throws Exception {
        ConstructorUtils.invokeExactConstructor(null, "arg1");
    }

    // ==========================================================================
    // Tests for invokeConstructor with explicit parameter types
    // ==========================================================================

    @Test
    public void invokeConstructor_withExplicitTypes_shouldCreateInstance() throws Exception {
        Object instance = ConstructorUtils.invokeConstructor(Object.class, new Object[0], new Class<?>[0]);
        assertNotNull("Instance should be created", instance);
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void invokeConstructor_onInterface_shouldThrowNoSuchMethodException() throws Exception {
        // Annotation is an interface and cannot be instantiated.
        ConstructorUtils.invokeConstructor(Annotation.class, new Object[0], new Class<?>[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invokeConstructor_withMismatchedArgsAndParamTypes_shouldThrowIllegalArgumentException() throws Exception {
        Object[] args = {}; // 0 arguments
        Class<?>[] paramTypes = {String.class}; // 1 parameter type
        ConstructorUtils.invokeConstructor(String.class, args, paramTypes);
    }

    // ==========================================================================
    // Test for utility class constructor
    // ==========================================================================

    @Test
    public void constructor_shouldBePublicForBeanCompatibility() {
        // The constructor is public to allow instantiation by tools,
        // but the class consists of static methods and is not intended
        // to be instantiated in normal use.
        ConstructorUtils instance = new ConstructorUtils();
        assertNotNull("Instance should be created", instance);
    }
}