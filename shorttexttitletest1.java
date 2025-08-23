package org.jfree.chart.title;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for the {@link ShortTextTitle} class, focusing on the equals() method.
 */
@DisplayName("ShortTextTitle equals()")
public class ShortTextTitleTest {

    @Test
    @DisplayName("Should correctly reflect changes in the text property")
    void equals_shouldCorrectlyReflectChangesInTextProperty() {
        // Arrange: Create two identical ShortTextTitle objects.
        ShortTextTitle title1 = new ShortTextTitle("ABC");
        ShortTextTitle title2 = new ShortTextTitle("ABC");

        // Assert: Initially, the two titles should be equal.
        assertEquals(title1, title2, "Two titles with the same initial text should be equal.");

        // Act: Change the text of the first title.
        title1.setText("Test 1");

        // Assert: The titles should now be unequal.
        assertNotEquals(title1, title2, "Titles should not be equal after changing one's text.");

        // Act: Change the text of the second title to match the first.
        title2.setText("Test 1");

        // Assert: The titles should be equal again.
        assertEquals(title1, title2, "Titles should be equal again once their texts match.");
    }
}