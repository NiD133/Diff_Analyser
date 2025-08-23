package org.jsoup.parser;

import org.junit.Test;
import java.io.StringReader;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link Tokeniser} class, focusing on entity unescaping.
 */
public class TokeniserTest {

    /**
     * Verifies that unescapeEntities returns the original string unmodified when it
     * contains no character entities.
     */
    @Test
    public void unescapeEntitiesReturnsOriginalStringWhenNoEntitiesPresent() {
        // Arrange
        String inputWithNoEntities = "*S(.f;(4%%-%Rr";

        // To test the Tokeniser's unescapeEntities method, we need to initialize it
        // with a CharacterReader containing our input string. The standard way to do this
        // is by initializing a TreeBuilder, which the Tokeniser will use to get the reader.
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        treeBuilder.initialiseParse(new StringReader(inputWithNoEntities), "", parser);
        Tokeniser tokeniser = new Tokeniser(treeBuilder);

        // Act
        // The 'inAttribute' parameter (true) specifies the context for unescaping,
        // though it doesn't alter the behavior for this specific input string.
        String unescapedString = tokeniser.unescapeEntities(true);

        // Assert
        assertEquals("The string should be returned as-is because it contains no entities.",
            inputWithNoEntities, unescapedString);
    }
}