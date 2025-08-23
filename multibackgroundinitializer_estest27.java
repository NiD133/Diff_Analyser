package org.apache.commons.lang3.concurrent;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MultiBackgroundInitializer_ESTestTest27 extends MultiBackgroundInitializer_ESTest_scaffolding {

    /**
     * Tests that attempting to retrieve an exception for a non-existent child initializer
     * throws a NoSuchElementException.
     */
    @Test(timeout = 4000)
    public void getExceptionForUnknownInitializerShouldThrowNoSuchElementException() throws Exception {
        // Arrange: Create a MultiBackgroundInitializer with no child initializers and get the results.
        // Using the no-arg constructor is cleaner than passing a null ExecutorService.
        MultiBackgroundInitializer multiInitializer = new MultiBackgroundInitializer();
        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();
        final String unknownInitializerName = "initializer";

        // Act & Assert: Attempt to get an exception for an initializer that was never added.
        try {
            results.getException(unknownInitializerName);
            fail("Expected a NoSuchElementException because the initializer does not exist.");
        } catch (NoSuchElementException e) {
            // Verify that the correct exception with a descriptive message is thrown.
            String expectedMessage = "No child initializer with name " + unknownInitializerName;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}