package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link JsonSetter.Value} class.
 */
public class JsonSetterValueTest {

    /**
     * Tests that creating a {@link JsonSetter.Value} from a null annotation
     * returns the default 'empty' instance.
     */
    @Test
    public void from_withNullAnnotation_shouldReturnDefaultInstance() {
        // Arrange: No setup needed, the input is null.

        // Act: Call the factory method with a null input.
        JsonSetter.Value value = JsonSetter.Value.from(null);

        // Assert: The returned instance should be the default singleton,
        // and its properties should have default values.
        assertSame("The default 'empty' instance should be returned for null input",
                JsonSetter.Value.empty(), value);
        assertEquals(Nulls.DEFAULT, value.getValueNulls());
        assertEquals(Nulls.DEFAULT, value.getContentNulls());
    }
}