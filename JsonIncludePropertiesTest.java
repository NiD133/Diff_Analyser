package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test suite verifies the functionality of the {@link JsonIncludeProperties.Value} class.
 * It focuses on ensuring that instances can be created correctly and that the merging logic,
 * particularly for overrides, behaves as expected.
 */
public class JsonIncludePropertiesTest {

    // Test constants for property names to avoid magic strings.
    private static final String PROP_FOO = "foo";
    private static final String PROP_BAR = "bar";
    private static final Set<String> FOO_BAR_PROPS = Set.of(PROP_FOO, PROP_BAR);
    private static final Set<String> FOO_PROP = Set.of(PROP_FOO);

    /**
     * Defines a class with JsonIncludeProperties for testing purposes.
     */
    @JsonIncludeProperties({PROP_FOO, PROP_BAR})
    private static class TestBean {
    }

    private final JsonIncludeProperties.Value ALL_PROPERTIES = JsonIncludeProperties.Value.all();

    /**
     * Tests the contract of the 'ALL' singleton instance, which represents
     * that no specific properties are defined for inclusion.
     */
    @Test
    public void allInstance_shouldRepresentUndefinedAndBehaveAsSingleton() {
        assertSame(ALL_PROPERTIES, JsonIncludeProperties.Value.from(null),
                "Value.from(null) should return the singleton 'ALL' instance.");
        assertNull(ALL_PROPERTIES.getIncluded(),
                "The 'ALL' instance should have a null set of included properties.");
        assertEquals(ALL_PROPERTIES, ALL_PROPERTIES,
                "The 'ALL' instance should be equal to itself.");
        assertEquals(0, ALL_PROPERTIES.hashCode(),
                "The 'ALL' instance should have a consistent hashcode of 0.");
        assertEquals("JsonIncludeProperties.Value(included=null)", ALL_PROPERTIES.toString(),
                "The toString() representation should be as expected.");
    }

    /**
     * Verifies that a Value instance can be correctly created from an annotation,
     * capturing the specified properties.
     */
    @Test
    public void fromAnnotation_shouldExtractDefinedProperties() {
        JsonIncludeProperties annotation = TestBean.class.getAnnotation(JsonIncludeProperties.class);
        JsonIncludeProperties.Value valueFromAnnotation = JsonIncludeProperties.Value.from(annotation);

        assertNotNull(valueFromAnnotation);
        assertEquals(FOO_BAR_PROPS, valueFromAnnotation.getIncluded(),
                "The included properties should match those in the annotation.");

        // Verify that equals() and from() are consistent
        assertEquals(valueFromAnnotation, JsonIncludeProperties.Value.from(annotation),
                "Multiple calls to from() with the same annotation should produce equal Value objects.");
    }

    /**
     * Tests that when a Value is overridden by the 'ALL' instance (which signifies
     * "undefined"), the original Value remains unchanged.
     */
    @Test
    public void withOverrides_whenGivenAll_shouldReturnOriginalValue() {
        JsonIncludeProperties.Value initialValue = JsonIncludeProperties.Value.from(TestBean.class.getAnnotation(JsonIncludeProperties.class));
        JsonIncludeProperties.Value overriddenValue = initialValue.withOverrides(ALL_PROPERTIES);

        assertEquals(FOO_BAR_PROPS, overriddenValue.getIncluded(),
                "Overriding with 'ALL' should not change the included properties.");
    }

    /**
     * Tests that overriding a Value with a new Value containing an empty set of properties
     * results in a final Value with an empty set. This confirms the intersection logic.
     */
    @Test
    public void withOverrides_whenGivenEmptySet_shouldResultInEmptySet() {
        JsonIncludeProperties.Value initialValue = JsonIncludeProperties.Value.from(TestBean.class.getAnnotation(JsonIncludeProperties.class));
        JsonIncludeProperties.Value emptyOverride = new JsonIncludeProperties.Value(Collections.emptySet());
        JsonIncludeProperties.Value overriddenValue = initialValue.withOverrides(emptyOverride);

        assertTrue(overriddenValue.getIncluded().isEmpty(),
                "Overriding with an empty set should result in an empty set of properties.");
    }

    /**
     * Verifies that the override mechanism correctly calculates the intersection
     * of properties between the original and the overriding Value.
     */
    @Test
    public void withOverrides_whenGivenSubset_shouldResultInIntersection() {
        JsonIncludeProperties.Value initialValue = JsonIncludeProperties.Value.from(TestBean.class.getAnnotation(JsonIncludeProperties.class));
        JsonIncludeProperties.Value subsetOverride = new JsonIncludeProperties.Value(FOO_PROP);
        JsonIncludeProperties.Value overriddenValue = initialValue.withOverrides(subsetOverride);

        assertEquals(FOO_PROP, overriddenValue.getIncluded(),
                "The resulting properties should be the intersection of the original and the override.");
    }
}