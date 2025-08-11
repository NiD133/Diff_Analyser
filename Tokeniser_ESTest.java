package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;

/**
 * Understandable tests for the Jsoup {@link Tokeniser}.
 *
 * This suite focuses on testing the core responsibilities of the Tokeniser,
 * such as entity unescaping, token creation, and state management concerning tags.
 */
public class TokeniserTest {

    /**
     * Helper to create a Tokeniser instance for a given input string.
     * A Tokeniser is tightly coupled with a TreeBuilder and Parser. This method
     * encapsulates the necessary setup to get a properly initialized Tokeniser.
     *
     * @param input The HTML or XML string to be tokenised.
     * @return An initialized Tokeniser.
     */
    private Tokeniser createTokeniserFor(String input) {
        // Use XmlTreeBuilder as it's a concrete implementation suitable for setup.
        XmlTreeBuilder tb = new XmlTreeBuilder();
        Parser parser = new Parser(tb);
        // This step initializes the reader and, crucially, the tokeniser within the tree builder.
        tb.initialiseParse(new StringReader(input), "", parser);
        return tb.tokeniser;
    }

    // --- Tests for unescapeEntities ---

    @Test
    public void unescapeEntities_whenStringHasNoEntities_returnsOriginalString() {
        // Arrange
        Tokeniser tokeniser = createTokeniserFor("A string with no entities.");

        // Act
        String result = tokeniser.unescapeEntities(false);

        // Assert
        assertEquals("A string with no entities.", result);
    }

    @Test
    public void unescapeEntities_withNamedEntities_decodesThem() {
        // Arrange
        Tokeniser tokeniser = createTokeniserFor("Hello &amp; &lt;world&gt;");

        // Act
        String result = tokeniser.unescapeEntities(false);

        // Assert
        assertEquals("Hello & <world>", result);
    }

    @Test
    public void unescapeEntities_withNumericDecimalEntities_decodesThem() {
        // Arrange
        Tokeniser tokeniser = createTokeniserFor("&#60;&#62;"); // Less-than and greater-than symbols

        // Act
        String result = tokeniser.unescapeEntities(false);

        // Assert
        assertEquals("<>", result);
    }

    @Test
    public void unescapeEntities_withNumericHexEntities_decodesThem() {
        // Arrange
        Tokeniser tokeniser = createTokeniserFor("&#x3C;&#x3E;"); // Less-than and greater-than symbols

        // Act
        String result = tokeniser.unescapeEntities(false);

        // Assert
        assertEquals("<>", result);
    }

    @Test
    public void unescapeEntities_withInvalidEntity_leavesItAsIs() {
        // Arrange
        Tokeniser tokeniser = createTokeniserFor("An invalid entity &notanentity; here.");

        // Act
        String result = tokeniser.unescapeEntities(false);

        // Assert
        assertEquals("An invalid entity &notanentity; here.", result);
    }

    // --- Tests for Tag State and Creation ---

    @Test
    public void createTagPending_whenCreatingStartTag_returnsStartTagToken() {
        // Arrange
        Tokeniser tokeniser = createTokeniserFor("");

        // Act
        Token.Tag tag = tokeniser.createTagPending(true); // true for start tag

        // Assert
        assertTrue("Tag should be a start tag", tag.isStartTag());
        assertFalse("Tag should not be an end tag", tag.isEndTag());
    }

    @Test
    public void createTagPending_whenCreatingEndTag_returnsEndTagToken() {
        // Arrange
        Tokeniser tokeniser = createTokeniserFor("");

        // Act
        Token.Tag tag = tokeniser.createTagPending(false); // false for end tag

        // Assert
        assertTrue("Tag should be an end tag", tag.isEndTag());
        assertFalse("Tag should not be a start tag", tag.isStartTag());
    }

    @Test
    public void appropriateEndTagName_whenNoStartTagEncountered_returnsNull() {
        // Arrange
        Tokeniser tokeniser = createTokeniserFor("some text without tags");

        // Act
        String endTagName = tokeniser.appropriateEndTagName();

        // Assert
        assertNull(endTagName);
    }

    @Test
    public void appropriateEndTagName_afterEmittingStartTag_returnsCorrectName() {
        // Arrange
        Tokeniser tokeniser = createTokeniserFor("<div>");
        tokeniser.read(); // This reads and emits the <div> token, setting the internal state.

        // Act
        String endTagName = tokeniser.appropriateEndTagName();

        // Assert
        assertEquals("div", endTagName);
    }

    @Test
    public void appropriateEndTagSeq_afterEmittingStartTag_returnsCorrectSequence() {
        // Arrange
        Tokeniser tokeniser = createTokeniserFor("<p>");
        tokeniser.read(); // Reads and processes the <p> tag, setting internal state.

        // Act
        String endTagSeq = tokeniser.appropriateEndTagSeq();

        // Assert
        assertEquals("</p", endTagSeq);
    }

    @Test
    public void isAppropriateEndTagToken_whenEndTagMatches_returnsTrue() {
        // Arrange
        Tokeniser tokeniser = createTokeniserFor("<div>");
        tokeniser.read(); // Consume <div>, which sets lastStartTag = "div"

        // Simulate the state where the tokeniser is about to process a matching end tag
        tokeniser.tagPending = tokeniser.createTagPending(false); // Create an end tag
        tokeniser.tagPending.name("div");

        // Act & Assert
        assertTrue("Should be an appropriate end tag", tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void isAppropriateEndTagToken_whenEndTagDoesNotMatch_returnsFalse() {
        // Arrange
        Tokeniser tokeniser = createTokeniserFor("<div>");
        tokeniser.read(); // Consume <div>, which sets lastStartTag = "div"

        // Simulate the state where the tokeniser is about to process a mismatched end tag
        tokeniser.tagPending = tokeniser.createTagPending(false); // Create an end tag
        tokeniser.tagPending.name("p");

        // Act & Assert
        assertFalse("Should not be an appropriate end tag", tokeniser.isAppropriateEndTagToken());
    }

    // --- Tests for Other Token Creation ---

    @Test
    public void createCommentPending_createsNonBogusComment() {
        // Arrange
        Tokeniser tokeniser = createTokeniserFor("");

        // Act
        tokeniser.createCommentPending();
        Token.Comment comment = tokeniser.commentPending;

        // Assert
        assertNotNull(comment);
        assertFalse("Comment should be a regular (non-bogus) comment", comment.bogus);
    }

    @Test
    public void createBogusCommentPending_createsBogusComment() {
        // Arrange
        Tokeniser tokeniser = createTokeniserFor("");

        // Act
        tokeniser.createBogusCommentPending();
        Token.Comment comment = tokeniser.commentPending;

        // Assert
        assertNotNull(comment);
        assertTrue("Comment should be a bogus comment", comment.bogus);
    }

    @Test
    public void createDoctypePending_createsDoctypeToken() {
        // Arrange
        Tokeniser tokeniser = createTokeniserFor("");

        // Act
        tokeniser.createDoctypePending();
        Token.Doctype doctype = tokeniser.doctypePending;

        // Assert
        assertNotNull(doctype);
        assertEquals(Token.TokenType.Doctype, doctype.getType());
    }
}