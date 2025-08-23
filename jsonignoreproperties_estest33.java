package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test suite focuses on the {@link JsonIgnoreProperties.Value} class.
 */
public class JsonIgnoreProperties_ESTestTest33 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Tests that calling {@link JsonIgnoreProperties.Value#withoutIgnored()} on an instance
     * that was created without any specific properties to ignore results in an equal instance.
     * This verifies the method's behavior when there are no ignored properties to remove.
     */
    @Test
    public void withoutIgnored_whenNoPropertiesAreIgnored_returnsEqualInstance() {
        // Arrange: Create a Value instance with no specific properties to ignore,
        // but with 'ignoreUnknown' set to true.
        JsonIgnoreProperties.Value originalValue = JsonIgnoreProperties.Value.forIgnoreUnknown(true);

        // Act: Call the method under test, which should produce a new Value instance
        // with the set of ignored properties cleared.
        JsonIgnoreProperties.Value resultValue = originalValue.withoutIgnored();

        // Assert: The resulting value should be equal to the original, as there were
        // no ignored properties to remove in the first place.
        assertEquals(originalValue, resultValue);

        // Also, explicitly verify the properties of the resulting instance to be certain.
        assertTrue("Result should still have ignoreUnknown=true", resultValue.getIgnoreUnknown());
        assertTrue("Result should have no ignored properties", resultValue.getIgnored().isEmpty());
        assertFalse("Result should have allowGetters=false by default", resultValue.getAllowGetters());
        assertFalse("Result should have allowSetters=false by default", resultValue.getAllowSetters());
        assertTrue("Result should have merge=true by default", resultValue.getMerge());
    }
}