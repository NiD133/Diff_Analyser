package com.google.gson.internal;

import org.junit.Test;

/**
 * Unit tests for {@link JavaVersion}.
 */
public class JavaVersionTest {

    /**
     * Verifies that parseMajorJavaVersion throws a NullPointerException when the input string is null.
     * This is the expected behavior as the method does not accept null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void parseMajorJavaVersion_withNullInput_shouldThrowNullPointerException() {
        // Calling the method with null should trigger the exception
        JavaVersion.parseMajorJavaVersion(null);
    }
}