package org.jsoup.parser;

import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

/**
 * Contains tests for the {@link Tokeniser} class, focusing on specific edge cases.
 */
public class TokeniserTest {

    /**
     * Verifies that calling consumeCharacterReference with a null 'additionalAllowedCharacter'
     * correctly throws a NullPointerException. This is the expected behavior as the method
     * is not designed to handle a null character input.
     */
    @Test(expected = NullPointerException.class)
    public void consumeCharacterReferenceWithNullAdditionalCharacterThrowsException() {
        // Arrange: Create a Tokeniser instance.
        // The XmlTreeBuilder and its parse() call are boilerplate needed to instantiate a Tokeniser,
        // as its constructor requires an initialized TreeBuilder. The input content is not relevant.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("dummy input", "https://example.com/");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);

        // Act: Call the method under test with a null argument for the character.
        // The assertion is handled by the @Test(expected=...) annotation, which
        // expects a NullPointerException to be thrown.
        tokeniser.consumeCharacterReference(null, true);
    }
}