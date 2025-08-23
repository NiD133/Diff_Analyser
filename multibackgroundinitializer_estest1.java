package org.apache.commons.lang3.concurrent;

import org.junit.Test;

import java.util.NoSuchElementException;

/**
 * Test suite for {@link MultiBackgroundInitializer.MultiBackgroundInitializerResults}.
 */
public class MultiBackgroundInitializer_ESTestTest1 {

    /**
     * Tests that querying the results for an initializer that was never added
     * throws a NoSuchElementException.
     */
    @Test(expected = NoSuchElementException.class)
    public void isExceptionShouldThrowNoSuchElementExceptionForUnknownInitializer() throws Exception {
        // Arrange: Create a MultiBackgroundInitializer and initialize it.
        // It contains no child initializers.
        MultiBackgroundInitializer multiInitializer = new MultiBackgroundInitializer();
        MultiBackgroundInitializer.MultiBackgroundInitializerResults results = multiInitializer.initialize();
        final String unknownInitializerName = "non.existent.initializer.name";

        // Act: Attempt to check the exception status of a non-existent child initializer.
        // This call is expected to throw NoSuchElementException, as specified by the
        // @Test(expected=...) annotation.
        results.isException(unknownInitializerName);
    }
}