package com.fasterxml.jackson.annotation;

import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link JsonIncludeProperties.Value} class.
 */
public class JsonIncludePropertiesValueTest {

    /**
     * Verifies that the constant {@code JsonIncludeProperties.Value.ALL} returns a null set
     * for its included properties.
     * <p>
     * According to the class documentation, a null set from {@code getIncluded()} signifies
     * that all properties are included (i.e., no specific inclusion filter is defined). This
     * test confirms that the 'ALL' constant correctly represents this "not defined" state.
     */
    @Test
    public void shouldReturnNullForIncludedPropertiesWhenValueIsAll() {
        // Arrange: Get the predefined 'ALL' instance.
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL;

        // Act: Retrieve the set of included properties.
        Set<String> includedProperties = allValue.getIncluded();

        // Assert: The set should be null to indicate no specific properties are filtered.
        assertNull("The 'ALL' value should return a null set of included properties, " +
                   "indicating that no specific inclusion filter is applied.",
                   includedProperties);
    }
}