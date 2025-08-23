package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test suite focuses on the {@link JacksonInject.Value} class,
 * verifying the behavior of its factory and mutant methods.
 */
public class JacksonInjectValueTest {

    /**
     * Tests that the withId() method correctly creates a new Value instance
     * with the specified ID, and that the hasId() method reflects this change.
     */
    @Test
    public void withId_should_createNewInstanceWithId() {
        // Arrange: Start with an empty Value instance, which should not have an ID.
        JacksonInject.Value initialValue = JacksonInject.Value.empty();
        assertFalse("Precondition: The empty value should not have an ID.", initialValue.hasId());

        final Object expectedId = "test-id";

        // Act: Call withId() to create a new instance with an ID.
        JacksonInject.Value valueWithId = initialValue.withId(expectedId);

        // Assert: The new instance should have the correct ID, and the original should be unchanged.
        assertTrue("The new instance should report that it has an ID.", valueWithId.hasId());
        assertEquals("The ID should match the one provided.", expectedId, valueWithId.getId());

        // Verify immutability: the original instance should not have been modified.
        assertFalse("The original empty value should remain unchanged.", initialValue.hasId());
    }
}