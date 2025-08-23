package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Entities} class.
 */
public class EntitiesTest {

    /**
     * Verifies that getByName() correctly translates the "nbsp" named entity
     * to its corresponding non-breaking space character.
     */
    @Test
    public void getByNameShouldReturnNonBreakingSpaceForNbspEntity() {
        // Arrange
        String entityName = "nbsp";
        String expectedCharacter = "\u00A0"; // The non-breaking space character

        // Act
        String actualCharacter = Entities.getByName(entityName);

        // Assert
        assertEquals(
            "The named entity 'nbsp' should be converted to a non-breaking space character.",
            expectedCharacter,
            actualCharacter
        );
    }
}