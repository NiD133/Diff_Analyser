package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link JsonIncludeProperties.Value} class, which encapsulates
 * the configuration for which properties to include during serialization or deserialization.
 */
class JsonIncludePropertiesValueTest {

    // The canonical instance representing that all properties are to be included.
    private final JsonIncludeProperties.Value ALL_PROPERTIES = JsonIncludeProperties.Value.all();

    /**
     * This test verifies the contract of the special "ALL" instance, which is used
     * as the default to indicate that no properties should be filtered.
     */
    @Test
    void testStaticAllInstanceContract() {
        // The factory method `from(null)` is expected to return the canonical "ALL" instance.
        assertSame(ALL_PROPERTIES, JsonIncludeProperties.Value.from(null),
                "The factory method from(null) should return the singleton 'ALL' instance.");

        // The contract for the "ALL" instance is that its set of included properties is null.
        // This null signifies "include all" as opposed to an empty set which means "include none".
        assertNull(ALL_PROPERTIES.getIncluded(),
                "getIncluded() should return null to signify all properties are included.");

        // Verify the standard object methods for consistency and easier debugging.
        assertEquals("JsonIncludeProperties.Value(included=null)", ALL_PROPERTIES.toString(),
                "toString() should accurately reflect the 'all included' state.");

        // The hashCode is defined to be 0 for the "ALL" instance.
        assertEquals(0, ALL_PROPERTIES.hashCode(),
                "hashCode() for the 'ALL' instance should be consistent and well-defined.");

        // The "ALL" instance should be equal to itself and any other reference to it.
        // This also implicitly tests the equals() method's handling of the singleton.
        assertEquals(ALL_PROPERTIES, JsonIncludeProperties.Value.all());
    }
}