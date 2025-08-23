package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JacksonInject} annotation and its inner {@link JacksonInject.Value} class.
 */
public class JacksonInjectTest {

    /**
     * Verifies that creating a {@link JacksonInject.Value} from a null annotation
     * returns the canonical empty instance, which correctly reports its 'optional'
     * property as null.
     */
    @Test
    public void testFrom_withNullAnnotation_shouldReturnEmptyValue() {
        // Arrange: The input is a null JacksonInject annotation.
        JacksonInject nullAnnotation = null;

        // Act: Create a Value instance from the null annotation.
        JacksonInject.Value result = JacksonInject.Value.from(nullAnnotation);

        // Assert: The result should be the canonical empty instance.
        assertSame("from(null) should return the singleton empty instance",
                JacksonInject.Value.empty(), result);

        // The empty instance should have null for all its properties.
        assertNull("The 'optional' property of an empty Value should be null", result.getOptional());
        assertNull("The 'id' property of an empty Value should be null", result.getId());
        assertNull("The 'useInput' property of an empty Value should be null", result.getUseInput());
    }
}