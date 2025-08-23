package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Unit tests for {@link org.apache.commons.lang3.SerializationUtils}.
 */
public class SerializationUtilsTest {

    /**
     * Tests that cloning a serializable object results in a new object
     * that is equal in value but is not the same instance.
     */
    @Test
    public void clone_withSerializableObject_returnsEqualButNotSameInstance() {
        // Arrange: Create a serializable object to be cloned.
        final Integer original = 114;

        // Act: Clone the object using the utility method.
        final Integer cloned = SerializationUtils.clone(original);

        // Assert: Verify the properties of the cloned object.
        // 1. The cloned object should have the same value as the original.
        assertEquals(original, cloned);
        
        // 2. The cloned object should be a different instance from the original.
        assertNotSame(original, cloned);
    }
}