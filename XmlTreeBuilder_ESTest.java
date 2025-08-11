package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.NoSuchElementException;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;

/**
 * Test suite for XmlTreeBuilder functionality.
 * Tests XML parsing, namespace handling, and various XML token processing.
 */
public class XmlTreeBuilder_ESTest {

    private static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";
    private static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";
    private static final String MATHML_NAMESPACE = "http://www.w3.org/1998/Math/MathML";
    private static final String XHTML_NAMESPACE = "http://www.w3.org/1999/xhtml";

    // Basic functionality tests
    
    @Test
    public void shouldReturnCorrectDefaultNamespace() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        String defaultNamespace = builder.defaultNamespace();
        
        assertEquals(XML_NAMESPACE, defaultNamespace);
    }

    @Test
    public void shouldReturnDefaultTagSet() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        TagSet tagSet = builder.defaultTagSet();
        
        assertNotNull(tagSet);
    }

    @Test
    public void shouldReturnDefaultParseSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        ParseSettings settings = builder.defaultSettings();
        
        assertTrue("XML parsing should preserve tag case", settings.preserveTagCase());
    }

    @Test
    public void shouldCreateNewInstance() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        XmlTreeBuilder newInstance = builder.newInstance();
        
        assertEquals(XML_NAMESPACE, newInstance.defaultNamespace());
    }

    // Parsing tests

    @Test
    public void shouldParseSimpleXmlFromString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        Document document = builder.parse("xmlns", "xmlns");
        
        assertNotNull(document);
    }

    @Test
    public void shouldParseXmlFromReader() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        StringReader reader = new StringReader("xmlns:#text");
        Parser parser = Parser.xmlParser();
        
        builder.initialiseParse(reader, "xmlns:#text", parser);
        
        assertEquals(XML_NAMESPACE, parser.defaultNamespace());
        assertEquals(XML_NAMESPACE, builder.defaultNamespace());
    }

    @Test
    public void shouldCompleteParseFragmentSuccessfully() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse("ME;}", "ME;}");
        
        builder.completeParseFragment();
        
        assertEquals(XML_NAMESPACE, builder.defaultNamespace());
    }

    // Fragment parsing tests

    @Test
    public void shouldParseFragmentWithSvgNamespace() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Parser parser = new Parser(builder);
        StreamParser streamParser = new StreamParser(parser);
        Element element = new Element(MATHML_NAMESPACE, XML_NAMESPACE);
        
        streamParser.parseFragment(SVG_NAMESPACE, element, "{FDP|4G0#6");
        
        assertEquals(XML_NAMESPACE, builder.defaultNamespace());
    }

    @Test
    public void shouldHandleFragmentParsingWithXhtmlNamespace() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Parser parser = new Parser(builder);
        StreamParser streamParser = new StreamParser(parser);
        Element element = new Element(XHTML_NAMESPACE, XML_NAMESPACE);
        
        streamParser.parseFragment("z>!GVh", element, "u");
        
        assertEquals(XML_NAMESPACE, builder.defaultNamespace());
    }

    @Test
    public void shouldInitialiseParseFragmentWithNullContext() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        builder.initialiseParseFragment(null);
        
        assertEquals(XML_NAMESPACE, builder.defaultNamespace());
    }

    // Token processing tests

    @Test
    public void shouldProcessXmlDeclarationToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(">: ", ">: ");
        Parser parser = new Parser(builder);
        StreamParser streamParser = new StreamParser(parser);
        streamParser.parseFragment(SVG_NAMESPACE, document, XML_NAMESPACE);
        Tokeniser tokeniser = new Tokeniser(builder);
        
        Token.XmlDecl xmlDecl = tokeniser.createXmlDeclPending(true);
        boolean result = builder.process(xmlDecl);
        
        assertTrue("XML declaration should be processed successfully", result);
        assertEquals(XML_NAMESPACE, builder.defaultNamespace());
    }

    @Test
    public void shouldProcessDoctypeToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Parser parser = new Parser(builder);
        StreamParser streamParser = new StreamParser(parser);
        Element element = new Element("Doctype", "nfHa(P+.f");
        streamParser.parseFragment("$M{RJ]hM9&ek.Vtk$I", element, XHTML_NAMESPACE);
        
        Token.Doctype doctype = new Token.Doctype();
        boolean result = builder.process(doctype);
        
        assertTrue("Doctype should be processed successfully", result);
        assertEquals(XML_NAMESPACE, builder.defaultNamespace());
    }

    @Test
    public void shouldProcessCommentToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(">: ", ">: ");
        Parser parser = new Parser(builder);
        StreamParser streamParser = new StreamParser(parser);
        streamParser.parseFragment(SVG_NAMESPACE, document, XML_NAMESPACE);
        Tokeniser tokeniser = new Tokeniser(builder);
        
        Token.Comment comment = tokeniser.commentPending;
        boolean result = builder.process(comment);
        
        assertTrue("Comment should be processed successfully", result);
        assertEquals(XML_NAMESPACE, builder.defaultNamespace());
    }

    @Test
    public void shouldProcessEndTagToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Parser parser = new Parser(builder);
        StreamParser streamParser = new StreamParser(parser);
        Element element = new Element(MATHML_NAMESPACE, XML_NAMESPACE);
        streamParser.parseFragment(XML_NAMESPACE, element, XHTML_NAMESPACE);
        
        Token.EndTag endTag = new Token.EndTag(builder);
        boolean result = builder.process(endTag);
        
        assertTrue("End tag should be processed successfully", result);
        assertEquals(XML_NAMESPACE, builder.defaultNamespace());
    }

    // Element manipulation tests

    @Test
    public void shouldProcessStartTagAndReturnElement() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Parser parser = new Parser(builder);
        StreamParser streamParser = new StreamParser(parser);
        Element element = new Element(SVG_NAMESPACE, XHTML_NAMESPACE);
        streamParser.parseFragment("7~n", element, "7~n");
        
        boolean result = builder.processStartTag(SVG_NAMESPACE);
        assertTrue("Start tag should be processed successfully", result);
        
        Element poppedElement = builder.pop();
        assertEquals(SVG_NAMESPACE, poppedElement.tagName());
        assertEquals(XML_NAMESPACE, builder.defaultNamespace());
    }

    @Test
    public void shouldInsertLeafNodeSuccessfully() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(">: ", ">: ");
        Parser parser = new Parser(builder);
        StreamParser streamParser = new StreamParser(parser);
        streamParser.parseFragment(SVG_NAMESPACE, document, XML_NAMESPACE);
        
        Comment comment = new Comment("KRq7");
        builder.insertLeafNode(comment);
        
        assertEquals(XML_NAMESPACE, builder.defaultNamespace());
    }

    @Test
    public void shouldInsertDoctypeSuccessfully() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Parser parser = new Parser(builder);
        StreamParser streamParser = new StreamParser(parser);
        Element element = new Element(XHTML_NAMESPACE, XML_NAMESPACE);
        streamParser.parseFragment("z>!GVh", element, "u");
        
        Token.Doctype doctype = new Token.Doctype();
        builder.insertDoctypeFor(doctype);
        
        assertEquals(XML_NAMESPACE, builder.defaultNamespace());
    }

    @Test
    public void shouldInsertCommentSuccessfully() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(">: ", ">: ");
        Parser parser = new Parser(builder);
        StreamParser streamParser = new StreamParser(parser);
        streamParser.parseFragment("}VF\"~1WF,kf?;\"Lf2", document, "");
        
        Token.Comment comment = new Token.Comment();
        builder.insertCommentFor(comment);
        
        assertEquals(XML_NAMESPACE, builder.defaultNamespace());
    }

    @Test
    public void shouldInsertXmlDeclarationSuccessfully() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Parser parser = new Parser(builder);
        StreamParser streamParser = new StreamParser(parser);
        Element element = new Element("Doctype", "nfHa(P+.f");
        streamParser.parseFragment("$M{RJ]hM9&ek.Vtk$I", element, XHTML_NAMESPACE);
        Tokeniser tokeniser = new Tokeniser(builder);
        
        Token.XmlDecl xmlDecl = tokeniser.createXmlDeclPending(true);
        builder.insertXmlDeclarationFor(xmlDecl);
        
        assertEquals(XML_NAMESPACE, builder.defaultNamespace());
    }

    // Error handling tests

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenParsingWithNullBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        builder.parse(",Ix<T(pi#>?mbGzH", null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenParsingNullInput() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        builder.parse((String) null, (String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenParsingReaderWithNullBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        PipedWriter writer = new PipedWriter();
        PipedReader reader = new PipedReader(writer);
        
        builder.parse(reader, null);
    }

    @Test(expected = UncheckedIOException.class)
    public void shouldThrowExceptionWhenParsingDisconnectedPipe() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        PipedReader reader = new PipedReader();
        
        builder.parse(reader, "kxrewut");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenInsertingNullLeafNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Parser parser = new Parser(builder);
        StreamParser streamParser = new StreamParser(parser);
        Document document = parser.parseInput("class", "class");
        streamParser.parseFragment("{FDP|4G0#6", document, "_',=~G~eEf3?aX_HU");
        
        builder.insertLeafNode(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenPoppingEmptyStack() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        builder.pop();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenInitializingParseWithNullReader() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Parser parser = Parser.xmlParser();
        
        builder.initialiseParse(null, "ScriptData", parser);
    }

    @Test(expected = UncheckedIOException.class)
    public void shouldThrowExceptionWhenInitializingParseWithDisconnectedPipe() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        PipedReader reader = new PipedReader();
        Parser parser = new Parser(builder);
        
        builder.initialiseParse(reader, "IuI=", parser);
    }

    // State validation tests

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenProcessingStartTagWithoutInitialization() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("jHQIs@:5#c&", "2#0f[w|.T$lBU[oa^f");
        
        builder.processStartTag(":contains(%s)");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenPoppingWithoutInitialization() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse(",Ix<T(pi#>?mbGzH", ",Ix<T(pi#>?mbGzH");
        
        builder.pop();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenCompletingParseFragmentWithoutInitialization() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        builder.completeParseFragment();
    }
}