package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link JacksonInject.Value} class.
 */
public class JacksonInjectValueTest {

    /**
     * Verifies that the `forId()` factory method correctly creates a 
     * `JacksonInject.Value` instance that contains the specified ID.
     */
    @Test
    public void forIdShouldCreateValueWithId() {
        // Arrange: Define a clear and representative identifier.
        final Object injectionId = "test-injection-id";

        // Act: Create a Value instance using the factory method under test.
        JacksonInject.Value value = JacksonInject.Value.forId(injectionId);

        // Assert: Confirm that the instance correctly reports having an ID
        // and that the ID matches the one provided.
        assertTrue("The value should report that it has an ID.", value.hasId());
        assertEquals("The stored ID should match the one used for creation.", injectionId, value.getId());
    }
}