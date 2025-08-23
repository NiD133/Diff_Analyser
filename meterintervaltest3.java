package org.jfree.chart.plot;

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A collection of tests for the {@link MeterInterval} class, focusing on serialization.
 */
class MeterIntervalTest {

    /**
     * Verifies that a MeterInterval instance can be serialized and deserialized,
     * preserving its state. This test uses a constructor that sets default
     * values for paint and stroke.
     */
    @Test
    void serialization_withDefaultProperties_shouldPreserveState() {
        // Arrange
        MeterInterval originalInterval = new MeterInterval("Normal", new Range(1.0, 2.0));

        // Act
        MeterInterval deserializedInterval = TestUtils.serialised(originalInterval);

        // Assert
        assertEquals(originalInterval, deserializedInterval);
    }

    /**
     * Verifies that a MeterInterval instance with custom paint and stroke
     * is correctly serialized and deserialized. This is important because the
     * paint and stroke fields are transient and require custom serialization logic.
     */
    @Test
    void serialization_withCustomProperties_shouldPreserveState() {
        // Arrange
        String label = "High";
        Range range = new Range(80.0, 100.0);
        Paint outlinePaint = Color.RED;
        Stroke outlineStroke = new BasicStroke(3.0f);
        Paint backgroundPaint = new Color(255, 200, 200);

        MeterInterval originalInterval = new MeterInterval(label, range, outlinePaint, outlineStroke, backgroundPaint);

        // Act
        MeterInterval deserializedInterval = TestUtils.serialised(originalInterval);

        // Assert
        assertEquals(originalInterval, deserializedInterval);
    }
}