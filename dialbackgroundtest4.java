package org.jfree.chart.plot.dial;

import org.jfree.chart.TestUtils;
import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.GradientPaint;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A collection of serialization tests for the {@link DialBackground} class.
 */
@DisplayName("DialBackground Serialization")
class DialBackgroundSerializationTest {

    /**
     * Verifies that a default DialBackground instance can be serialized and
     * deserialized correctly, maintaining equality with the original object.
     */
    @Test
    @DisplayName("A default instance should be serializable")
    void serializationOfDefaultInstance_shouldPreserveEquality() {
        // Arrange: Create a default DialBackground instance.
        DialBackground originalBackground = new DialBackground();

        // Act: Serialize and then deserialize the instance.
        DialBackground deserializedBackground = TestUtils.serialised(originalBackground);

        // Assert: The deserialized object should be equal to the original.
        assertEquals(originalBackground, deserializedBackground,
                "Deserialized default instance should be equal to the original.");
    }

    /**
     * Verifies that a customized DialBackground instance can be serialized and
     * deserialized correctly, maintaining equality with the original object.
     */
    @Test
    @DisplayName("A customized instance should be serializable")
    void serializationOfCustomizedInstance_shouldPreserveEquality() {
        // Arrange: Create a DialBackground instance with custom properties.
        DialBackground originalBackground = new DialBackground();
        originalBackground.setPaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.GREEN));
        originalBackground.setGradientPaintTransformer(
                new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));

        // Act: Serialize and then deserialize the instance.
        DialBackground deserializedBackground = TestUtils.serialised(originalBackground);

        // Assert: The deserialized object should be equal to the original.
        assertEquals(originalBackground, deserializedBackground,
                "Deserialized customized instance should be equal to the original.");
    }
}