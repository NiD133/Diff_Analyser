package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.integration.ParseTest;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import static org.jsoup.TextUtil.normalizeSpaces;
import static org.jsoup.nodes.Document.OutputSettings.Syntax.xml;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for W3CDom class that converts JSoup documents to W3C DOM documents.
 * 
 * This test suite covers:
 * - Basic conversion functionality
 * - Namespace handling
 * - Invalid HTML/XML handling
 * - XPath integration
 * - Output formatting (HTML vs XML)
 */
public class W3CDomTest {

    // Test Data Constants
    private static final String SIMPLE_HTML = 
        "<html><head><title>W3c</title></head><body><p class='one' id=12>Text</p><!-- comment --><invalid>What<script>alert('!')";
    
    private static final String EXPECTED_XML_OUTPUT = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>W3c</title></head><body><p class=\"one\" id=\"12\">Text</p><!-- comment --><invalid>What<script>alert('!')</script></invalid></body></html>";
    
    private static final String UNICODE_ATTRIBUTES_HTML = 
        "<!DOCTYPE html><html><head></head><body><p hành=\"1\" hình=\"2\">unicode attr names</p></body></html>";
    
    private static final String INVALID_ATTRIBUTES_HTML = 
        "<html><head></head><body style=\"color: red\" \" name\"></body></html>";

    // Helper Methods
    
    /**
     * Parses XML string into W3C Document with configurable namespace awareness.
     */
    private static Document parseXmlDocument(String xml, boolean namespaceAware) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(namespaceAware);
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            // Handle HTML5 doctype compatibility
            builder.setEntityResolver((publicId, systemId) -> {
                if (systemId.contains("about:legacy-compat")) {
                    return new InputSource(new StringReader(""));
                } else {
                    return null;
                }
            });
            
            Document document = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            document.normalizeDocument();
            return document;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse XML: " + xml, e);
        }
    }

    /**
     * Executes XPath query against W3C document and returns matching nodes.
     */
    private NodeList executeXPathQuery(Document document, String xpathQuery) throws XPathExpressionException {
        XPathExpression xpath = XPathFactory.newInstance().newXPath().compile(xpathQuery);
        return (NodeList) xpath.evaluate(document, XPathConstants.NODE);
    }

    /**
     * Converts JSoup document to W3C and outputs as string with specified format.
     */
    private String convertAndOutput(String htmlInput, boolean outputAsHtml) {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlInput);
        Document w3cDoc = W3CDom.convert(jsoupDoc);
        
        Map<String, String> outputProperties = outputAsHtml ? W3CDom.OutputHtml() : W3CDom.OutputXml();
        return normalizeSpaces(W3CDom.asString(w3cDoc, outputProperties));
    }

    /**
     * Asserts two strings are equal ignoring case differences.
     */
    private void assertEqualsIgnoringCase(String expected, String actual) {
        assertEquals(expected.toLowerCase(Locale.ROOT), actual.toLowerCase(Locale.ROOT));
    }

    // Basic Conversion Tests

    @Test
    public void shouldConvertSimpleHtmlToW3cDocument() {
        // Given: A simple HTML document
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(SIMPLE_HTML);

        // When: Converting to W3C DOM
        W3CDom converter = new W3CDom();
        Document w3cDoc = converter.fromJsoup(jsoupDoc);

        // Then: Should not add META tags automatically
        NodeList metaTags = w3cDoc.getElementsByTagName("META");
        assertEquals(0, metaTags.getLength(), "Should not add META tags during conversion");

        // And: Should produce correct XML output
        String xmlOutput = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertEquals(EXPECTED_XML_OUTPUT, TextUtil.stripNewlines(xmlOutput));

        // And: Should be parseable as valid XML
        Document roundTripDoc = parseXmlDocument(xmlOutput, true);
        assertEquals("Text", roundTripDoc.getElementsByTagName("p").item(0).getTextContent());
    }

    @Test
    public void shouldSupportCustomOutputProperties() {
        // Given: A simple HTML document converted to W3C
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(SIMPLE_HTML);
        W3CDom converter = new W3CDom();
        Document w3cDoc = converter.fromJsoup(jsoupDoc);

        // When: Setting custom output properties (indentation)
        Map<String, String> customProperties = W3CDom.OutputXml();
        customProperties.put(OutputKeys.INDENT, "yes");
        String indentedOutput = W3CDom.asString(w3cDoc, customProperties);

        // Then: Output should be longer due to indentation
        String standardOutput = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertTrue(indentedOutput.length() > standardOutput.length(), 
                  "Indented output should be longer than standard output");

        // And: Content should remain the same when normalized
        String expectedContent = 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>W3c</title></head><body><p class=\"one\" id=\"12\">Text</p><!-- comment --><invalid>What<script>alert('!')</script></invalid></body></html>";
        assertEquals(expectedContent, TextUtil.stripNewlines(indentedOutput));
    }

    // Namespace Handling Tests

    @Test
    public void shouldPreserveNamespacesFromXhtmlDocument() throws IOException {
        // Given: An XHTML document with multiple namespaces
        File namespaceFile = ParseTest.getFile("/htmltests/namespaces.xhtml");
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(namespaceFile, "UTF-8", "", Parser.xmlParser());

        // When: Converting to W3C DOM
        W3CDom converter = new W3CDom();
        Document w3cDoc = converter.fromJsoup(jsoupDoc);

        // Then: Should preserve XHTML namespace on root element
        Node htmlElement = w3cDoc.getChildNodes().item(0);
        assertEquals("http://www.w3.org/1999/xhtml", htmlElement.getNamespaceURI());
        assertEquals("html", htmlElement.getLocalName());
        assertEquals("html", htmlElement.getNodeName());

        // And: Should inherit default namespace for standard elements
        Node headElement = htmlElement.getFirstChild().getNextSibling();
        assertEquals("http://www.w3.org/1999/xhtml", headElement.getNamespaceURI());
        assertEquals("head", headElement.getLocalName());

        // And: Should preserve custom namespaces with prefixes
        Node epubTitleElement = htmlElement.getChildNodes().item(3).getChildNodes().item(3);
        assertEquals("Check", epubTitleElement.getTextContent());
        assertEquals("http://www.idpf.org/2007/ops", epubTitleElement.getNamespaceURI());
        assertEquals("title", epubTitleElement.getLocalName());
        assertEquals("epub:title", epubTitleElement.getNodeName());
    }

    @Test
    public void shouldHandleUndeclaredNamespacesAsLocalNames() {
        // Given: HTML with Facebook-style namespace prefix (undeclared)
        String htmlWithUndeclaredNamespace = "<fb:like>One</fb:like>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlWithUndeclaredNamespace);

        // When: Converting to W3C DOM
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        // Then: Root element should have XHTML namespace
        Node htmlElement = w3cDoc.getFirstChild();
        assertEquals("http://www.w3.org/1999/xhtml", htmlElement.getNamespaceURI());
        assertEquals("html", htmlElement.getLocalName());

        // And: Undeclared namespace should be treated as local name in XHTML namespace
        Node facebookLikeElement = htmlElement.getFirstChild().getNextSibling().getFirstChild();
        assertEquals("http://www.w3.org/1999/xhtml", facebookLikeElement.getNamespaceURI());
        assertEquals("like", facebookLikeElement.getLocalName());
        assertEquals("fb:like", facebookLikeElement.getNodeName());
    }

    @Test
    public void shouldAllowDisablingNamespaceAwareness() throws XPathExpressionException {
        // Given: W3CDom converter with namespace awareness disabled
        W3CDom converter = new W3CDom();
        assertTrue(converter.namespaceAware(), "Should be namespace aware by default");
        
        converter.namespaceAware(false);
        assertFalse(converter.namespaceAware(), "Should be able to disable namespace awareness");

        // When: Converting HTML with namespace declarations
        String htmlWithNamespace = "<html xmlns='http://www.w3.org/1999/xhtml'><body id='One'><div>hello</div></body></html>";
        Document w3cDoc = converter.fromJsoup(Jsoup.parse(htmlWithNamespace));

        // Then: Should be able to query without namespace prefixes
        NodeList bodyElements = executeXPathQuery(w3cDoc, "//body");
        assertEquals("div", bodyElements.item(0).getLocalName());
    }

    // Invalid Content Handling Tests

    @Test
    public void shouldSanitizeInvalidAttributeNames() {
        // Given: HTML with invalid attribute names (containing quotes)
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(INVALID_ATTRIBUTES_HTML);
        Element bodyElement = jsoupDoc.select("body").first();
        
        // Verify JSoup preserves the invalid attributes
        assertTrue(bodyElement.hasAttr("\""), "JSoup should preserve quote attribute");
        assertTrue(bodyElement.hasAttr("name\""), "JSoup should preserve attribute ending with quote");

        // When: Converting to W3C DOM
        Document w3cDoc = W3CDom.convert(jsoupDoc);
        String xmlOutput = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

        // Then: Should sanitize invalid attribute names by replacing with underscores
        String expectedOutput = 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body _=\"\" name_=\"\" style=\"color: red\"/></html>";
        assertEquals(expectedOutput, xmlOutput);
    }

    @Test
    public void shouldHandleInvalidTagNamesAsText() {
        // Given: HTML with invalid tag name (Japanese characters)
        String htmlWithInvalidTag = "<インセンティブで高収入！>Text <p>More</p>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlWithInvalidTag);

        // When: Converting to W3C DOM
        Document w3cDoc = W3CDom.convert(jsoupDoc);
        String xmlOutput = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

        // Then: Should escape invalid tag as text content
        String expectedOutput = 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body>&lt;インセンティブで高収入！&gt;Text <p>More</p></body></html>";
        assertEquals(expectedOutput, xmlOutput);
    }

    @Test
    public void shouldHandleElementsWithLessThanInNames() {
        // Given: HTML elements with '<' in their names (valid in HTML5)
        String htmlWithLessThanInNames = "<foo<bar attr<name=\"123\"><b>Text</b></foo<bar>";
        String expectedXmlOutput = "<foo_bar attr_name=\"123\"><b>Text</b></foo_bar>";

        // When: Parsing as HTML (should preserve < in names)
        org.jsoup.nodes.Document htmlDoc = Jsoup.parse(htmlWithLessThanInNames);
        String htmlOutput = htmlDoc.body().html();
        assertEquals(htmlWithLessThanInNames, normalizeSpaces(htmlOutput));

        // And: Converting to XML syntax (should replace < with _)
        htmlDoc.outputSettings().syntax(xml);
        String xmlOutput = htmlDoc.body().html();
        assertEquals(expectedXmlOutput, normalizeSpaces(xmlOutput));

        // Then: W3C should be able to parse the XML version
        Document w3cXmlDoc = parseXmlDocument(xmlOutput, true);
        NodeList elementsWithSanitizedNames = w3cXmlDoc.getElementsByTagName("foo_bar");
        assertEquals(1, elementsWithSanitizedNames.getLength());
        assertEquals("123", elementsWithSanitizedNames.item(0).getAttributes().getNamedItem("attr_name").getTextContent());

        // And: W3CDom conversion should also sanitize the names
        Document w3cDoc = W3CDom.convert(htmlDoc);
        NodeList w3cElements = w3cDoc.getElementsByTagName("foo_bar");
        assertEquals(1, w3cElements.getLength());
        assertEquals("123", w3cElements.item(0).getAttributes().getNamedItem("attr_name").getTextContent());
    }

    // Unicode and Encoding Tests

    @Test
    public void shouldPreserveUnicodeAttributeNamesInHtmlOutput() {
        // Given: HTML with Unicode attribute names
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(UNICODE_ATTRIBUTES_HTML);

        // When: Converting to W3C and outputting as HTML
        Document w3cDoc = W3CDom.convert(jsoupDoc);
        String htmlOutput = W3CDom.asString(w3cDoc, W3CDom.OutputHtml());

        // Then: Should preserve Unicode attribute names
        String expectedOutput = 
            "<!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p hành=\"1\" hình=\"2\">unicode attr names</p></body></html>";
        assertEquals(expectedOutput, TextUtil.stripNewlines(htmlOutput));
    }

    @Test
    public void shouldCoerceUnicodeAttributeNamesInXmlSyntax() {
        // Given: HTML document with XML syntax and Unicode attribute names
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(UNICODE_ATTRIBUTES_HTML);
        jsoupDoc.outputSettings().syntax(xml);

        // When: Converting to W3C and outputting as HTML
        Document w3cDoc = W3CDom.convert(jsoupDoc);
        String htmlOutput = W3CDom.asString(w3cDoc, W3CDom.OutputHtml());

        // Then: Should coerce problematic Unicode characters in attribute names
        String expectedOutput = 
            "<!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p h_nh=\"2\">unicode attr names coerced</p></body></html>";
        assertEquals(expectedOutput, TextUtil.stripNewlines(htmlOutput));
    }

    // XPath Integration Tests

    @Test
    public void shouldSupportXPathQueriesWithNamespaces() throws XPathExpressionException {
        // Given: HTML document converted to W3C DOM
        W3CDom converter = new W3CDom();
        String simpleHtml = "<html><body><div>hello</div></body></html>";
        Document w3cDoc = converter.fromJsoup(Jsoup.parse(simpleHtml));

        // When: Executing namespace-aware XPath query
        NodeList bodyElements = executeXPathQuery(w3cDoc, "//*[local-name()=\"body\"]");

        // Then: Should find the body element
        assertNotNull(bodyElements);
        assertEquals("div", bodyElements.item(0).getLocalName());

        // And: Non-namespace-aware queries should fail on namespace-aware documents
        NodeList directBodyQuery = executeXPathQuery(w3cDoc, "//body");
        assertNull(directBodyQuery, "Direct element queries should fail on namespace-aware documents");
    }

    @Test
    public void shouldSupportXPathQueriesWithoutNamespaces() throws XPathExpressionException {
        // Given: W3CDom converter with namespace awareness disabled
        W3CDom converter = new W3CDom();
        converter.namespaceAware(false);
        
        String simpleHtml = "<html><body><div>hello</div></body></html>";
        Document w3cDoc = converter.fromJsoup(Jsoup.parse(simpleHtml));

        // When: Executing simple XPath query
        NodeList bodyElements = executeXPathQuery(w3cDoc, "//body");

        // Then: Should find the body element without namespace complications
        assertEquals(1, bodyElements.getLength());
        assertEquals("div", bodyElements.item(0).getLocalName());
    }

    // Custom Document Tests

    @Test
    public void shouldConvertToCustomW3cDocument() throws ParserConfigurationException {
        // Given: A custom W3C document and JSoup document
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><div></div></html>");
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document customW3cDoc = factory.newDocumentBuilder().newDocument();

        // When: Converting JSoup document into the custom W3C document
        W3CDom converter = new W3CDom();
        converter.convert(jsoupDoc, customW3cDoc);

        // Then: Custom document should contain the converted content
        String htmlOutput = W3CDom.asString(customW3cDoc, W3CDom.OutputHtml());
        String expectedOutput = 
            "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><div></div></body></html>";
        assertEquals(expectedOutput, htmlOutput);
    }

    // Source Node Tracking Tests

    @Test
    public void shouldMaintainLinksToOriginalJsoupNodes() {
        // Given: JSoup document with nested elements
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<body><div><p>One</div><div><p>Two");
        Element originalDiv = jsoupDoc.selectFirst("div");
        assertNotNull(originalDiv);

        // When: Converting specific element to W3C
        W3CDom converter = new W3CDom();
        Document w3cDoc = converter.fromJsoup(originalDiv);
        Node w3cDiv = converter.contextNode(w3cDoc);

        // Then: W3C nodes should link back to original JSoup nodes
        assertEquals("div", w3cDiv.getLocalName());
        assertEquals(originalDiv, w3cDiv.getUserData(W3CDom.SourceProperty));

        // And: Text nodes should also maintain links
        Node w3cTextNode = w3cDiv.getFirstChild().getFirstChild();
        assertEquals("One", w3cTextNode.getTextContent());
        assertEquals(Node.TEXT_NODE, w3cTextNode.getNodeType());

        TextNode originalTextNode = (TextNode) originalDiv.childNode(0).childNode(0);
        assertEquals(originalTextNode, w3cTextNode.getUserData(W3CDom.SourceProperty));
    }

    // DOCTYPE Handling Tests

    @Test
    public void shouldHandleVariousDoctypeDeclarations() {
        testDoctypeConversion(
            "<!DOCTYPE html><p>One</p>",
            "<!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><p>One</p></body></html>",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html SYSTEM \"about:legacy-compat\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body><p>One</p></body></html>"
        );
    }

    @Test
    public void shouldHandlePublicDoctypeDeclarations() {
        String publicDoctype = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        String htmlOutput = convertAndOutput(publicDoctype, true);
        String xmlOutput = convertAndOutput(publicDoctype, false);
        
        assertEqualsIgnoringCase(
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body></body></html>",
            htmlOutput
        );
        assertTrue(xmlOutput.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC"));
    }

    @Test
    public void shouldHandleEmptyDoctypeDeclarations() {
        // Given: HTML with empty doctype
        String htmlWithEmptyDoctype = "<!doctype>Foo";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlWithEmptyDoctype);
        
        // When: Converting to W3C DOM
        Document w3cDoc = (new W3CDom()).fromJsoup(jsoupDoc);
        
        // Then: Should not create doctype node and preserve content
        assertNull(w3cDoc.getDoctype());
        assertEquals("Foo", w3cDoc.getFirstChild().getTextContent());
    }

    private void testDoctypeConversion(String input, String expectedHtml, String expectedXml) {
        String actualHtml = convertAndOutput(input, true);
        String actualXml = convertAndOutput(input, false);
        
        assertEqualsIgnoringCase(expectedHtml, actualHtml);
        assertEqualsIgnoringCase(expectedXml, actualXml);
    }

    // Output Format Tests

    @Test
    public void shouldOutputHtmlWithoutNamespaces() {
        // Given: Simple HTML and namespace-unaware converter
        String simpleHtml = "<p>One</p>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(simpleHtml);
        
        W3CDom converter = new W3CDom();
        converter.namespaceAware(false);

        // When: Converting and outputting in different formats
        Document w3cDoc = converter.fromJsoup(jsoupDoc);
        String htmlOutput = W3CDom.asString(w3cDoc, W3CDom.OutputHtml());
        String xmlOutput = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

        // Then: Should produce clean output without namespace declarations
        assertEqualsIgnoringCase(
            "<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"></head><body><p>one</p></body></html>",
            htmlOutput
        );
        assertEqualsIgnoringCase(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head/><body><p>One</p></body></html>",
            xmlOutput
        );
    }

    // CDATA Handling Tests

    @Test
    public void shouldHandleCdataInXmlParsing() throws XPathExpressionException {
        // Given: HTML with script and style content that becomes CDATA in XML
        String htmlWithScriptAndStyle = "<p><script>1 && 2</script><style>3 && 4</style> 5 &amp;&amp; 6</p>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlWithScriptAndStyle);
        jsoupDoc.outputSettings().syntax(xml);
        
        // When: Converting to XML and parsing with W3C
        String xmlOutput = jsoupDoc.body().html();
        assertTrue(xmlOutput.contains("<script>//<![CDATA[\n1 && 2\n//]]></script>"));
        
        Document w3cDoc = parseXmlDocument(xmlOutput, false);
        NodeList scriptElements = executeXPathQuery(w3cDoc, "//script");
        
        // Then: Should properly handle CDATA sections
        assertEquals(2, scriptElements.getLength());
        Node scriptCommentNode = scriptElements.item(0);
        assertEquals("//", scriptCommentNode.getTextContent());
        
        Node scriptContentNode = scriptElements.item(1);
        assertEquals("\n1 && 2\n//", scriptContentNode.getTextContent());
    }

    // Security Tests

    @ParameterizedTest
    @MethodSource("parserProvider")
    public void shouldNotExpandXmlEntities(Parser parser) {
        // Given: XML with entity declarations (potential billion laughs attack)
        String xmlWithEntities = "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE lolz [\n" +
            " <!ENTITY lol \"lol\">\n" +
            " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" +
            "]>\n" +
            "<html><body><p>&lol1;</p></body></html>";

        // When: Parsing and converting to W3C DOM
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(xmlWithEntities, parser);
        W3CDom converter = new W3CDom();
        Document w3cDoc = converter.fromJsoup(jsoupDoc);

        // Then: Should not expand entities (security protection)
        assertNotNull(w3cDoc);
        
        NodeList paragraphs = w3cDoc.getElementsByTagName("p");
        assertEquals(1, paragraphs.getLength());
        assertEquals("&lol1;", paragraphs.item(0).getTextContent());

        // And: String output should not contain expanded entities
        String xmlString = W3CDom.asString(w3cDoc, W3CDom.OutputXml());
        assertFalse(xmlString.contains("lololol"), "Should not contain expanded entities");
        assertTrue(xmlString.contains("&amp;lol1;"), "Should contain escaped entity reference");
    }

    // Namespace Attribute Tests

    @Test
    public void shouldHandleUndeclaredAttributeNamespaces() {
        // Given: HTML with Vue.js-style namespaced attributes (undeclared)
        W3CDom converter = new W3CDom();
        String htmlWithNamespacedAttrs = "<html><body><div v-bind:class='test'></div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlWithNamespacedAttrs);
        
        // When: Converting to W3C DOM
        Document w3cDoc = converter.fromJsoup(jsoupDoc);
        String xmlOutput = converter.asString(w3cDoc);
        
        // Then: Should add namespace declaration for undeclared prefix
        String expectedOutput = 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body><div xmlns:v-bind=\"undefined\" v-bind:class=\"test\"/></body></html>";
        assertEquals(expectedOutput, xmlOutput);
    }

    @Test
    public void shouldUseDeclaredAttributeNamespaces() {
        // Given: HTML with properly declared namespace for attributes
        W3CDom converter = new W3CDom();
        String htmlWithDeclaredNamespace = "<html xmlns:v-bind=\"http://example.com\"><body><div v-bind:class='test'></div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlWithDeclaredNamespace);
        
        // When: Converting to W3C DOM
        Document w3cDoc = converter.fromJsoup(jsoupDoc);
        String xmlOutput = converter.asString(w3cDoc);
        
        // Then: Should use the declared namespace
        String expectedOutput = 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v-bind=\"http://example.com\"><head/><body><div v-bind:class=\"test\"/></body></html>";
        assertEquals(expectedOutput, xmlOutput);
    }

    @Test
    public void shouldHandleNestedElementsWithUndeclaredNamespaces() {
        // Given: Nested elements with undeclared namespace attributes
        W3CDom converter = new W3CDom();
        String htmlWithNestedNamespacedAttrs = "<html><body><div v-bind:class='test'><span v-bind:style='color:red'></span></div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlWithNestedNamespacedAttrs);
        
        // When: Converting to W3C DOM
        Document w3cDoc = converter.fromJsoup(jsoupDoc);
        String xmlOutput = converter.asString(w3cDoc);
        
        // Then: Should declare namespace once on the first element that uses it
        String expectedOutput = 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body><div xmlns:v-bind=\"undefined\" v-bind:class=\"test\"><span v-bind:style=\"color:red\"/></div></body></html>";
        assertEquals(expectedOutput, xmlOutput);
    }

    // Case Sensitivity Tests

    @Test
    public void shouldPreserveCaseInsensitiveHtmlAttributes() throws IOException {
        // Given: HTML with mixed-case attributes (HTML is case-insensitive)
        String htmlWithMixedCaseAttrs = "<html lang=en>\n" +
            "<body>\n" +
            "<img src=\"firstImage.jpg\" alt=\"Alt one\" />\n" +
            "<IMG SRC=\"secondImage.jpg\" AlT=\"Alt two\" />\n" +
            "</body>\n" +
            "</html>";

        // When: Parsing and converting to W3C DOM
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlWithMixedCaseAttrs);
        W3CDom converter = new W3CDom();
        Document w3cDoc = converter.fromJsoup(jsoupDoc);

        // Then: Should normalize to lowercase and preserve attribute values
        org.w3c.dom.Element bodyElement = (org.w3c.dom.Element) w3cDoc.getDocumentElement().getElementsByTagName("body").item(0);
        NodeList imageElements = bodyElement.getElementsByTagName("img");
        assertEquals(2, imageElements.getLength());

        // First image
        org.w3c.dom.Element firstImage = (org.w3c.dom.Element) imageElements.item(0);
        assertEquals(2, firstImage.getAttributes().getLength());
        assertEquals("firstImage.jpg", firstImage.getAttribute("src"));
        assertEquals("Alt one", firstImage.getAttribute("alt"));

        // Second image (originally uppercase)
        org.w3c.dom.Element secondImage = (org.w3c.dom.Element) imageElements.item(1);
        assertEquals(2, secondImage.getAttributes().getLength());
        assertEquals("secondImage.jpg", secondImage.getAttribute("src"));
        assertEquals("Alt two", secondImage.getAttribute("alt"));
    }

    // Test Data Providers

    private static Stream<Arguments> parserProvider() {
        return Stream.of(
            Arguments.of(Parser.htmlParser()),
            Arguments.of(Parser.xmlParser())
        );
    }
}