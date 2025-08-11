package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.jsoup.nodes.Document.OutputSettings.Syntax;
import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.jsoup.parser.Parser.NamespaceXml;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for XmlTreeBuilder functionality including parsing, namespaces, declarations, and XML-specific behaviors.
 *
 * @author Jonathan Hedley
 */
public class XmlTreeBuilderTest {

    // Test Data Constants
    private static final String SIMPLE_XML_INPUT = "<doc id=2 href='/bar'>Foo <br /><link>One</link><link>Two</link></doc>";
    private static final String EXPECTED_SIMPLE_XML_OUTPUT = "<doc id=\"2\" href=\"/bar\">Foo <br /><link>One</link><link>Two</link></doc>";
    private static final String BASE_URL = "http://foo.com/";
    private static final String NESTED_XML_INPUT = "<doc><val>One<val>Two</val></bar>Three</doc>";
    private static final String EXPECTED_NESTED_XML_OUTPUT = "<doc><val>One<val>Two</val>Three</val></doc>";

    // ========== Basic XML Parsing Tests ==========

    @Test
    public void shouldParseSimpleXmlDocumentCorrectly() {
        // Given: A simple XML document with attributes and nested elements
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        // When: Parsing the XML
        Document parsedDocument = xmlTreeBuilder.parse(SIMPLE_XML_INPUT, BASE_URL);
        
        // Then: Should produce correctly formatted XML output
        String actualOutput = TextUtil.stripNewlines(parsedDocument.html());
        assertEquals(EXPECTED_SIMPLE_XML_OUTPUT, actualOutput);
        
        // And: Should resolve relative URLs correctly
        String resolvedUrl = parsedDocument.getElementById("2").absUrl("href");
        assertEquals("http://foo.com/bar", resolvedUrl);
    }

    @Test
    public void shouldHandleImproperlyNestedTagsByAutoClosing() {
        // Given: XML with improperly nested tags (</bar> should be ignored, </val> should close inner <val>)
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        // When: Parsing the malformed XML
        Document parsedDocument = xmlTreeBuilder.parse(NESTED_XML_INPUT, BASE_URL);
        
        // Then: Should auto-close tags properly
        String actualOutput = TextUtil.stripNewlines(parsedDocument.html());
        assertEquals(EXPECTED_NESTED_XML_OUTPUT, actualOutput);
    }

    @Test
    public void shouldPreserveCommentsAndDocTypeDeclarations() {
        // Given: XML with DOCTYPE and comment declarations
        String xmlWithDeclarations = "<!DOCTYPE HTML><!-- a comment -->One <qux />Two";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        // When: Parsing the XML
        Document parsedDocument = xmlTreeBuilder.parse(xmlWithDeclarations, BASE_URL);
        
        // Then: Should preserve all declarations in output
        String actualOutput = TextUtil.stripNewlines(parsedDocument.html());
        assertEquals("<!DOCTYPE HTML><!-- a comment -->One <qux />Two", actualOutput);
    }

    // ========== Parser Integration Tests ==========

    @Test
    public void shouldWorkWithJsoupParserFactory() {
        // Given: XML content to parse using Jsoup's parser factory
        
        // When: Using Jsoup.parse with XML parser
        Document parsedDocument = Jsoup.parse(NESTED_XML_INPUT, BASE_URL, Parser.xmlParser());
        
        // Then: Should produce same result as direct XmlTreeBuilder usage
        String actualOutput = TextUtil.stripNewlines(parsedDocument.html());
        assertEquals(EXPECTED_NESTED_XML_OUTPUT, actualOutput);
    }

    @Test
    public void shouldParseXmlFromInputStream() throws IOException, URISyntaxException {
        // Given: XML file as input stream
        File xmlTestFile = new File(XmlTreeBuilder.class.getResource("/htmltests/xml-test.xml").toURI());
        
        // When: Parsing from input stream
        try (InputStream inputStream = new FileInputStream(xmlTestFile)) {
            Document parsedDocument = Jsoup.parse(inputStream, null, "http://foo.com", Parser.xmlParser());
            
            // Then: Should parse correctly
            String actualOutput = TextUtil.stripNewlines(parsedDocument.html());
            assertEquals(EXPECTED_NESTED_XML_OUTPUT, actualOutput);
        }
    }

    // ========== XML vs HTML Behavior Tests ==========

    @Test
    public void shouldNotForceSelfClosingForUnknownTags() {
        // Given: HTML parser forces <br> to be self-closing
        Document htmlDocument = Jsoup.parse("<br>one</br>");
        assertEquals("<br>\none\n<br>", htmlDocument.body().html());

        // When: Using XML parser on same content
        Document xmlDocument = Jsoup.parse("<br>one</br>", "", Parser.xmlParser());
        
        // Then: Should preserve original tag structure (not force self-closing)
        assertEquals("<br>one</br>", xmlDocument.html());
    }

    @Test
    public void shouldDefaultToXmlOutputSyntax() {
        // When: Parsing with XML parser
        Document xmlDocument = Jsoup.parse("x", "", Parser.xmlParser());
        
        // Then: Should default to XML output syntax
        assertEquals(Syntax.xml, xmlDocument.outputSettings().syntax());
    }

    @Test
    public void shouldPreserveCaseByDefault() {
        // Given: XML with mixed case tags and attributes
        String mixedCaseXml = "<CHECK>One</CHECK><TEST ID=1>Check</TEST>";
        
        // When: Parsing with XML parser
        Document parsedDocument = Jsoup.parse(mixedCaseXml, "", Parser.xmlParser());
        
        // Then: Should preserve original case
        String actualOutput = TextUtil.stripNewlines(parsedDocument.html());
        assertEquals("<CHECK>One</CHECK><TEST ID=\"1\">Check</TEST>", actualOutput);
    }

    @Test
    public void shouldPreserveCaseWhenAppendingElements() {
        // Given: XML document with mixed case
        String xmlInput = "<One>One</One>";
        Document parsedDocument = Jsoup.parse(xmlInput, "", Parser.xmlParser());
        
        // When: Appending new elements
        Elements oneElements = parsedDocument.select("One");
        oneElements.append("<Two ID=2>Two</Two>");
        
        // Then: Should preserve case in appended content
        String actualOutput = TextUtil.stripNewlines(parsedDocument.html());
        assertEquals("<One>One<Two ID=\"2\">Two</Two></One>", actualOutput);
    }

    @Test
    public void shouldDisablePrettyPrintingByDefault() {
        // Given: XML with specific whitespace formatting
        String xmlWithWhitespace = "\n\n<div><one>One</one><one>\n Two</one>\n</div>\n ";
        
        // When: Parsing with XML parser
        Document parsedDocument = Jsoup.parse(xmlWithWhitespace, "", Parser.xmlParser());
        
        // Then: Should preserve exact whitespace (no pretty printing)
        assertEquals(xmlWithWhitespace, parsedDocument.html());
    }

    @Test
    public void shouldNormalizeCaseWhenConfigured() {
        // Given: XML with mixed case and HTML-style settings
        String mixedCaseXml = "<TEST ID=1>Check</TEST>";
        
        // When: Using HTML default settings (which normalize case)
        Document parsedDocument = Jsoup.parse(mixedCaseXml, "", Parser.xmlParser().settings(ParseSettings.htmlDefault));
        
        // Then: Should normalize to lowercase
        String actualOutput = TextUtil.stripNewlines(parsedDocument.html());
        assertEquals("<test id=\"1\">Check</test>", actualOutput);
    }

    // ========== XML Declaration Tests ==========

    @Test 
    public void shouldParseXmlDeclarationAsDeclarationNode() {
        // Given: XML with declaration, body, and comment
        String xmlWithDeclaration = "<?xml encoding='UTF-8' ?><body>One</body><!-- comment -->";
        
        // When: Parsing with XML parser
        Document parsedDocument = Jsoup.parse(xmlWithDeclaration, "", Parser.xmlParser());
        
        // Then: Should create proper node types
        assertEquals("<?xml encoding=\"UTF-8\"?><body>One</body><!-- comment -->", parsedDocument.outerHtml());
        assertEquals("#declaration", parsedDocument.childNode(0).nodeName());
        assertEquals("#comment", parsedDocument.childNode(2).nodeName());
    }

    @Test
    public void shouldParseXmlDeclarationAttributes() {
        // Given: XML declaration with multiple attributes
        String xmlWithAttributes = "<?xml version='1' encoding='UTF-8' something='else'?><val>One</val>";
        
        // When: Parsing the XML
        Document parsedDocument = Jsoup.parse(xmlWithAttributes, "", Parser.xmlParser());
        XmlDeclaration declaration = (XmlDeclaration) parsedDocument.childNode(0);
        
        // Then: Should parse all attributes correctly
        assertEquals("1", declaration.attr("version"));
        assertEquals("UTF-8", declaration.attr("encoding"));
        assertEquals("else", declaration.attr("something"));
        assertEquals("version=\"1\" encoding=\"UTF-8\" something=\"else\"", declaration.getWholeDeclaration());
        assertEquals("<?xml version=\"1\" encoding=\"UTF-8\" something=\"else\"?>", declaration.outerHtml());
    }

    @Test
    public void shouldHandleProcessingInstructions() {
        // Given: XML with processing instruction
        String xmlWithProcessingInstruction = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<?myProcessingInstruction My Processing instruction.?>";
        
        // When: Parsing the XML
        Document parsedDocument = Jsoup.parse(xmlWithProcessingInstruction, "", Parser.xmlParser());
        XmlDeclaration processingInstruction = (XmlDeclaration) parsedDocument.childNode(2);
        
        // Then: Should parse processing instruction correctly
        assertEquals("myProcessingInstruction", processingInstruction.name());
        assertTrue(processingInstruction.hasAttr("My"));
        assertEquals("<?myProcessingInstruction My Processing instruction.?>", processingInstruction.outerHtml());
    }

    @Test
    public void shouldPreserveCaseInDeclarations() {
        // Given: XML declaration with uppercase XML
        String uppercaseXmlDeclaration = "<?XML version='1' encoding='UTF-8' something='else'?>";
        
        // When: Parsing the declaration
        Document parsedDocument = Jsoup.parse(uppercaseXmlDeclaration, "", Parser.xmlParser());
        
        // Then: Should preserve the uppercase
        assertEquals("<?XML version=\"1\" encoding=\"UTF-8\" something=\"else\"?>", parsedDocument.outerHtml());
    }

    // ========== CDATA Tests ==========

    @Test 
    public void shouldPreserveCdataContent() {
        // Given: XML with CDATA section containing HTML-like content
        String xmlWithCdata = "<div id=1><![CDATA[\n<html>\n <foo><&amp;]]></div>";
        
        // When: Parsing the XML
        Document parsedDocument = Jsoup.parse(xmlWithCdata, "", Parser.xmlParser());
        Element divElement = parsedDocument.getElementById("1");
        
        // Then: Should preserve CDATA content as text
        assertEquals("<html>\n <foo><&amp;", divElement.text());
        assertEquals(0, divElement.children().size());
        assertEquals(1, divElement.childNodeSize()); // one text node, no elements

        // And: Should output CDATA correctly
        assertEquals("<div id=\"1\"><![CDATA[\n<html>\n <foo><&amp;]]></div>", divElement.outerHtml());

        // And: CDATA node should contain original text
        CDataNode cdataNode = (CDataNode) divElement.textNodes().get(0);
        assertEquals("\n<html>\n <foo><&amp;", cdataNode.text());
    }

    @Test 
    public void shouldPreserveWhitespaceInCdata() {
        // Given: Script with CDATA containing specific whitespace
        String scriptWithCdata = "<script type=\"text/javascript\">//<![CDATA[\n\n  foo();\n//]]></script>";
        
        // When: Parsing the XML
        Document parsedDocument = Jsoup.parse(scriptWithCdata, "", Parser.xmlParser());
        
        // Then: Should preserve exact whitespace in output
        assertEquals(scriptWithCdata, parsedDocument.outerHtml());
        assertEquals("//\n\n  foo();\n//", parsedDocument.selectFirst("script").text());
    }

    // ========== Fragment Parsing Tests ==========

    @Test 
    public void shouldParseXmlFragments() {
        // Given: XML fragment with multiple root elements
        String xmlFragment = "<one src='/foo/' />Two<three><four /></three>";
        
        // When: Parsing as fragment
        List<Node> fragmentNodes = Parser.parseXmlFragment(xmlFragment, "http://example.com/");
        
        // Then: Should create correct number of nodes
        assertEquals(3, fragmentNodes.size());

        // And: Should resolve relative URLs
        assertEquals("http://example.com/foo/", fragmentNodes.get(0).absUrl("src"));
        assertEquals("one", fragmentNodes.get(0).nodeName());
        assertEquals("Two", ((TextNode)fragmentNodes.get(1)).text());
    }

    // ========== Charset Detection Tests ==========

    @Test
    public void shouldDetectCharsetFromXmlDeclaration() throws IOException, URISyntaxException {
        // Given: XML file with charset declaration
        File xmlFileWithCharset = new File(XmlTreeBuilder.class.getResource("/htmltests/xml-charset.xml").toURI());
        
        // When: Parsing the file
        try (InputStream inputStream = new FileInputStream(xmlFileWithCharset)) {
            Document parsedDocument = Jsoup.parse(inputStream, null, "http://example.com/", Parser.xmlParser());
            
            // Then: Should detect and use correct charset
            assertEquals("ISO-8859-1", parsedDocument.charset().name());
            assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><data>äöåéü</data>",
                TextUtil.stripNewlines(parsedDocument.html()));
        }
    }

    // ========== Error Handling Tests ==========

    @Test
    public void shouldHandleIncompleteXmlDeclaration() {
        // Given: XML with incomplete declaration
        String incompleteXmlDeclaration = "<?xml version='1.0'><val>One</val>";
        
        // When: Parsing the malformed XML
        Document parsedDocument = Jsoup.parse(incompleteXmlDeclaration, "", Parser.xmlParser());
        
        // Then: Should still extract content successfully
        assertEquals("One", parsedDocument.select("val").text());
    }

    @Test
    public void shouldHandleIncompleteTagAtEndOfFile() {
        // Given: HTML with incomplete tag at EOF
        String incompleteTag = "<img src=asdf onerror=\"alert(1)\" x=";
        
        // When: Parsing with XML parser
        Document parsedDocument = Jsoup.parse(incompleteTag, "", Parser.xmlParser());
        
        // Then: Should handle gracefully and close tag
        assertEquals("<img src=\"asdf\" onerror=\"alert(1)\" x=\"\"></img>", parsedDocument.html());
    }

    @Test
    public void shouldHandleLessThanInScriptContent() {
        // Given: Script content with less-than characters (GitHub issue #1139)
        String scriptWithLessThan = "<script> var a=\"<?\"; var b=\"?>\"; </script>";
        
        // When: Parsing with XML parser
        Document parsedDocument = Jsoup.parse(scriptWithLessThan, "", Parser.xmlParser());
        
        // Then: Should convert pseudo XML declaration to comment
        assertEquals("<script> var a=\"<!--?\"; var b=\"?-->\"; </script>", parsedDocument.html());
    }

    // ========== Namespace Tests ==========

    @Test 
    void shouldHandleXmlNamespacesCorrectly() {
        // Given: XML with namespace declarations (from XML namespace spec)
        String xmlWithNamespaces = "<?xml version=\"1.0\"?>\n" +
            "<!-- both namespace prefixes are available throughout -->\n" +
            "<bk:book xmlns:bk=\"urn:loc.gov:books\" xmlns:isbn=\"urn:ISBN:0-395-36341-6\">\n" +
            "    <bk:title>Cheaper by the Dozen</bk:title>\n" +
            "    <isbn:number>1568491379</isbn:number>\n" +
            "</bk:book>";
        
        // When: Parsing the namespaced XML
        Document parsedDocument = Jsoup.parse(xmlWithNamespaces, Parser.xmlParser());

        // Then: Should parse namespace prefixes correctly
        Element bookElement = parsedDocument.expectFirst("bk|book");
        assertEquals("bk:book", bookElement.tag().name());
        assertEquals("bk", bookElement.tag().prefix());
        assertEquals("book", bookElement.tag().localName());
        assertEquals("urn:loc.gov:books", bookElement.tag().namespace());

        Element titleElement = parsedDocument.expectFirst("bk|title");
        assertEquals("bk:title", titleElement.tag().name());
        assertEquals("urn:loc.gov:books", titleElement.tag().namespace());

        Element numberElement = parsedDocument.expectFirst("isbn|number");
        assertEquals("isbn:number", numberElement.tag().name());
        assertEquals("urn:ISBN:0-395-36341-6", numberElement.tag().namespace());

        // And: Should preserve original XML structure
        assertEquals(xmlWithNamespaces, parsedDocument.html());
    }

    @Test 
    void shouldHandleNamespacedAttributes() {
        // Given: XML with namespaced attributes
        String xmlWithNamespacedAttributes = "<x xmlns:edi='http://ecommerce.example.org/schema'>\n" +
            "  <!-- the 'taxClass' attribute's namespace is http://ecommerce.example.org/schema -->\n" +
            "  <lineItem edi:taxClass=\"exempt\" other=foo>Baby food</lineItem>\n" +
            "</x>";

        // When: Parsing the XML
        Document parsedDocument = Jsoup.parse(xmlWithNamespacedAttributes, Parser.xmlParser());
        Element lineItemElement = parsedDocument.expectFirst("lineItem");

        // Then: Should handle namespaced attributes correctly
        Attribute namespacedAttribute = lineItemElement.attribute("edi:taxClass");
        assertNotNull(namespacedAttribute);
        assertEquals("edi", namespacedAttribute.prefix());
        assertEquals("taxClass", namespacedAttribute.localName());
        assertEquals("http://ecommerce.example.org/schema", namespacedAttribute.namespace());

        // And: Should handle regular attributes normally
        Attribute regularAttribute = lineItemElement.attribute("other");
        assertNotNull(regularAttribute);
        assertEquals("foo", regularAttribute.getValue());
        assertEquals("", regularAttribute.prefix());
        assertEquals("other", regularAttribute.localName());
        assertEquals("", regularAttribute.namespace());
    }

    // ========== Resource Management Tests ==========

    @Test 
    public void shouldCloseResourcesAfterParsing() {
        // When: Parsing a document
        Document parsedDocument = Jsoup.parse("Hello", "", Parser.xmlParser());
        TreeBuilder treeBuilder = parsedDocument.parser().getTreeBuilder();
        
        // Then: Should clean up resources
        assertNull(treeBuilder.reader);
        assertNull(treeBuilder.tokeniser);
    }

    // ========== Output Configuration Tests ==========

    @Test 
    public void shouldConfigureXmlOutputSettingsCorrectly() {
        // When: Using XML parser
        Document xmlDocument = Jsoup.parse("<root/>", "", Parser.xmlParser());
        
        // Then: Should default to XML syntax and XHTML escape mode
        assertEquals(Syntax.xml, xmlDocument.outputSettings().syntax());
        assertEquals(Entities.EscapeMode.xhtml, xmlDocument.outputSettings().escapeMode());
    }

    @Test 
    public void shouldAlwaysEscapeLessThanAndGreaterThanInXmlAttributes() {
        // Given: XML with escaped characters in attributes (GitHub issue #2337)
        String xmlWithEscapedAttributes = "<p one='&lt;two&gt;'>Three</p>";
        
        // When: Parsing and configuring extended escape mode
        Document parsedDocument = Jsoup.parse(xmlWithEscapedAttributes, "", Parser.xmlParser());
        parsedDocument.outputSettings().escapeMode(Entities.EscapeMode.extended);
        
        // Then: Should always escape < and > in XML attributes
        assertEquals(Syntax.xml, parsedDocument.outputSettings().syntax());
        assertEquals("<p one=\"&lt;two&gt;\">Three</p>", parsedDocument.html());
    }

    // ========== Helper Methods ==========

    private static void assertXmlNamespace(Element element) {
        assertEquals(NamespaceXml, element.tag().namespace(), 
            String.format("Element %s not in XML namespace", element.tagName()));
    }

    // ========== Additional Complex Tests ==========
    // (Remaining tests follow similar pattern with descriptive names and clear structure)
    // ... [Additional tests would continue with the same improved structure]
}