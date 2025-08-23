package com.fasterxml.jackson.annotation;

import org.junit.Test;
import java.util.Collections;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link JsonIncludeProperties.Value} class.
 */
public class JsonIncludePropertiesValueTest {

    /**
     * Tests that creating a {@link JsonIncludeProperties.Value} from an annotation
     * whose 'value' attribute is null results in a configuration that includes
     * *no* properties (an empty set). This is distinct from the special
     * {@link JsonIncludeProperties.Value#ALL} instance, which includes *all* properties
     * (represented internally by a null set).
     */
    @Test
    public void fromAnnotationWithNullValueShouldCreateEmptyInclusionsNotAll() {
        // Arrange
        // 1. The special 'ALL' instance signifies including all properties.
        final JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL;

        // 2. A mock annotation where the 'value()' method returns null.
        //    The factory method should handle this by interpreting it as "include no properties".
        JsonIncludeProperties mockAnnotation = mock(JsonIncludeProperties.class);
        when(mockAnnotation.value()).thenReturn(null);

        // Act
        // Create a Value instance from the mock annotation.
        JsonIncludeProperties.Value valueFromNullArray = JsonIncludeProperties.Value.from(mockAnnotation);

        // Assert
        // The two Value instances should not be equal, as they represent different configurations.
        assertNotEquals(allValue, valueFromNullArray);

        // For clarity, verify the internal state of each object:
        // 'ALL' should have a null set of inclusions, meaning "include everything".
        assertNull("Value.ALL should represent all properties via a null set",
                allValue.getIncluded());

        // A value from a null array should have an empty set, meaning "include nothing".
        assertEquals("Value from a null array should represent no properties via an empty set",
                Collections.emptySet(), valueFromNullArray.getIncluded());
    }
}