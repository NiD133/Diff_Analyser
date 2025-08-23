package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link Entities} class.
 */
public class EntitiesTest {

    /**
     * Verifies that isNamedEntity() returns false for a string that is not a valid HTML entity.
     */
    @Test
    public void isNamedEntityReturnsFalseForNonExistentEntity() {
        // "US-ASCII" is a common string (a charset name) but not a named HTML entity.
        // The method should correctly identify it as such.
        boolean isEntity = Entities.isNamedEntity("US-ASCII");
        assertFalse(isEntity);
    }
}