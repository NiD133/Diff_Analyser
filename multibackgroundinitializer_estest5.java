package org.apache.commons.lang3.concurrent;

import org.junit.Test;
import java.util.NoSuchElementException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class focuses on the behavior of the MultiBackgroundInitializer.MultiBackgroundInitializerResults class.
 */
// Note: The original class name and inheritance are preserved to match the context.
// In a real-world scenario, these would likely be renamed for clarity.
public class MultiBackgroundInitializer_ESTestTest5 extends MultiBackgroundInitializer_ESTest_scaffolding {

    /**
     * Verifies that attempting to retrieve a result for an unknown initializer name
     * throws a NoSuchElementException.
     */
    @Test
    public void getResultObject_shouldThrowNoSuchElementException_whenInitializerNameIsUnknown() throws Exception {
        // Arrange
        final String knownInitializerName = "knownInitializer";
        final String unknownInitializerName = "unknownInitializer";

        MultiBackgroundInitializer multiInitializer = new MultiBackgroundInitializer();

        // Add a simple child initializer to set up the results object correctly.
        multiInitializer.addInitializer(knownInitializerName, new BackgroundInitializer<Object>() {
            @Override
            protected Object initialize() {
                return "Success";
            }
        });

        // Act: Perform the main initialization to get the results object.
        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();

        // Assert: Check for the expected exception when using an unknown name.
        try {
            results.getResultObject(unknownInitializerName);
            fail("Expected a NoSuchElementException to be thrown for an unknown initializer name.");
        } catch (NoSuchElementException e) {
            // Verify the exception message is informative and correct.
            String expectedMessage = "No child initializer with name " + unknownInitializerName;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}