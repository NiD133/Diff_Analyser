package org.jfree.chart.urls;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link CustomCategoryURLGenerator} class.
 */
public class CustomCategoryURLGeneratorTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * An instance of CustomCategoryURLGenerator should always be equal to itself.
     */
    @Test
    public void equals_shouldReturnTrue_whenComparingAnObjectToItself() {
        // Arrange
        CustomCategoryURLGenerator generator = new CustomCategoryURLGenerator();

        // Act & Assert
        // According to the equals() contract, an object must be equal to itself.
        assertTrue("An object should be equal to itself.", generator.equals(generator));
    }
}