package org.jfree.chart.block;

import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Font;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * Tests for the cloning functionality of the {@link LabelBlock} class.
 */
@DisplayName("LabelBlock Cloning")
class LabelBlockTest {

    /**
     * Verifies that cloning a LabelBlock creates a new instance that is
     * logically equal to the original but is a separate object in memory.
     */
    @Test
    @DisplayName("Should create an independent and equal copy")
    void clone_shouldCreateIndependentAndEqualCopy() throws CloneNotSupportedException {
        // Arrange: Create an original LabelBlock instance.
        final Font testFont = new Font("Dialog", Font.PLAIN, 12);
        final Color testColor = Color.RED;
        LabelBlock original = new LabelBlock("ABC", testFont, testColor);

        // Act: Create a clone of the original instance.
        LabelBlock clone = CloneUtils.clone(original);

        // Assert: The clone should be a different object but have the same content.
        assertAll("A cloned LabelBlock must be an independent but equal copy",
            () -> assertNotSame(original, clone, "The clone should be a new object instance."),
            () -> assertEquals(original, clone, "The clone should be logically equal to the original.")
        );
    }
}