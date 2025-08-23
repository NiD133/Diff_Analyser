package org.jfree.chart.axis;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * Tests for the serialization of the {@link CategoryLabelPosition} class.
 */
class CategoryLabelPositionTest {

    @Test
    void defaultInstanceShouldBeSerializable() {
        // Arrange: Create a default instance of the class under test.
        CategoryLabelPosition original = new CategoryLabelPosition();

        // Act: Serialize the original object and then deserialize it into a new object.
        CategoryLabelPosition restored = TestUtils.serialised(original);

        // Assert: The restored object should be a new, but equal, instance.
        assertEquals(original, restored, "The restored object should be equal to the original.");
        assertNotSame(original, restored, "Serialization should produce a new object instance.");
    }
}