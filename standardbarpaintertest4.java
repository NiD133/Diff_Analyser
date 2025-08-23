package org.jfree.chart.renderer.category;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the serialization of the {@link StandardBarPainter} class.
 */
@DisplayName("StandardBarPainter")
class StandardBarPainterTest {

    @Test
    @DisplayName("should be serializable")
    void serialization_ofDefaultInstance_preservesEquality() {
        // Arrange
        StandardBarPainter originalPainter = new StandardBarPainter();

        // Act
        StandardBarPainter deserializedPainter = (StandardBarPainter) TestUtils.serialised(originalPainter);

        // Assert
        assertEquals(originalPainter, deserializedPainter, "A deserialized painter should be equal to the original.");
    }
}