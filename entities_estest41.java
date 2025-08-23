package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link Entities} class, focusing on entity name validation.
 */
public class EntitiesTest {

    /**
     * Verifies that {@link Entities#isBaseNamedEntity(String)} returns false
     * when given a string that is not a valid base named entity.
     */
    @Test
    public void isBaseNamedEntityShouldReturnFalseForNonExistentEntityName() {
        // Arrange: A string that does not correspond to a known base HTML entity.
        String nonExistentEntityName = "InTemplate";

        // Act: Check if the string is a base named entity.
        boolean isBaseEntity = Entities.isBaseNamedEntity(nonExistentEntityName);

        // Assert: The method should correctly identify the string as not a base entity.
        assertFalse("Expected isBaseNamedEntity to return false for a non-existent entity name.", isBaseEntity);
    }
}