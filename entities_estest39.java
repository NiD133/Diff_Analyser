package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link Entities} class.
 */
public class EntitiesTest {

    /**
     * Verifies that calling {@link Entities#getByName(String)} with an empty string
     * as input returns an empty string, as an unknown entity should.
     */
    @Test
    public void getByNameShouldReturnEmptyStringForEmptyInput() {
        // Arrange
        final String emptyEntityName = "";
        final String expectedResult = "";

        // Act
        final String actualResult = Entities.getByName(emptyEntityName);

        // Assert
        assertEquals(expectedResult, actualResult);
    }
}