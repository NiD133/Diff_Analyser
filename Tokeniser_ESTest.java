package org.jsoup.parser;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * Readable and maintainable tests for Tokeniser and related parsing utilities.
 *
 * Notes:
 * - Tokeniser is package-private, so tests are in the same package.
 * - We avoid brittle tests that depend on internal null states or throw NPEs.
 * - Each test focuses on a single, well-described behavior.
 */
public class TokeniserReadableTest {

    // Helper: build a Tokeniser backed by an initialized XmlTreeBuilder.
    private static Tokeniser newTokeniser(String input) {
        XmlTreeBuilder tb = new XmlTreeBuilder();
        tb.parse(input, ""); // initializes reader and parser
        return new Tokeniser(tb);
    }

    // -------- Parser / entity unescaping --------

    @Test
    public void unescapeEntities_hexNumericQuote() {
        // '&#x27;' is the hex numeric reference for an apostrophe.
        String result = Parser.unescapeEntities("&#x27;", false);
        assertEquals("'", result);
    }

    @Test
    public void unescapeEntities_textWithoutEntities_isReturnedAsIs() {
        String input = "c|jj9.a5&pr)$";
        String result = Parser.unescapeEntities(input, true);
        assertEquals(input, result);
    }

    // -------- Document / parser plumbing --------

    @Test
    public void xmlTreeBuilder_parse_setsDocumentLocation() {
        XmlTreeBuilder tb = new XmlTreeBuilder();
        Parser parser = new Parser(tb);
        parser.setTrackErrors(10);

        StringReader reader = new StringReader("amp=12;1&gt=1q;3&lt=1o;2&quot=y;0&");
        Document doc = tb.parse(reader, "baseUri", parser);

        assertEquals("baseUri", doc.location());
    }

    @Test
    public void parseBodyFragment_hasRootTag() {
        Document doc = Parser.parseBodyFragment("C(p;<i>L_mFu^", "C(p;<i>L_mFu^");
        assertEquals("#root", doc.tagName());
    }

    @Test
    public void xmlParsing_basicDocumentProperties() {
        XmlTreeBuilder tb = new XmlTreeBuilder();
        Document doc = tb.parse("mcP)?FW_&5;~\"", "mcP)?FW_&5;~\"");
        assertEquals(0, doc.siblingIndex());
    }

    @Test
    public void xmlParsing_normalNameIsRootForEmptyDoc() {
        XmlTreeBuilder tb = new XmlTreeBuilder();
        Document doc = tb.parse("{O_&<vinI", "{O_&<vinI");
        assertEquals("#root", doc.normalName());
    }

    // -------- Comment / XmlDeclaration bridging --------

    @Test
    public void comment_asXmlDeclaration_returnsNullWhenNotXml() {
        assertNull(new Comment("?_+gYddM=:Bw~{Pk").asXmlDeclaration());
        assertNull(new Comment("/Q").asXmlDeclaration());
    }

    // -------- Tokeniser basics (non-brittle cases only) --------

    @Test
    public void createTagPending_returnsNonNullForStartAndEnd() {
        Tokeniser tok = newTokeniser("<a>");
        assertNotNull("start tag pending should be created", tok.createTagPending(true));
        assertNotNull("end tag pending should be created", tok.createTagPending(false));
    }

    @Test
    public void createXmlDeclPending_returnsNonNull() {
        Tokeniser tok = newTokeniser("<?xml version=\"1.0\"?>");
        assertNotNull(tok.createXmlDeclPending(false));
    }

    @Test
    public void createDoctypePending_doesNotThrow() {
        Tokeniser tok = newTokeniser("<!DOCTYPE html>");
        tok.createDoctypePending();
        // no assertion: the intent is that it does not throw
    }

    @Test
    public void createCommentAndBogusCommentPending_doNotThrow() {
        Tokeniser tok = newTokeniser("<!-- hi -->");
        tok.createCommentPending();
        tok.createBogusCommentPending();
        // no assertion: the intent is that these paths are callable safely
    }

    @Test
    public void createTempBuffer_doesNotThrow() {
        Tokeniser tok = newTokeniser("<div>x</div>");
        tok.createTempBuffer();
        // no assertion: just verify it is callable
    }

    @Test
    public void isAppropriateEndTagToken_initiallyFalse() {
        Tokeniser tok = newTokeniser("<div>");
        assertFalse(tok.isAppropriateEndTagToken());
    }

    @Test
    public void appropriateEndTagName_initiallyNull() {
        Tokeniser tok = newTokeniser("");
        assertNull(tok.appropriateEndTagName());
    }

    @Test
    public void appropriateEndTagSeq_hasClosingPrefix() {
        Tokeniser tok = newTokeniser("");
        String seq = tok.appropriateEndTagSeq();
        assertNotNull(seq);
        assertTrue("sequence should start with a closing tag opener", seq.startsWith("</"));
    }

    // -------- Error reporting --------

    @Test
    public void error_withState_doesNotThrow() {
        Tokeniser tok = newTokeniser("<");
        tok.error(TokeniserState.TagOpen);
    }

    @Test
    public void error_withMessageAndVarargs_doesNotThrow() {
        Tokeniser tok = newTokeniser("<");
        tok.error("custom error without args");
        tok.error("custom error with null varargs", (Object[]) null);
    }

    @Test
    public void eofError_withState_doesNotThrow() {
        Tokeniser tok = newTokeniser("");
        tok.eofError(TokeniserState.ScriptDataEscapedLessthanSign);
    }
}