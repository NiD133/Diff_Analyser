package org.jfree.chart.renderer.xy;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the serialization of the {@link ClusteredXYBarRenderer} class.
 */
class ClusteredXYBarRendererTest {

    /**
     * Verifies that a default renderer, after serialization and deserialization,
     * remains equal to the original instance.
     */
    @Test
    void serializationOfDefaultInstanceShouldPreserveEquality() {
        // Arrange: Create a default renderer instance.
        ClusteredXYBarRenderer originalRenderer = new ClusteredXYBarRenderer();

        // Act: Serialize and then deserialize the renderer.
        ClusteredXYBarRenderer deserializedRenderer = TestUtils.serialised(originalRenderer);

        // Assert: The deserialized renderer should be equal to the original.
        assertEquals(originalRenderer, deserializedRenderer);
    }
}