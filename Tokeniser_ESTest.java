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
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class Tokeniser_ESTest extends Tokeniser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testTransitionToDoctypeSystemIdentifierSingleQuoted() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("", "");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        TokeniserState state = TokeniserState.DoctypeSystemIdentifier_singleQuoted;
        tokeniser.transition(state);
    }

    @Test(timeout = 4000)
    public void testEofErrorWithScriptDataEscapedLessthanSign() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("{O_&7vi9I", "{O_&7vi9I");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        TokeniserState state = TokeniserState.ScriptDataEscapedLessthanSign;
        tokeniser.eofError(state);
    }

    @Test(timeout = 4000)
    public void testCommentAsXmlDeclarationReturnsNull() throws Throwable {
        Comment comment = new Comment("?_+gYddM=:Bw~{Pk");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNull(xmlDeclaration);
    }

    @Test(timeout = 4000)
    public void testUnescapeEntities() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(".A!r,Rq!l<z-vT9k", ".A!r,Rq!l<z-vT9k");
        Parser parser = new Parser(xmlTreeBuilder);
        StreamParser streamParser = new StreamParser(parser);
        streamParser.parseFragment("*S(.f;(4%%-%Rr", (Element) document, "");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        String result = tokeniser.unescapeEntities(true);
        assertEquals("*S(.f;(4%%-%Rr", result);
    }

    @Test(timeout = 4000)
    public void testCreateTagPending() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("{_&vi9I", "{_&vi9I");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        Token.Tag tag = tokeniser.createTagPending(false);
        assertNotNull(tag);
    }

    @Test(timeout = 4000)
    public void testUnescapeEntitiesThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("~q|K}QnrD6s{{tpV-~", "~q|K}QnrD6s{{tpV-~");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        try {
            tokeniser.unescapeEntities(false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.Tokeniser", e);
        }
    }

    @Test(timeout = 4000)
    public void testTransitionThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("missing semicolon on [&#%s]", "missing semicolon on [&#%s]");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        TokeniserState state = TokeniserState.TagOpen;
        try {
            tokeniser.transition(state);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.Tokeniser", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("< ", "< ");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        try {
            tokeniser.read();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEmitTagPendingThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("</", "</");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        try {
            tokeniser.emitTagPending();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.Tokeniser", e);
        }
    }

    @Test(timeout = 4000)
    public void testEmitIntArrayThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("", "");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        try {
            tokeniser.emit((int[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.Tokeniser", e);
        }
    }

    @Test(timeout = 4000)
    public void testEmitIntArrayThrowsIllegalArgumentException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("{O_&7vinI", "{O_&7vinI");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        int[] intArray = new int[1];
        intArray[0] = -386;
        try {
            tokeniser.emit(intArray);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEmitDoctypePendingThrowsIllegalArgumentException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(".A!r,Rq!l<z-vT9k", ".A!r,Rq!l<z-vT9k");
        Parser parser = new Parser(xmlTreeBuilder);
        StreamParser streamParser = new StreamParser(parser);
        streamParser.parseFragment("*S(.f;(4%%-%Rr", (Element) document, "");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        tokeniser.emitDoctypePending();
        Token.Doctype doctype = new Token.Doctype();
        try {
            tokeniser.emit(doctype);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testEmitTokenThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("</", "</");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        try {
            tokeniser.emit((Token) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.Tokeniser", e);
        }
    }

    @Test(timeout = 4000)
    public void testEmitStringThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("< ", "< ");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        try {
            tokeniser.emit("< ");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.Tokeniser", e);
        }
    }

    @Test(timeout = 4000)
    public void testEmitCharThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("missing semicolon on [&#%s]", "missing semicolon on [&#%s]");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        try {
            tokeniser.emit('C');
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.Tokeniser", e);
        }
    }

    @Test(timeout = 4000)
    public void testConsumeCharacterReferenceThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("< ", "< ");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        try {
            tokeniser.consumeCharacterReference((Character) null, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.Tokeniser", e);
        }
    }

    @Test(timeout = 4000)
    public void testAdvanceTransitionThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("missing semicolon on [&#%s]", "missing semicolon on [&#%s]");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        TokeniserState state = TokeniserState.RawtextEndTagName;
        try {
            tokeniser.advanceTransition(state);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.Tokeniser", e);
        }
    }

    @Test(timeout = 4000)
    public void testTokeniserConstructorThrowsNullPointerException() throws Throwable {
        HtmlTreeBuilder htmlTreeBuilder = new HtmlTreeBuilder();
        try {
            new Tokeniser(htmlTreeBuilder);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.Tokeniser", e);
        }
    }

    @Test(timeout = 4000)
    public void testErrorMethod() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("~q|K}QnrD6s{{tpV-~", "~q|K}QnrD6s{{tpV-~");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        tokeniser.error("~q|K}QnrD6s{{tpV-~", (Object[]) null);
    }

    @Test(timeout = 4000)
    public void testUnescapeEntitiesMethod() throws Throwable {
        String result = Parser.unescapeEntities("&#x27;", false);
        assertEquals("'", result);
    }

    @Test(timeout = 4000)
    public void testErrorWithMessage() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("{O_&7vinI", "{O_&7vinI");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        tokeniser.error("{O_&7vinI");
    }

    @Test(timeout = 4000)
    public void testParseWithStringReader() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        StringReader stringReader = new StringReader("amp=12;1&gt=1q;3&lt=1o;2&quot=y;0&");
        Parser parser = new Parser(xmlTreeBuilder);
        parser.setTrackErrors(39);
        Document document = xmlTreeBuilder.parse(stringReader, "userAgent", parser);
        assertEquals("userAgent", document.location());
    }

    @Test(timeout = 4000)
    public void testErrorWithTokeniserState() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("{_&vi9I", "{_&vi9I");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        TokeniserState state = TokeniserState.BeforeDoctypeSystemIdentifier;
        tokeniser.error(state);
    }

    @Test(timeout = 4000)
    public void testAppropriateEndTagSeq() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(":matchesWholeOwnText(%s)", ":matchesWholeOwnText(%s)");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        String result = tokeniser.appropriateEndTagSeq();
        assertEquals("</null", result);
        assertNotNull(result);
    }

    @Test(timeout = 4000)
    public void testIsAppropriateEndTagToken() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("{_&vi9I", "{_&vi9I");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        boolean result = tokeniser.isAppropriateEndTagToken();
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testUnescapeEntitiesWithTrueFlag() throws Throwable {
        String result = Parser.unescapeEntities("c|jj9.a5&pr)$", true);
        assertEquals("c|jj9.a5&pr)$", result);
    }

    @Test(timeout = 4000)
    public void testParseDocumentSiblingIndex() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("mcP)?FW_&5;~\"", "mcP)?FW_&5;~\"");
        assertEquals(0, document.siblingIndex());
    }

    @Test(timeout = 4000)
    public void testParseDocumentNormalName() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("{O_&<vinI", "{O_&<vinI");
        assertEquals("#root", document.normalName());
    }

    @Test(timeout = 4000)
    public void testCommentAsXmlDeclarationReturnsNullAgain() throws Throwable {
        Comment comment = new Comment("/Q");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNull(xmlDeclaration);
    }

    @Test(timeout = 4000)
    public void testCreateDoctypePending() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("", "");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        tokeniser.createDoctypePending();
    }

    @Test(timeout = 4000)
    public void testCreateXmlDeclPending() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("http://www.w3.org/1999/xhtml", "http://www.w3.org/1999/xhtml");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        Token.XmlDecl xmlDecl = tokeniser.createXmlDeclPending(false);
        assertNotNull(xmlDecl);
    }

    @Test(timeout = 4000)
    public void testEmitCommentPendingThrowsNullPointerException() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("&#x27;", "&#x27;");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        try {
            tokeniser.emitCommentPending();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.Tokeniser", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseBodyFragment() throws Throwable {
        Document document = Parser.parseBodyFragment("C(p;<i>L_mFu^", "C(p;<i>L_mFu^");
        assertEquals("#root", document.tagName());
    }

    @Test(timeout = 4000)
    public void testCreateBogusCommentPending() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("missing semicolon on [&#%s]", "missing semicolon on [&#%s]");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        tokeniser.createBogusCommentPending();
    }

    @Test(timeout = 4000)
    public void testCreateCommentPending() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("{_&vi9I", "{_&vi9I");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        tokeniser.createCommentPending();
    }

    @Test(timeout = 4000)
    public void testEmitDoctypePendingThrowsNullPointerExceptionAgain() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("{O_&7vi9I", "v62F");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        try {
            tokeniser.emitDoctypePending();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.Tokeniser", e);
        }
    }

    @Test(timeout = 4000)
    public void testAppropriateEndTagNameReturnsNull() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("", "");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        String result = tokeniser.appropriateEndTagName();
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testCreateTempBuffer() throws Throwable {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("~q|K}QnrD6s{{tpV-~", "~q|K}QnrD6s{{tpV-~");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);
        tokeniser.createTempBuffer();
    }
}