package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test suite focuses on the {@link JacksonInject.Value} class.
 * Note: The original test class name 'JacksonInject_ESTestTest13' was renamed for clarity.
 */
public class JacksonInjectValueTest {

    /**
     * Verifies that two JacksonInject.Value instances created using the same ID
     * are considered equal. It also confirms that the created instance correctly
     * reports that it has an ID.
     */
    @Test
    public void forId_whenCreatedWithSameId_shouldBeEqual() {
        // Arrange: Create a common object to use as an injection ID.
        Object injectionId = new Object();

        // Act: Create two JacksonInject.Value instances using the same ID.
        JacksonInject.Value value1 = JacksonInject.Value.forId(injectionId);
        JacksonInject.Value value2 = JacksonInject.Value.forId(injectionId);

        // Assert:
        // 1. The two instances should be equal to each other.
        assertEquals("Instances created with the same ID should be equal.", value1, value2);

        // 2. The instance should correctly report that it has an ID.
        assertTrue("Instance should confirm it has an ID.", value1.hasId());
    }
}