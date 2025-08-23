package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Entities} class.
 */
public class EntitiesTest {

    /**
     * Verifies that {@link Entities#isNamedEntity(String)} correctly identifies a valid,
     * known HTML named entity.
     */
    @Test
    public void isNamedEntity_withValidEntityName_returnsTrue() {
        // Arrange: "Racute" is a valid HTML named entity for the character 'Ŕ'.
        String validEntityName = "Racute";

        // Act: Call the method under test.
        boolean isEntity = Entities.isNamedEntity(validEntityName);

        // Assert: The method should return true for a valid entity name.
        assertTrue("'" + validEntityName + "' should be recognized as a valid named entity.", isEntity);
    }
}