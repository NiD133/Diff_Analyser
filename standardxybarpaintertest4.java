package org.jfree.chart.renderer.xy;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link StandardXYBarPainter} class, focusing on its serialization.
 */
class StandardXYBarPainterTest {

    /**
     * Verifies that an instance can be serialized and then deserialized,
     * resulting in an object that is equal to the original.
     * This is crucial for features like saving/loading charts.
     */
    @Test
    void shouldBeSerializable() {
        // Arrange: Create the original painter instance.
        StandardXYBarPainter originalPainter = new StandardXYBarPainter();

        // Act: Serialize and then deserialize the painter.
        StandardXYBarPainter deserializedPainter = TestUtils.serialised(originalPainter);

        // Assert: The deserialized painter should be equal to the original.
        assertEquals(originalPainter, deserializedPainter);
    }
}