package org.jsoup.internal;

import org.junit.Test;

import static org.junit.Assert.fail; // Retained for context, but @Test(expected) is used.

/**
 * This test was automatically generated and originally verified the behavior of
 * java.lang.StringBuilder, not StringUtil. It has been refactored for clarity
 * by removing irrelevant code and simplifying the setup.
 */
public class StringUtil_ESTestTest29 extends StringUtil_ESTest_scaffolding {

    /**
     * Verifies that StringBuilder.insert() throws a StringIndexOutOfBoundsException
     * when the insertion index is greater than the builder's current length.
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void stringBuilderInsertThrowsExceptionForOutOfBoundsIndex() {
        // Arrange: Create a simple StringBuilder and define an index that is clearly out of bounds.
        StringBuilder sb = new StringBuilder("test");
        char[] dataToInsert = {'a', 'b', 'c'};
        int outOfBoundsIndex = 100; // An index far beyond the string's length of 4.

        // Act & Assert: Attempting to insert data at an invalid index.
        // The @Test(expected=...) annotation asserts that a StringIndexOutOfBoundsException is thrown.
        sb.insert(outOfBoundsIndex, dataToInsert);
    }
}