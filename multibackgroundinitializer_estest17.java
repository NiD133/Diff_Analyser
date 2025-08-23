package org.apache.commons.lang3.concurrent;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains an improved version of a test for {@link MultiBackgroundInitializer}.
 * The original test was auto-generated and has been refactored for better understandability.
 */
public class MultiBackgroundInitializer_ESTestTest17 extends MultiBackgroundInitializer_ESTest_scaffolding {

    /**
     * Tests that getResultObject() throws a NoSuchElementException when called with a name
     * that does not correspond to any registered child initializer.
     */
    @Test(timeout = 4000)
    public void getResultObjectShouldThrowNoSuchElementExceptionForUnknownInitializer() throws Exception {
        // Arrange: Create a MultiBackgroundInitializer without any child initializers
        // and perform the initialization to get the results object.
        MultiBackgroundInitializer initializer = new MultiBackgroundInitializer();
        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = initializer.initialize();
        String unknownInitializerName = "nonExistentInitializer";

        // Act & Assert: Attempt to get a result for an unknown name and verify that the
        // correct exception is thrown with the expected message.
        try {
            results.getResultObject(unknownInitializerName);
            fail("Expected a NoSuchElementException to be thrown for an unknown initializer name.");
        } catch (NoSuchElementException e) {
            // Verify the exception message is as expected.
            String expectedMessage = "No child initializer with name " + unknownInitializerName;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}