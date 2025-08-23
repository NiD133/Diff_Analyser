package org.jsoup.nodes;

import org.junit.Test;

/**
 * Test suite for the {@link Entities#escape(org.jsoup.internal.QuietAppendable, String, Document.OutputSettings, int)} method.
 */
public class EntitiesEscapeTest {

    /**
     * Verifies that calling the escape method with a null Appendable
     * results in a NullPointerException, as expected.
     */
    @Test(expected = NullPointerException.class)
    public void escapeWithNullAppendableThrowsNullPointerException() {
        // Arrange
        String input = "An example string.";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        int arbitraryOptions = 0; // The value of options is not relevant for this test.

        // Act
        // The method under test should throw an exception when the first argument is null.
        Entities.escape(null, input, outputSettings, arbitraryOptions);

        // Assert: The test passes if a NullPointerException is thrown, as declared
        // in the @Test annotation.
    }
}