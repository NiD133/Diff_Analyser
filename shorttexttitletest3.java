package org.jfree.chart.title;

import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the cloning functionality of the {@link ShortTextTitle} class.
 */
class ShortTextTitleTest {

    @Test
    @DisplayName("A cloned ShortTextTitle is an independent and equal copy")
    void clone_shouldCreateIndependentAndEqualCopy() throws CloneNotSupportedException {
        // Arrange: Create an original ShortTextTitle instance.
        ShortTextTitle original = new ShortTextTitle("Test Title");

        // Act: Clone the original instance.
        ShortTextTitle clone = CloneUtils.clone(original);

        // Assert: Verify that the clone is a new, independent object with the same state.
        assertAll("A cloned title must be an independent copy with equal state",
            () -> assertNotSame(original, clone, "Clone should be a new object instance."),
            () -> assertEquals(original, clone, "Clone should be equal to the original in content."),
            () -> assertSame(original.getClass(), clone.getClass(), "Clone should be of the exact same class.")
        );
    }
}