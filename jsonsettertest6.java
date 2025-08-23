package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link JsonSetter.Value} class, focusing on its
 * immutable "with" methods.
 */
@DisplayName("JsonSetter.Value")
class JsonSetterValueTest {

    @Test
    @DisplayName("should create a new instance with updated properties when using 'with' methods")
    void withMethodsShouldCreateNewInstanceWithUpdatedProperties() {
        // Arrange: Start with an empty instance, which has default null-handling settings.
        JsonSetter.Value initialValue = JsonSetter.Value.empty();

        // Act: Create a new instance by setting a specific content nulls policy.
        JsonSetter.Value intermediateValue = initialValue.withContentNulls(Nulls.SKIP);

        // Assert: The new instance reflects the change, while other properties remain default.
        assertEquals(Nulls.SKIP, intermediateValue.getContentNulls());
        assertEquals(Nulls.DEFAULT, intermediateValue.getValueNulls());

        // Act: Create a final instance by setting the value nulls policy on the intermediate instance.
        JsonSetter.Value finalValue = intermediateValue.withValueNulls(Nulls.FAIL);

        // Assert: The final instance has the new value nulls policy AND retains the
        // previously set content nulls policy, demonstrating immutability.
        assertEquals(Nulls.FAIL, finalValue.getValueNulls(),
                "Value nulls should be updated to FAIL");
        assertEquals(Nulls.SKIP, finalValue.getContentNulls(),
                "Content nulls should be retained as SKIP");
    }
}