package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Entities#escape(String, Document.OutputSettings)} method.
 */
public class EntitiesEscapeTest {

    @Test
    public void escapeShouldConvertNonBreakingSpaceToNbspEntity() {
        // Arrange
        // The input string contains "yen" followed by a non-breaking space character (\u00A0).
        String inputWithNonBreakingSpace = "yen\u00A0";
        String expectedOutput = "yen&nbsp;";
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        // Act
        String actualOutput = Entities.escape(inputWithNonBreakingSpace, outputSettings);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
}