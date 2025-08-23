package com.google.gson.internal.bind.util;

import org.junit.Test;
import java.text.ParsePosition;

/**
 * This class contains tests for the {@link ISO8601Utils} class.
 *
 * Note: The original class name and inheritance from a scaffolding class are artifacts
 * of an auto-generated test suite. In a typical project, this test would be part of a
 * single, comprehensive `ISO8601UtilsTest` class.
 */
public class ISO8601Utils_ESTestTest4 extends ISO8601Utils_ESTest_scaffolding {

    /**
     * Verifies that {@link ISO8601Utils#parse(String, ParsePosition)} throws a
     * {@link NullPointerException} when the provided {@code ParsePosition} is null.
     */
    @Test(expected = NullPointerException.class)
    public void parseWithNullParsePositionThrowsNullPointerException() {
        // The input date string is irrelevant for this test, as the method should
        // fail due to the null ParsePosition before attempting to parse the string.
        String anyValidDateString = "2023-10-26T10:00:00Z";
        
        ISO8601Utils.parse(anyValidDateString, null);
    }
}