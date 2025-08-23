package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link JacksonInject.Value}.
 */
public class JacksonInjectValueTest {

    @Test
    public void hasId_shouldReturnFalse_whenConstructedWithNullId() {
        // Arrange: Create a JacksonInject.Value instance where the ID is null.
        // The 'useInput' and 'optional' parameters are not relevant to this test.
        JacksonInject.Value injectValue = JacksonInject.Value.construct(null, null, true);

        // Act & Assert: Verify that hasId() correctly reports that no ID is present.
        assertFalse("hasId() should return false for a null ID", injectValue.hasId());
    }
}