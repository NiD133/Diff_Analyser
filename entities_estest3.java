package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Entities} class.
 */
public class EntitiesTest {

    /**
     * Verifies that the unescape method returns the original string
     * when the input contains no HTML entities to unescape.
     */
    @Test
    public void unescape_withNoEntities_returnsOriginalString() {
        // Arrange
        String inputWithNoEntities = "FX{u";

        // Act
        // The 'strict' parameter is true, meaning entities must end with a semicolon.
        String result = Entities.unescape(inputWithNoEntities, true);

        // Assert
        assertEquals("The string should remain unchanged as it contains no entities.", inputWithNoEntities, result);
    }
}