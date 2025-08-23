package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Entities} class.
 */
public class EntitiesTest {

    /**
     * Verifies that escaping text with cloned OutputSettings produces identical results
     * to each other and to the original settings object. This ensures that the
     * OutputSettings.clone() method correctly copies all state relevant to escaping.
     */
    @Test
    public void escapeWithClonedOutputSettingsShouldProduceIdenticalResult() {
        // Arrange
        String textToEscape = "Hello &<> Å å π 新 there ¾ © »";
        OutputSettings originalSettings = new OutputSettings();

        // Create two separate clones from the original settings
        OutputSettings clonedSettings1 = originalSettings.clone();
        OutputSettings clonedSettings2 = originalSettings.clone();

        // Act
        // Escape the text using the original and both cloned settings
        String escapedWithOriginal = Entities.escape(textToEscape, originalSettings);
        String escapedWithClone1 = Entities.escape(textToEscape, clonedSettings1);
        String escapedWithClone2 = Entities.escape(textToEscape, clonedSettings2);

        // Assert
        // The results from the clones should be identical to the original's result.
        assertEquals(escapedWithOriginal, escapedWithClone1, "Result from first clone should match original");
        assertEquals(escapedWithOriginal, escapedWithClone2, "Result from second clone should match original");
    }
}