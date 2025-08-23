package org.jsoup.internal;

import org.junit.Test;

/**
 * Test suite for the {@link StringUtil} class, focusing on exception-related behavior.
 * This class was improved from an auto-generated test case to enhance clarity and maintainability.
 */
public class StringUtil_ESTestTest64 extends StringUtil_ESTest_scaffolding {

    /**
     * Verifies that the {@code StringUtil.in(String, String...)} method throws a
     * {@code NullPointerException} when the 'haystack' varargs array contains a null element.
     * This is the expected behavior, as the method likely attempts to call {@code .equals()}
     * on a null reference during its search.
     */
    @Test(expected = NullPointerException.class)
    public void inShouldThrowNullPointerExceptionWhenHaystackContainsNull() {
        // Arrange: Define a search term and a "haystack" array containing a null value.
        // The presence of any null element should trigger the exception.
        String needle = "a";
        String[] haystackWithNull = {"a", "b", null};

        // Act: Call the method under test. This line is expected to throw the exception.
        StringUtil.in(needle, haystackWithNull);

        // Assert: The test passes if the expected NullPointerException is thrown,
        // which is handled declaratively by the @Test(expected=...) annotation.
    }
}