package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.StringReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
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
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true)
public class Tokeniser_ESTest extends Tokeniser_ESTest_scaffolding {

    // Tests for state transitions and error handling
    @Test(timeout = 4000)
    public void transitionToDoctypeSystemIdentifierSingleQuotedState() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("", "");
        Tokeniser tokeniser = new Tokeniser(builder);
        tokeniser.transition(TokeniserState.DoctypeSystemIdentifier_singleQuoted);
    }

    @Test(timeout = 4000)
    public void handleEofErrorInScriptDataEscapedState() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("{_&vi9I", "{_&vi9I");
        Tokeniser tokeniser = new Tokeniser(builder);
        tokeniser.eofError(TokeniserState.ScriptDataEscapedLessthanSign);
    }

    @Test(timeout = 4000)
    public void handleErrorInBeforeDoctypeSystemIdentifierState() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("{_&vi9I", "{_&vi9I");
        Tokeniser tokeniser = new Tokeniser(builder);
        tokeniser.error(TokeniserState.BeforeDoctypeSystemIdentifier);
    }

    // Tests for XML declaration and comment behavior
    @Test(timeout = 4000)
    public void commentConversionToXmlDeclarationReturnsNull() {
        Comment comment = new Comment("?_+gYddM=:Bw~{Pk");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNull(declaration);
    }

    @Test(timeout = 4000)
    public void anotherCommentConversionToXmlDeclarationReturnsNull() {
        Comment comment = new Comment("/Q");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNull(declaration);
    }

    // Tests for entity unescaping functionality
    @Test(timeout = 4000)
    public void unescapeEntitiesInAttributeContext() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(".A!r,Rq!l<z-vT9k", ".A!r,Rq!l<z-vT9k");
        Parser parser = new Parser(builder);
        StreamParser streamParser = new StreamParser(parser);
        streamParser.parseFragment("*S(.f;(4%%-%Rr", doc.body(), "");
        
        Tokeniser tokeniser = new Tokeniser(builder);
        String unescaped = tokeniser.unescapeEntities(true);
        assertEquals("*S(.f;(4%%-%Rr", unescaped);
    }

    @Test(timeout = 4000)
    public void unescapeApostropheEntity() {
        String unescaped = Parser.unescapeEntities("&#x27;", false);
        assertEquals("'", unescaped);
    }

    @Test(timeout = 4000)
    public void unescapeEntitiesWithoutSpecialCharacters() {
        String unescaped = Parser.unescapeEntities("c|jj9.a5&pr)$", true);
        assertEquals("c|jj9.a5&pr)$", unescaped);
    }

    // Tests for tag creation and emission
    @Test(timeout = 4000)
    public void createPendingTag() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("{_&vi9I", "{_&vi9I");
        Tokeniser tokeniser = new Tokeniser(builder);
        Token.Tag tag = tokeniser.createTagPending(false);
        assertNotNull(tag);
    }

    @Test(timeout = 4000)
    public void createXmlDeclarationPending() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("http://www.w3.org/1999/xhtml", "http://www.w3.org/1999/xhtml");
        Tokeniser tokeniser = new Tokeniser(builder);
        Token.XmlDecl decl = tokeniser.createXmlDeclPending(false);
        assertNotNull(decl);
    }

    @Test(timeout = 4000)
    public void createDoctypePending() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("", "");
        Tokeniser tokeniser = new Tokeniser(builder);
        tokeniser.createDoctypePending();
    }

    @Test(timeout = 4000)
    public void createCommentPending() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("{_&vi9I", "{_&vi9I");
        Tokeniser tokeniser = new Tokeniser(builder);
        tokeniser.createCommentPending();
    }

    @Test(timeout = 4000)
    public void createBogusCommentPending() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("missing semicolon on [&#%s]", "missing semicolon on [&#%s]");
        Tokeniser tokeniser = new Tokeniser(builder);
        tokeniser.createBogusCommentPending();
    }

    @Test(timeout = 4000)
    public void createTemporaryBuffer() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("~q|K}QnrD6s{{tpV-~", "~q|K}QnrD6s{{tpV-~");
        Tokeniser tokeniser = new Tokeniser(builder);
        tokeniser.createTempBuffer();
    }

    // Tests for end tag handling
    @Test(timeout = 4000)
    public void getAppropriateEndTagSequence() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse(":matchesWholeOwnText(%s)", ":matchesWholeOwnText(%s)");
        Tokeniser tokeniser = new Tokeniser(builder);
        String endTagSeq = tokeniser.appropriateEndTagSeq();
        assertEquals("</null", endTagSeq);
    }

    @Test(timeout = 4000)
    public void checkAppropriateEndTagNameReturnsNull() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("", "");
        Tokeniser tokeniser = new Tokeniser(builder);
        String endTagName = tokeniser.appropriateEndTagName();
        assertNull(endTagName);
    }

    @Test(timeout = 4000)
    public void verifyIsAppropriateEndTagToken() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("{_&vi9I", "{_&vi9I");
        Tokeniser tokeniser = new Tokeniser(builder);
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    // Tests for document parsing behavior
    @Test(timeout = 4000)
    public void parseDocumentSetsSiblingIndex() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("mcP)?FW_&5;~\"", "mcP)?FW_&5;~\"");
        assertEquals(0, doc.siblingIndex());
    }

    @Test(timeout = 4000)
    public void parseDocumentSetsNormalName() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("{O_&<vinI", "{O_&<vinI");
        assertEquals("#root", doc.normalName());
    }

    @Test(timeout = 4000)
    public void parseBodyFragmentSetsTagName() {
        Document doc = Parser.parseBodyFragment("C(p;<i>L_mFu^", "C(p;<i>L_mFu^");
        assertEquals("#root", doc.tagName());
    }

    @Test(timeout = 4000)
    public void parseWithStringReaderSetsLocation() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        StringReader reader = new StringReader("amp=12;1&gt=1q;3&lt=1o;2&quot=y;0&");
        Parser parser = new Parser(builder);
        parser.setTrackErrors(39);
        Document doc = builder.parse(reader, "userAgent", parser);
        assertEquals("userAgent", doc.location());
    }

    // Tests for error reporting
    @Test(timeout = 4000)
    public void reportErrorWithMessage() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("~q|K}QnrD6s{{tpV-~", "~q|K}QnrD6s{{tpV-~");
        Tokeniser tokeniser = new Tokeniser(builder);
        tokeniser.error("~q|K}QnrD6s{{tpV-~");
    }

    @Test(timeout = 4000)
    public void reportAnotherErrorWithMessage() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("{O_&7vinI", "{O_&7vinI");
        Tokeniser tokeniser = new Tokeniser(builder);
        tokeniser.error("{O_&7vinI");
    }

    // Tests for expected exceptions
    @Test(timeout = 4000)
    public void unescapeEntitiesThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("~q|K}QnrD6s{{tpV-~", "~q|K}QnrD6s{{tpV-~");
        Tokeniser tokeniser = new Tokeniser(builder);
        try {
            tokeniser.unescapeEntities(false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void transitionThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("missing semicolon on [&#%s]", "missing semicolon on [&#%s]");
        Tokeniser tokeniser = new Tokeniser(builder);
        try {
            tokeniser.transition(TokeniserState.TagOpen);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void readThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("< ", "< ");
        Tokeniser tokeniser = new Tokeniser(builder);
        try {
            tokeniser.read();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void emitTagPendingThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("</", "</");
        Tokeniser tokeniser = new Tokeniser(builder);
        try {
            tokeniser.emitTagPending();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void emitNullIntArrayThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("", "");
        Tokeniser tokeniser = new Tokeniser(builder);
        try {
            tokeniser.emit((int[]) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void emitInvalidCodepointThrowsIllegalArgumentException() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("{O_&7vinI", "{O_&7vinI");
        Tokeniser tokeniser = new Tokeniser(builder);
        int[] invalidCodepoint = {-386};
        try {
            tokeniser.emit(invalidCodepoint);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void emitDoctypeAfterPendingThrowsException() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(".A!r,Rq!l<z-vT9k", ".A!r,Rq!l<z-vT9k");
        Parser parser = new Parser(builder);
        StreamParser streamParser = new StreamParser(parser);
        streamParser.parseFragment("*S(.f;(4%%-%Rr", doc.body(), "");
        
        Tokeniser tokeniser = new Tokeniser(builder);
        tokeniser.emitDoctypePending();
        try {
            tokeniser.emit(new Token.Doctype());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void emitNullTokenThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("</", "</");
        Tokeniser tokeniser = new Tokeniser(builder);
        try {
            tokeniser.emit((Token) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void emitStringThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("< ", "< ");
        Tokeniser tokeniser = new Tokeniser(builder);
        try {
            tokeniser.emit("< ");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void emitCharThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("missing semicolon on [&#%s]", "missing semicolon on [&#%s]");
        Tokeniser tokeniser = new Tokeniser(builder);
        try {
            tokeniser.emit('C');
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void consumeCharacterReferenceWithNullCharThrowsException() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("< ", "< ");
        Tokeniser tokeniser = new Tokeniser(builder);
        try {
            tokeniser.consumeCharacterReference(null, true);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void advanceTransitionThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("missing semicolon on [&#%s]", "missing semicolon on [&#%s]");
        Tokeniser tokeniser = new Tokeniser(builder);
        try {
            tokeniser.advanceTransition(TokeniserState.RawtextEndTagName);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void tokeniserConstructorWithHtmlTreeBuilderThrowsException() {
        HtmlTreeBuilder builder = new HtmlTreeBuilder();
        try {
            new Tokeniser(builder);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void emitCommentPendingThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("&#x27;", "&#x27;");
        Tokeniser tokeniser = new Tokeniser(builder);
        try {
            tokeniser.emitCommentPending();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void emitDoctypePendingThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("{O_&7vi9I", "v62F");
        Tokeniser tokeniser = new Tokeniser(builder);
        try {
            tokeniser.emitDoctypePending();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}