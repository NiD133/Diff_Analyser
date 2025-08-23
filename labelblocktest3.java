package org.jfree.chart.block;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A test suite for the serialization of the {@link LabelBlock} class.
 */
@DisplayName("LabelBlock Serialization")
class LabelBlockSerializationTest {

    @Test
    @DisplayName("A LabelBlock instance should be correctly serialized and deserialized")
    void labelBlockShouldBeSerializable() {
        // Arrange: Create a LabelBlock with a non-default Paint to test
        // the custom serialization logic for the transient 'paint' field.
        Paint paint = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.BLUE);
        Font font = new Font("Dialog", Font.PLAIN, 12);
        LabelBlock originalBlock = new LabelBlock("ABC", font, paint);

        // Act: Serialize and then deserialize the block.
        LabelBlock deserializedBlock = TestUtils.serialised(originalBlock);

        // Assert: The deserialized block should be equal to the original.
        assertEquals(originalBlock, deserializedBlock);
    }
}