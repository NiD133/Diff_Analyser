package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Test suite for the {@link Tokeniser} class.
 */
public class TokeniserTest {

    /**
     * Verifies that appropriateEndTagName() returns null when the tokeniser has not yet
     * processed any start tags.
     */
    @Test
    public void appropriateEndTagNameShouldBeNullWhenNoStartTagSeen() {
        // Arrange: Create a Tokeniser that has not processed any input.
        // An XmlTreeBuilder is a convenient way to initialize a Tokeniser.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("", ""); // Initializes the internal reader
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);

        // Act: Call the method under test.
        String appropriateEndTag = tokeniser.appropriateEndTagName();

        // Assert: The result should be null because no start tag has been emitted.
        assertNull("Expected null for appropriate end tag name when no start tag has been processed", appropriateEndTag);
    }
}