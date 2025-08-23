package org.jsoup.internal;

import org.junit.Test;

/**
 * Test suite for the {@link StringUtil#join(String[], String)} method.
 *
 * Note: This class retains its original name and parent class for consistency with the
 * provided code, but in a real-world scenario, it would be named something like
 * `StringUtilTest` and would not extend a scaffolding class.
 */
public class StringUtil_ESTestTest100 extends StringUtil_ESTest_scaffolding {

    /**
     * Verifies that StringUtil.join throws a NullPointerException when the input array
     * contains a null element. The join operation cannot proceed with null values
     * as it attempts to call methods on them.
     */
    @Test(expected = NullPointerException.class)
    public void joinOnArrayContainingNullShouldThrowNullPointerException() {
        // Arrange: Create an array of strings that includes a null element.
        String[] arrayWithNull = {"first", null, "third"};
        String separator = ", ";

        // Act: Attempt to join the elements of the array.
        // This call is expected to fail with a NullPointerException.
        StringUtil.join(arrayWithNull, separator);

        // Assert: The test passes if a NullPointerException is thrown,
        // which is declared and handled by the @Test(expected=...) annotation.
    }
}