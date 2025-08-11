package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringReader;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.HtmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.StreamParser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.XmlTreeBuilder;

/**
 * Test suite for the Tokeniser class, which reads HTML/XML input streams and converts them into tokens.
 */
public class TokeniserTest {

    // Test data constants
    private static final String EMPTY_STRING = "";
    private static final String SIMPLE_HTML = "<div>content</div>";
    private static final String XML_ENTITY = "&#x27;"; // Single quote entity
    private static final String MALFORMED_TAG = "</";
    private static final String INCOMPLETE_TAG = "< ";

    /**
     * Helper method to create a tokeniser with XML tree builder and parsed content
     */
    private Tokeniser createXmlTokeniser(String input, String baseUri) {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(input, baseUri);
        return new Tokeniser(xmlTreeBuilder);
    }

    /**
     * Helper method to create a tokeniser with HTML tree builder
     */
    private Tokeniser createHtmlTokeniser() {
        HtmlTreeBuilder htmlTreeBuilder = new HtmlTreeBuilder();
        return new Tokeniser(htmlTreeBuilder);
    }

    @Test(timeout = 4000)
    public void shouldTransitionToSpecifiedTokeniserState() throws Throwable {
        Tokeniser tokeniser = createXmlTokeniser(EMPTY_STRING, EMPTY_STRING);
        TokeniserState targetState = TokeniserState.DoctypeSystemIdentifier_singleQuoted;
        
        // Should not throw exception when transitioning to valid state
        tokeniser.transition(targetState);
    }

    @Test(timeout = 4000)
    public void shouldHandleEofErrorInSpecificState() throws Throwable {
        Tokeniser tokeniser = createXmlTokeniser("{O_&7vi9I", "{O_&7vi9I");
        TokeniserState scriptState = TokeniserState.ScriptDataEscapedLessthanSign;
        
        // Should handle EOF error gracefully
        tokeniser.eofError(scriptState);
    }

    @Test(timeout = 4000)
    public void shouldReturnNullWhenCommentCannotBeConvertedToXmlDeclaration() throws Throwable {
        Comment comment = new Comment("?_+gYddM=:Bw~{Pk");
        
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        
        assertNull("Comment with invalid XML declaration format should return null", xmlDeclaration);
    }

    @Test(timeout = 4000)
    public void shouldUnescapeEntitiesInParsedFragment() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(".A!r,Rq!l<z-vT9k", ".A!r,Rq!l<z-vT9k");
        Parser parser = new Parser(xmlTreeBuilder);
        StreamParser streamParser = new StreamParser(parser);
        String fragmentContent = "*S(.f;(4%%-%Rr";
        
        streamParser.parseFragment(fragmentContent, (Element) document, EMPTY_STRING);
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        
        String unescapedResult = tokeniser.unescapeEntities(true);
        assertEquals("Should return the original fragment content", fragmentContent, unescapedResult);
    }

    @Test(timeout = 4000)
    public void shouldCreateStartTagToken() throws Throwable {
        Tokeniser tokeniser = createXmlTokeniser("{_&vi9I", "{_&vi9I");
        
        Token.Tag startTag = tokeniser.createTagPending(false);
        
        assertNotNull("Should create a valid start tag token", startTag);
    }

    @Test(timeout = 4000)
    public void shouldThrowNullPointerExceptionWhenUnescapingEntitiesWithoutInAttribute() throws Throwable {
        Tokeniser tokeniser = createXmlTokeniser("~q|K}QnrD6s{{tpV-~", "~q|K}QnrD6s{{tpV-~");
        
        try {
            tokeniser.unescapeEntities(false);
            fail("Expected NullPointerException when unescaping entities without proper context");
        } catch(NullPointerException e) {
            // Expected behavior - tokeniser not properly initialized for this operation
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowNullPointerExceptionOnTransitionWithoutProperInitialization() throws Throwable {
        Tokeniser tokeniser = createXmlTokeniser("missing semicolon on [&#%s]", "missing semicolon on [&#%s]");
        
        try {
            tokeniser.transition(TokeniserState.TagOpen);
            fail("Expected NullPointerException when transitioning without proper reader state");
        } catch(NullPointerException e) {
            // Expected behavior - reader not properly positioned
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowNullPointerExceptionWhenReadingWithoutProperState() throws Throwable {
        Tokeniser tokeniser = createXmlTokeniser(INCOMPLETE_TAG, INCOMPLETE_TAG);
        
        try {
            tokeniser.read();
            fail("Expected NullPointerException when reading without proper tokeniser state");
        } catch(NullPointerException e) {
            // Expected behavior - tokeniser state not properly initialized
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowNullPointerExceptionWhenEmittingTagWithoutPendingTag() throws Throwable {
        Tokeniser tokeniser = createXmlTokeniser(MALFORMED_TAG, MALFORMED_TAG);
        
        try {
            tokeniser.emitTagPending();
            fail("Expected NullPointerException when emitting tag without pending tag");
        } catch(NullPointerException e) {
            // Expected behavior - no pending tag to emit
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowNullPointerExceptionWhenEmittingNullCodepoints() throws Throwable {
        Tokeniser tokeniser = createXmlTokeniser(EMPTY_STRING, EMPTY_STRING);
        
        try {
            tokeniser.emit((int[]) null);
            fail("Expected NullPointerException when emitting null codepoints");
        } catch(NullPointerException e) {
            // Expected behavior - cannot emit null codepoints
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowIllegalArgumentExceptionForInvalidCodepoint() throws Throwable {
        Tokeniser tokeniser = createXmlTokeniser("{O_&7vinI", "{O_&7vinI");
        int[] invalidCodepoints = {-386}; // Negative codepoint is invalid
        
        try {
            tokeniser.emit(invalidCodepoints);
            fail("Expected IllegalArgumentException for invalid negative codepoint");
        } catch(IllegalArgumentException e) {
            // Expected behavior - negative codepoints are invalid
        }
    }

    @Test(timeout = 4000)
    public void shouldUnescapeHexadecimalEntityCorrectly() throws Throwable {
        String result = Parser.unescapeEntities(XML_ENTITY, false);
        
        assertEquals("Should unescape &#x27; to single quote", "'", result);
    }

    @Test(timeout = 4000)
    public void shouldHandleErrorReportingGracefully() throws Throwable {
        Tokeniser tokeniser = createXmlTokeniser("{O_&7vinI", "{O_&7vinI");
        
        // Should not throw exception when reporting error
        tokeniser.error("{O_&7vinI");
    }

    @Test(timeout = 4000)
    public void shouldParseXmlWithEntityReferencesAndTrackErrors() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        StringReader stringReader = new StringReader("amp=12;1&gt=1q;3&lt=1o;2&quot=y;0&");
        Parser parser = new Parser(xmlTreeBuilder);
        parser.setTrackErrors(39);
        
        Document document = xmlTreeBuilder.parse(stringReader, "userAgent", parser);
        
        assertEquals("Should set document location to base URI", "userAgent", document.location());
    }

    @Test(timeout = 4000)
    public void shouldReturnAppropriateEndTagSequence() throws Throwable {
        Tokeniser tokeniser = createXmlTokeniser(":matchesWholeOwnText(%s)", ":matchesWholeOwnText(%s)");
        
        tokeniser.appropriateEndTagSeq(); // Initialize the sequence
        String endTagSequence = tokeniser.appropriateEndTagSeq();
        
        assertEquals("Should return appropriate end tag sequence", "</null", endTagSequence);
    }

    @Test(timeout = 4000)
    public void shouldReturnFalseForInappropriateEndTagToken() throws Throwable {
        Tokeniser tokeniser = createXmlTokeniser("{_&vi9I", "{_&vi9I");
        
        boolean isAppropriate = tokeniser.isAppropriateEndTagToken();
        
        assertFalse("Should return false when no appropriate end tag token exists", isAppropriate);
    }

    @Test(timeout = 4000)
    public void shouldReturnUnmodifiedStringWhenNoEntitiesToUnescape() throws Throwable {
        String input = "c|jj9.a5&pr)$";
        
        String result = Parser.unescapeEntities(input, true);
        
        assertEquals("Should return original string when no entities to unescape", input, result);
    }

    @Test(timeout = 4000)
    public void shouldCreateDoctypeTokenSuccessfully() throws Throwable {
        Tokeniser tokeniser = createXmlTokeniser(EMPTY_STRING, EMPTY_STRING);
        
        // Should not throw exception when creating doctype token
        tokeniser.createDoctypePending();
    }

    @Test(timeout = 4000)
    public void shouldCreateXmlDeclarationToken() throws Throwable {
        Tokeniser tokeniser = createXmlTokeniser("http://www.w3.org/1999/xhtml", "http://www.w3.org/1999/xhtml");
        
        Token.XmlDecl xmlDeclaration = tokeniser.createXmlDeclPending(false);
        
        assertNotNull("Should create valid XML declaration token", xmlDeclaration);
    }

    @Test(timeout = 4000)
    public void shouldCreateCommentTokenSuccessfully() throws Throwable {
        Tokeniser tokeniser = createXmlTokeniser("{_&vi9I", "{_&vi9I");
        
        // Should not throw exception when creating comment token
        tokeniser.createCommentPending();
    }

    @Test(timeout = 4000)
    public void shouldReturnNullForAppropriateEndTagNameWhenNoneExists() throws Throwable {
        Tokeniser tokeniser = createXmlTokeniser(EMPTY_STRING, EMPTY_STRING);
        
        String endTagName = tokeniser.appropriateEndTagName();
        
        assertNull("Should return null when no appropriate end tag name exists", endTagName);
    }

    @Test(timeout = 4000)
    public void shouldCreateTempBufferSuccessfully() throws Throwable {
        Tokeniser tokeniser = createXmlTokeniser("~q|K}QnrD6s{{tpV-~", "~q|K}QnrD6s{{tpV-~");
        
        // Should not throw exception when creating temp buffer
        tokeniser.createTempBuffer();
    }

    @Test(timeout = 4000)
    public void shouldParseBodyFragmentCorrectly() throws Throwable {
        Document document = Parser.parseBodyFragment("C(p;<i>L_mFu^", "C(p;<i>L_mFu^");
        
        assertEquals("Should create document with root tag name", "#root", document.tagName());
    }

    @Test(timeout = 4000)
    public void shouldFailToCreateTokeniserWithUninitializedHtmlTreeBuilder() throws Throwable {
        HtmlTreeBuilder htmlTreeBuilder = new HtmlTreeBuilder();
        
        try {
            new Tokeniser(htmlTreeBuilder);
            fail("Expected NullPointerException when creating tokeniser with uninitialized tree builder");
        } catch(NullPointerException e) {
            // Expected behavior - tree builder not properly initialized
        }
    }
}