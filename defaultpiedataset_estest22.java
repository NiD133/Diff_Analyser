package org.jfree.data.general;

import org.jfree.data.KeyedValues;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the constructor of the {@link DefaultPieDataset} class.
 */
public class DefaultPieDatasetConstructorTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException when the
     * source KeyedValues object is null. The public contract of the constructor
     * is to not accept a null source.
     */
    @Test
    public void constructorWithNullSourceShouldThrowIllegalArgumentException() {
        // Arrange: Define a null source for the dataset.
        KeyedValues<String> nullSource = null;
        String expectedMessage = "Null 'source' argument.";

        // Act & Assert: Attempt to create the dataset and verify the exception.
        try {
            new DefaultPieDataset<>(nullSource);
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            // Assert that the exception message is correct.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}