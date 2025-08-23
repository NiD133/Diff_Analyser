package org.apache.commons.lang3.reflect;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Constructor;
import java.util.stream.Stream;
import org.apache.commons.lang3.AbstractLangTest;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link ConstructorUtils#getMatchingAccessibleConstructor(Class, Class...)}.
 */
public class ConstructorUtilsTest extends AbstractLangTest {

    // --- Test Fixtures ---

    private static class BaseClass {
    }

    private static final class SubClass extends BaseClass {
    }

    @SuppressWarnings("unused")
    static class PrivateClass {
        public static class PublicInnerClass {
            public PublicInnerClass() {
            }
        }
        public PrivateClass() {
        }
    }

    public static class TestBean {
        public TestBean() {}
        public TestBean(final String s) {}
        public TestBean(final Integer i) {}
        public TestBean(final int i) {}
        public TestBean(final double d) {}
        public TestBean(final Object o) {}
        public TestBean(final BaseClass bc, final String... s) {}
        public TestBean(final Integer first, final int... args) {}
        public TestBean(final Integer i, final String... s) {}
        public TestBean(final String... s) {}
    }

    // --- Test Data Provider ---

    private static Stream<Arguments> getMatchingAccessibleConstructorParams() {
        return Stream.of(
            // Scenario: No-arg constructor
            Arguments.of("No-arg constructor", null, ArrayUtils.EMPTY_CLASS_ARRAY),
            Arguments.of("No-arg constructor with empty array", ArrayUtils.EMPTY_CLASS_ARRAY, ArrayUtils.EMPTY_CLASS_ARRAY),

            // Scenario: Exact matches
            Arguments.of("Exact match with String", new Class<?>[]{String.class}, new Class<?>[]{String.class}),
            Arguments.of("Exact match with Integer", new Class<?>[]{Integer.class}, new Class<?>[]{Integer.class}),
            Arguments.of("Exact match with int", new Class<?>[]{Integer.TYPE}, new Class<?>[]{Integer.TYPE}),
            Arguments.of("Exact match with Object", new Class<?>[]{Object.class}, new Class<?>[]{Object.class}),
            Arguments.of("Exact match with double", new Class<?>[]{Double.TYPE}, new Class<?>[]{Double.TYPE}),

            // Scenario: Superclass matching
            Arguments.of("Superclass match (Boolean -> Object)", new Class<?>[]{Boolean.class}, new Class<?>[]{Object.class}),

            // Scenario: Primitive widening (e.g., byte -> int)
            Arguments.of("Widening from byte to int", new Class<?>[]{Byte.TYPE}, new Class<?>[]{Integer.TYPE}),
            Arguments.of("Widening from short to int", new Class<?>[]{Short.TYPE}, new Class<?>[]{Integer.TYPE}),
            Arguments.of("Widening from char to int", new Class<?>[]{Character.TYPE}, new Class<?>[]{Integer.TYPE}),
            Arguments.of("Widening from long to double", new Class<?>[]{Long.TYPE}, new Class<?>[]{Double.TYPE}),
            Arguments.of("Widening from float to double", new Class<?>[]{Float.TYPE}, new Class<?>[]{Double.TYPE}),

            // Scenario: Autoboxing followed by widening (e.g., Byte -> int)
            Arguments.of("Autoboxing/Widening from Byte to int", new Class<?>[]{Byte.class}, new Class<?>[]{Integer.TYPE}),
            Arguments.of("Autoboxing/Widening from Short to int", new Class<?>[]{Short.class}, new Class<?>[]{Integer.TYPE}),
            Arguments.of("Autoboxing/Widening from Character to int", new Class<?>[]{Character.class}, new Class<?>[]{Integer.TYPE}),
            Arguments.of("Autoboxing/Widening from Long to double", new Class<?>[]{Long.class}, new Class<?>[]{Double.TYPE}),
            Arguments.of("Autoboxing/Widening from Float to double", new Class<?>[]{Float.class}, new Class<?>[]{Double.TYPE}),
            Arguments.of("Autoboxing/Widening from Double to double", new Class<?>[]{Double.class}, new Class<?>[]{Double.TYPE}),

            // Scenario: Inheritance and varargs (represented as an array)
            Arguments.of("Inheritance and array", new Class<?>[]{SubClass.class, String[].class}, new Class<?>[]{BaseClass.class, String[].class})
        );
    }

    // --- Test Method ---

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("getMatchingAccessibleConstructorParams")
    void getMatchingAccessibleConstructorFindsCorrectConstructor(final String scenario, final Class<?>[] requestTypes, final Class<?>[] expectedParamTypes) {
        // Act
        final Constructor<TestBean> constructor = ConstructorUtils.getMatchingAccessibleConstructor(TestBean.class, requestTypes);

        // Assert
        assertNotNull(constructor, "Constructor should not be null for scenario: " + scenario);
        assertArrayEquals(expectedParamTypes, constructor.getParameterTypes(), "Incorrect constructor parameter types found");
    }
}