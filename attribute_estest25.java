package org.jsoup.nodes;

import org.junit.Test;

/**
 * Test suite for the {@link Attribute} class, focusing on edge cases and invalid inputs.
 */
public class AttributeTest {

    /**
     * Verifies that the html() method throws a NullPointerException when passed a null Appendable.
     * The method's contract requires a valid Appendable instance to write the output to.
     */
    @Test(expected = NullPointerException.class)
    public void htmlWithNullAppendableThrowsNullPointerException() {
        // Arrange: Create a standard attribute and output settings.
        // The specific key and value of the attribute do not matter for this test.
        Attribute attribute = new Attribute("id", "test");
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        // Act: Call the html() method with a null Appendable.
        // The NullPointerException is expected to be thrown here.
        attribute.html(null, outputSettings);

        // Assert: The test passes if the expected NullPointerException is thrown.
        // This is handled declaratively by the @Test(expected = ...) annotation.
    }
}