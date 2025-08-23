package org.jfree.chart.annotations;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the serialization of the {@link XYLineAnnotation} class.
 */
@DisplayName("XYLineAnnotation Serialization")
class XYLineAnnotationTest {

    /**
     * Verifies that an XYLineAnnotation instance can be serialized and
     * deserialized without any loss of its state. The equals() method
     * provides a comprehensive check of all properties.
     */
    @Test
    @DisplayName("should be perfectly restored after serialization")
    void serialization_preservesObjectState() {
        // Arrange: Create an annotation with specific properties.
        Stroke stroke = new BasicStroke(2.0f);
        Paint paint = Color.BLUE;
        XYLineAnnotation originalAnnotation = new XYLineAnnotation(
                10.0, 20.0, 100.0, 200.0, stroke, paint
        );

        // Act: Serialize the original annotation and then deserialize it.
        XYLineAnnotation deserializedAnnotation = TestUtils.serialised(originalAnnotation);

        // Assert: The deserialized object should be identical to the original.
        assertEquals(originalAnnotation, deserializedAnnotation,
                "The deserialized annotation should be equal to the original.");
    }
}