package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link JsonIgnoreProperties.Value}.
 */
public class JsonIgnorePropertiesValueTest {

    @Test
    public void withIgnored_whenGivenEmptyArray_shouldReturnEqualInstance() {
        // Arrange: Create an initial Value instance and an empty array of properties.
        JsonIgnoreProperties.Value initialValue = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        String[] noPropertiesToIgnore = new String[0];

        // Act: Call the method under test with the empty array.
        JsonIgnoreProperties.Value resultValue = initialValue.withIgnored(noPropertiesToIgnore);

        // Assert: The returned instance should be equal to the original,
        // as no new properties were ignored.
        assertEquals(initialValue, resultValue);

        // For completeness, explicitly verify the properties of the resulting value.
        assertTrue("ignoreUnknown should remain true", resultValue.getIgnoreUnknown());
        assertFalse("allowSetters should retain its default value of false", resultValue.getAllowSetters());
        assertFalse("allowGetters should retain its default value of false", resultValue.getAllowGetters());
        assertTrue("merge should retain its default value of true", resultValue.getMerge());
        assertTrue("The set of ignored properties should be empty", resultValue.getIgnored().isEmpty());
    }
}