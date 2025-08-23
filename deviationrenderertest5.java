package org.jfree.chart.renderer.xy;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Serialization tests for the {@link DeviationRenderer} class.
 */
@DisplayName("DeviationRenderer Serialization")
class DeviationRendererSerializationTest {

    @Test
    @DisplayName("A default instance should be serializable")
    void defaultInstanceShouldBeSerializable() {
        // Arrange
        DeviationRenderer originalRenderer = new DeviationRenderer();

        // Act
        DeviationRenderer deserializedRenderer = TestUtils.serialised(originalRenderer);

        // Assert
        assertEquals(originalRenderer, deserializedRenderer, "A deserialized renderer should be equal to the original.");
    }
}