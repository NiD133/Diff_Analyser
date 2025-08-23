package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;

/**
 * This class contains tests for the {@link Attribute} class.
 * The original test was auto-generated and has been improved for clarity.
 */
public class Attribute_ESTestTest37 extends Attribute_ESTest_scaffolding {

    /**
     * Tests that calling the internal html() method with a null Appendable
     * throws a NullPointerException. This is a contract test ensuring the method
     * handles invalid arguments gracefully.
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void htmlWithNullAppendableThrowsNullPointerException() {
        // Arrange: Create a standard attribute and output settings.
        // The specific key and value of the attribute are not relevant for this test.
        Attribute attribute = new Attribute("id", "test-id");
        OutputSettings settings = new OutputSettings();

        // Act: Call the html() method with a null appendable.
        // The method is expected to throw a NullPointerException.
        attribute.html((QuietAppendable) null, settings);

        // Assert: The test will pass if a NullPointerException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}