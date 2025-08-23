package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;

/**
 * This test suite contains tests for the {@link Entities} class.
 * This specific test case focuses on the behavior of the escape method
 * when handling invalid arguments.
 */
public class Entities_ESTestTest29 { // Note: Class name kept from original for consistency.

    /**
     * Verifies that the {@link Entities#escape(QuietAppendable, String, Document.OutputSettings, int)}
     * method throws a NullPointerException when the destination 'Appendable' is null.
     * This ensures the method correctly handles invalid input and fails fast.
     */
    @Test(expected = NullPointerException.class)
    public void escapeWithNullAppendableThrowsNullPointerException() {
        // Arrange: Prepare the arguments for the escape method.
        // The key argument for this test is the null QuietAppendable.
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String inputToEscape = "some text"; // The content of the string is not critical for this test.
        int arbitraryOptions = 108; // An arbitrary value for options, not relevant to the null check.

        // Act & Assert: Call the method with a null appendable.
        // The @Test(expected) annotation handles the assertion, failing the test
        // if a NullPointerException is not thrown.
        Entities.escape((QuietAppendable) null, inputToEscape, outputSettings, arbitraryOptions);
    }
}