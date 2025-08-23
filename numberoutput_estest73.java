package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link NumberOutput} utility class.
 */
public class NumberOutputTest {

    /**
     * Verifies that an instance of the {@link NumberOutput} utility class can be created.
     *
     * Although this class is primarily used via its static methods, this test ensures
     * the default constructor is callable without throwing an exception. This is a common
     * practice for achieving full code coverage on utility classes.
     */
    @Test
    public void testConstructor() {
        // Act: Instantiate the NumberOutput class.
        NumberOutput instance = new NumberOutput();

        // Assert: The instance should be successfully created and not null.
        assertNotNull("Instantiation of NumberOutput should result in a non-null object.", instance);
    }
}