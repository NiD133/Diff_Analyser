package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Entities} class.
 * This focuses on the {@link Entities#isBaseNamedEntity(String)} method.
 */
public class EntitiesTest {

    /**
     * Verifies that isBaseNamedEntity() correctly identifies a valid, known base entity name.
     * The entity "not" (&amp;not;) is a standard base HTML entity and should be recognized.
     */
    @Test
    public void isBaseNamedEntityShouldReturnTrueForValidBaseEntity() {
        // Arrange: Define a known base entity name.
        String validBaseEntityName = "not";

        // Act: Call the method under test.
        boolean isBaseEntity = Entities.isBaseNamedEntity(validBaseEntityName);

        // Assert: The method should return true for a valid base entity.
        assertTrue("The entity 'not' should be recognized as a base named entity.", isBaseEntity);
    }
}