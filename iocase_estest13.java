package org.apache.commons.io;

import org.junit.Test;

/**
 * This test suite verifies the behavior of the {@link IOCase} enum,
 * specifically its string comparison methods.
 */
public class IOCaseTest {

    /**
     * Tests that calling {@code checkIndexOf} with a negative start index
     * correctly throws a {@code StringIndexOutOfBoundsException}. This is the
     * expected behavior as the method relies on underlying standard String
     * operations which prohibit negative indices.
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void checkIndexOfShouldThrowExceptionForNegativeStartIndex() {
        // Arrange: Define the test case, strings, and an invalid negative start index.
        final IOCase ioCase = IOCase.INSENSITIVE;
        final String text = "Some text to search within";
        final String search = "text";
        final int negativeStartIndex = -1;

        // Act: Attempt to find the search string starting from a negative index.
        // This call is expected to throw the exception.
        ioCase.checkIndexOf(text, negativeStartIndex, search);

        // Assert: The @Test(expected=...) annotation asserts that a
        // StringIndexOutOfBoundsException is thrown. No further assertions are needed.
    }
}