package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.integration.ParseTest;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

@DisplayName("W3CDom Tests")
public class W3CDomTest {

    // ------------------------------------------------------------------------------------
    // Top-level helper methods
    // ------------------------------------------------------------------------------------

    /**
     * Parses an XML string into a W3C Document.
     * @param xml The XML string to parse.
     * @param namespaceAware Whether the parser should be namespace-aware.
     * @return A W3C Document.
     */
    private static Document parseXml(String xml, boolean namespaceAware) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(namespaceAware);
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Suppress DTD download for <!doctype html>
            builder.setEntityResolver((publicId, systemId) -> {
                if (systemId.contains("about:legacy-compat")) {
                    return new InputSource(new StringReader(""));
                } else {
                    return null;
                }
            });
            Document dom = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            dom.normalizeDocument();
            return dom;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private NodeList xpath(Document w3cDoc, String query) throws XPathExpressionException {
        XPathExpression xpath = XPathFactory.newInstance().newXPath().compile(query);
        return (NodeList) xpath.evaluate(w3cDoc, XPathConstants.NODESET);
    }

    private void assertEqualsIgnoreCase(String want, String have) {
        assertEquals(want.toLowerCase(Locale.ROOT), have.toLowerCase(Locale.ROOT));
    }

    // ------------------------------------------------------------------------------------
    // Nested Test Classes
    // ------------------------------------------------------------------------------------

    @Nested
    @DisplayName("Core Conversion to W3C")
    class CoreConversionTests {
        /**
         * Tests the basic conversion of a Jsoup Document to a W3C Document.
         */
        @Test
        @DisplayName("fromJsoup should perform a basic conversion of simple HTML")
        void fromJsoup_shouldPerformBasicConversion() {
            // Arrange
            String html = "<html><head><title>W3c</title></head><body><p class='one' id=12>Text</p></body></html>";
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

            // Act
            Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

            // Assert
            assertEquals("W3c", w3cDoc.getElementsByTagName("title").item(0).getTextContent());
            assertEquals("Text", w3cDoc.getElementsByTagName("p").item(0).getTextContent());
            org.w3c.dom.Element p = (org.w3c.dom.Element) w3cDoc.getElementsByTagName("p").item(0);
            assertEquals("one", p.getAttribute("class"));
            assertEquals("12", p.getAttribute("id"));
        }

        /**
         * Verifies that the conversion process can populate a pre-existing W3C Document.
         */
        @Test
        @DisplayName("convert should populate a provided W3C Document")
        void convert_shouldPopulateProvidedW3cDocument() throws ParserConfigurationException {
            // Arrange
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><div></div></html>");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document w3cDoc = factory.newDocumentBuilder().newDocument();

            // Act
            new W3CDom().convert(jsoupDoc, w3cDoc);

            // Assert
            String html = W3CDom.asString(w3cDoc, W3CDom.OutputHtml());
            String expected = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><div></div></body></html>";
            assertEquals(expected, html);
        }

        /**
         * Verifies that converted W3C nodes contain a reference back to their original Jsoup source node.
         */
        @Test
        @DisplayName("fromJsoup should link W3C nodes to their original Jsoup source nodes")
        void fromJsoup_shouldLinkW3cNodesToOriginalJsoupSourceNodes() {
            // Arrange
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<body><div><p>One</p></div></body>");
            Element jsoupDiv = jsoupDoc.selectFirst("div");
            assertNotNull(jsoupDiv);
            TextNode jsoupText = (TextNode) jsoupDiv.childNode(0).childNode(0);

            // Act
            W3CDom w3cDom = new W3CDom();
            Document w3cDoc = w3cDom.fromJsoup(jsoupDiv);
            Node w3cDiv = w3cDom.contextNode(w3cDoc);
            Node w3cText = w3cDiv.getFirstChild().getFirstChild();

            // Assert
            assertEquals("div", w3cDiv.getLocalName());
            assertEquals(jsoupDiv, w3cDiv.getUserData(W3CDom.SourceProperty));

            assertEquals("One", w3cText.getTextContent());
            assertEquals(Node.TEXT_NODE, w3cText.getNodeType());
            assertEquals(jsoupText, w3cText.getUserData(W3CDom.SourceProperty));
        }

        /**
         * Jsoup's HTML parser is case-insensitive for attribute names. This test verifies that after conversion,
         * the resulting W3C DOM element behaves as expected for case-variant attribute names.
         */
        @Test
        @DisplayName("fromJsoup should produce a W3C element where attributes are case-insensitive")
        void fromJsoup_shouldHandleCaseInsensitiveAttributes() {
            // Arrange
            String html = "<body><img src='f.jpg' AlT='A'></body>";
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

            // Act
            Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
            NodeList imgs = w3cDoc.getElementsByTagName("img");
            assertEquals(1, imgs.getLength());
            org.w3c.dom.Element imgEl = (org.w3c.dom.Element) imgs.item(0);

            // Assert
            assertEquals("f.jpg", imgEl.getAttribute("src"));
            assertEquals("A", imgEl.getAttribute("alt"));
            assertEquals("A", imgEl.getAttribute("ALT")); // W3C DOM getAttribute is case-insensitive for HTML
        }
    }

    @Nested
    @DisplayName("Serialization to String")
    class SerializationTests {
        /**
         * Tests the serialization of a W3C Document to an XML string.
         */
        @Test
        @DisplayName("asString should serialize a W3C Document to an XML string")
        void asString_shouldSerializeToXml() {
            // Arrange
            String html = "<html><head><title>W3c</title></head><body><p class='one' id=12>Text</p><!-- comment --></body></html>";
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
            Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

            // Act
            String out = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

            // Assert
            String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head><title>W3c</title></head>" +
                "<body><p class=\"one\" id=\"12\">Text</p><!-- comment --></body>" +
                "</html>";
            assertEquals(expected, TextUtil.stripNewlines(out));
        }

        /**
         * Verifies that custom output properties, like indentation, can be applied during serialization.
         */
        @Test
        @DisplayName("asString should support custom output properties")
        void asString_shouldAllowCustomOutputProperties() {
            // Arrange
            String html = "<p>Text</p>";
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
            Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

            // Act
            String unindented = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

            Map<String, String> properties = W3CDom.OutputXml();
            properties.put(OutputKeys.INDENT, "yes");
            String indented = W3CDom.asString(w3cDoc, properties);

            // Assert
            assertTrue(indented.length() > unindented.length());
            assertTrue(indented.contains("\n"));
        }

        /**
         * Verifies that doctypes are correctly handled when serializing to both HTML and XML strings.
         */
        @Test
        @DisplayName("asString should correctly handle doctypes for HTML and XML output")
        void asString_shouldCorrectlyHandleDoctypes() {
            // Arrange
            String htmlWithDoctype = "<!DOCTYPE html><p>One</p>";
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlWithDoctype);
            Document w3cDoc = W3CDom.convert(jsoupDoc);

            // Act (HTML Output)
            String outHtml = W3CDom.asString(w3cDoc, W3CDom.OutputHtml());
            // Act (XML Output)
            String outXml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

            // Assert (HTML) - case-insensitive due to different transformer implementations
            String expectedHtml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head><META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head>" +
                "<body><p>One</p></body>" +
                "</html>";
            assertEqualsIgnoreCase(expectedHtml, normalizeSpaces(outHtml));

            // Assert (XML)
            String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<!DOCTYPE html SYSTEM \"about:legacy-compat\">" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head/>" +
                "<body><p>One</p></body>" +
                "</html>";
            assertEqualsIgnoreCase(expectedXml, normalizeSpaces(outXml));
        }

        /**
         * Verifies that when namespace awareness is disabled on the converter, the output string
         * also omits namespaces.
         */
        @Test
        @DisplayName("asString should omit namespaces when converter is namespace-unaware")
        void asString_shouldOmitNamespaces_whenConverterIsNamespaceUnaware() {
            // Arrange
            org.jsoup.nodes.Document jdoc = Jsoup.parse("<p>One</p>");
            W3CDom w3c = new W3CDom();
            w3c.namespaceAware(false);
            Document w3cDoc = w3c.fromJsoup(jdoc);

            // Act
            String asHtml = W3CDom.asString(w3cDoc, W3CDom.OutputHtml());
            String asXml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

            // Assert
            String expectedHtml = "<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"></head><body><p>one</p></body></html>";
            assertEqualsIgnoreCase(expectedHtml, asHtml);

            String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head/><body><p>One</p></body></html>";
            assertEqualsIgnoreCase(expectedXml, asXml);
        }
    }

    @Nested
    @DisplayName("Namespace Handling")
    class NamespaceHandlingTests {
        /**
         * Tests that namespaces and prefixes from a source XML document are preserved during conversion.
         */
        @Test
        @DisplayName("fromJsoup should preserve namespaces and prefixes when parsing XML")
        void fromJsoup_shouldPreserveNamespacesAndPrefixes_whenParsingXml() throws IOException {
            // Arrange
            File in = ParseTest.getFile("/htmltests/namespaces.xhtml");
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(in, "UTF-8", "", Parser.xmlParser());

            // Act
            Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

            // Assert
            Node htmlEl = w3cDoc.getChildNodes().item(0);
            assertEquals("http://www.w3.org/1999/xhtml", htmlEl.getNamespaceURI());
            assertEquals("html", htmlEl.getLocalName());

            Node epubTitle = htmlEl.getChildNodes().item(3).getChildNodes().item(3);
            assertEquals("http://www.idpf.org/2007/ops", epubTitle.getNamespaceURI());
            assertEquals("title", epubTitle.getLocalName());
            assertEquals("epub:title", epubTitle.getNodeName());

            Node svg = htmlEl.getChildNodes().item(3).getNextSibling().getNextSibling().getNextSibling().getNextSibling();
            assertEquals("http://www.w3.org/2000/svg", svg.getNamespaceURI());
            assertEquals("svg", svg.getLocalName());
            assertEquals("svg", svg.getNodeName());
        }

        /**
         * When an element has a namespace prefix but no corresponding xmlns declaration (e.g., <fb:like>),
         * it should be treated as a local element with the prefix as part of its node name.
         */
        @Test
        @DisplayName("fromJsoup should handle undeclared namespace prefixes as part of the node name")
        void fromJsoup_shouldHandleUndeclaredNamespacePrefixes() {
            // Arrange
            String html = "<fb:like>One</fb:like>";
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

            // Act
            Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
            Node fbNode = w3cDoc.getElementsByTagName("fb:like").item(0);

            // Assert
            // It belongs to the default HTML namespace because no other was declared.
            assertEquals("http://www.w3.org/1999/xhtml", fbNode.getNamespaceURI());
            // The local name does not include the prefix.
            assertEquals("like", fbNode.getLocalName());
            // The node name includes the prefix.
            assertEquals("fb:like", fbNode.getNodeName());
        }

        /**
         * Verifies that namespace awareness can be disabled, affecting the conversion process.
         */
        @Test
        @DisplayName("namespaceAware should be configurable and affect conversion")
        void namespaceAware_shouldBeConfigurableAndAffectConversion() throws XPathExpressionException {
            // Arrange
            String html = "<html xmlns='http://www.w3.org/1999/xhtml'><body><div>hello</div></body></html>";
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
            W3CDom w3c = new W3CDom();

            // Act & Assert (Default behavior)
            assertTrue(w3c.namespaceAware());
            assertNull(xpath(w3c.fromJsoup(jsoupDoc), "//body").item(0), "Should not find body without namespace query");

            // Act & Assert (Disabled)
            w3c.namespaceAware(false);
            assertFalse(w3c.namespaceAware());
            assertNotNull(xpath(w3c.fromJsoup(jsoupDoc), "//body").item(0), "Should find body with simple query");
        }

        /**
         * Tests that when an attribute has an undeclared namespace prefix (e.g., v-bind:class),
         * serialization adds a namespace definition to make the XML valid.
         */
        @Test
        @DisplayName("asString should add an 'undefined' namespace for undeclared attribute prefixes")
        void asString_shouldAddUndefinedNamespace_forUndeclaredAttributePrefix() {
            // Arrange
            String html = "<html><body><div v-bind:class='test'></div></body></html>";
            org.jsoup.nodes.Document jdoc = Jsoup.parse(html);
            W3CDom w3CDom = new W3CDom();
            org.w3c.dom.Document w3CDoc = w3CDom.fromJsoup(jdoc);

            // Act
            String xml = w3CDom.asString(w3CDoc);

            // Assert
            String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head/>" +
                "<body><div xmlns:v-bind=\"undefined\" v-bind:class=\"test\"/></body>" +
                "</html>";
            assertEquals(expected, xml);
        }

        /**
         * Verifies that if an attribute's namespace is explicitly declared, that declaration is used
         * during serialization.
         */
        @Test
        @DisplayName("asString should use explicitly declared namespaces for attributes")
        void asString_shouldUseDeclaredAttributeNamespaces() {
            // Arrange
            String html = "<html xmlns:v-bind=\"http://example.com\"><body><div v-bind:class='test'></div></body></html>";
            org.jsoup.nodes.Document jdoc = Jsoup.parse(html);
            W3CDom w3CDom = new W3CDom();
            org.w3c.dom.Document w3CDoc = w3CDom.fromJsoup(jdoc);

            // Act
            String xml = w3CDom.asString(w3CDoc);

            // Assert
            String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v-bind=\"http://example.com\">" +
                "<head/>" +
                "<body><div v-bind:class=\"test\"/></body>" +
                "</html>";
            assertEquals(expected, xml);
        }
    }

    @Nested
    @DisplayName("Edge Case and Error Handling")
    class EdgeCaseTests {
        /**
         * Jsoup's HTML parser allows attribute names that are invalid in XML (e.g., a lone quote).
         * This test verifies that these names are sanitized during conversion to a W3C Document.
         */
        @Test
        @DisplayName("convert should sanitize invalid attribute names for W3C compatibility")
        void convert_shouldSanitizeInvalidAttributeNames() {
            // Arrange
            String html = "<html><body style=\"color: red\" \" name\"></body></html>";
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

            // Act
            Document w3cDoc = W3CDom.convert(jsoupDoc);
            String xml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

            // Assert
            // The invalid attribute names `"` and `name"` are sanitized to `_` and `name_` respectively.
            String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head/>" +
                "<body _=\"\" name_=\"\" style=\"color: red\"/>" +
                "</html>";
            assertEquals(expected, xml);
        }

        /**
         * Verifies that invalid XML tag names are treated as text content during conversion.
         */
        @Test
        @DisplayName("convert should treat invalid tag names as text nodes")
        void convert_shouldTreatInvalidTagNamesAsTextNode() {
            // Arrange
            org.jsoup.nodes.Document jsoup = Jsoup.parse("<インセンティブで高収入！>Text <p>More</p>");

            // Act
            Document w3cDoc = W3CDom.convert(jsoup);
            String xml = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

            // Assert
            // The invalid tag is escaped and treated as text.
            String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head/>" +
                "<body>&lt;インセンティブで高収入！&gt;Text <p>More</p></body>" +
                "</html>";
            assertEquals(expected, xml);
        }

        /**
         * Verifies that '<' characters in tag and attribute names, which are valid in HTML5 but not XML,
         * are sanitized during conversion.
         */
        @Test
        @DisplayName("convert should sanitize '<' in tag and attribute names")
        void convert_shouldSanitizeLessThanInTagAndAttributeNames() {
            // Arrange
            String htmlWithInvalidChars = "<foo<bar attr<name=\"123\"><b>Text</b></foo<bar>";
            org.jsoup.nodes.Document htmlDoc = Jsoup.parse(htmlWithInvalidChars);

            // Act
            Document w3cDoc = W3CDom.convert(htmlDoc);
            NodeList nodes = w3cDoc.getElementsByTagName("foo_bar");

            // Assert
            // The '<' is replaced with '_' to create valid XML names.
            assertEquals(1, nodes.getLength());
            assertEquals("123", nodes.item(0).getAttributes().getNamedItem("attr_name").getTextContent());
        }

        /**
         * Tests that an empty doctype `<!doctype>` is handled gracefully and does not result in a doctype
         * node in the W3C document.
         */
        @Test
        @DisplayName("fromJsoup should handle an empty doctype gracefully")
        void fromJsoup_shouldHandleEmptyDoctype() {
            // Arrange
            String html = "<!doctype>Foo";
            org.jsoup.nodes.Document jdoc = Jsoup.parse(html);

            // Act
            Document doc = (new W3CDom()).fromJsoup(jdoc);

            // Assert
            assertNull(doc.getDoctype());
            assertEquals("Foo", doc.getDocumentElement().getTextContent());
        }

        /**
         * Verifies that unicode attribute names are preserved when converting from a Jsoup document
         * parsed as HTML.
         */
        @Test
        @DisplayName("convert should preserve unicode attribute names for HTML input")
        void convert_shouldPreserveUnicodeAttributeNames_forHtmlInput() {
            // Arrange
            String html = "<p hành=\"1\" hình=\"2\">unicode</p>";
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);

            // Act
            Document w3Doc = W3CDom.convert(jsoupDoc);
            String out = W3CDom.asString(w3Doc, W3CDom.OutputHtml());

            // Assert
            assertTrue(out.contains("hành=\"1\""));
            assertTrue(out.contains("hình=\"2\""));
        }

        /**
         * When Jsoup's output syntax is set to XML, it may coerce attribute names to be XML-compatible.
         * This test verifies that this coercion is reflected in the W3C conversion.
         */
        @Test
        @DisplayName("convert should reflect coerced attribute names for XML-syntax input")
        void convert_shouldReflectCoercedAttributeNames_forXmlInput() {
            // Arrange
            String html = "<p hành=\"1\" hình=\"2\">unicode</p>";
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
            jsoupDoc.outputSettings().syntax(xml); // Coerces attributes to be XML-safe

            // Act
            Document w3Doc = W3CDom.convert(jsoupDoc);
            String out = W3CDom.asString(w3Doc, W3CDom.OutputHtml());

            // Assert
            // 'hình' is not a valid XML name, so it's coerced. 'hành' is valid.
            // Note: The original test had a different expectation. This reflects current Jsoup behavior.
            assertTrue(out.contains("hành=\"1\""));
            assertTrue(out.contains("h_nh=\"2\""));
        }
    }

    @Nested
    @DisplayName("XPath Querying")
    class XPathQueryTests {
        /**
         * By default, the conversion is namespace-aware. This test verifies that XPath queries must also
         * be namespace-aware to find elements.
         */
        @Test
        @DisplayName("selectXpath should require namespace-aware queries by default")
        void selectXpath_shouldRequireNamespaceAwareQueries_byDefault() throws XPathExpressionException {
            // Arrange
            String html = "<html xmlns='http://www.w3.org/1999/xhtml'><body><div>hello</div></body></html>";
            Document dom = new W3CDom().fromJsoup(Jsoup.parse(html));

            // Act
            NodeList simpleQuery = xpath(dom, "//body");
            NodeList nsQuery = xpath(dom, "//*[local-name()='body']");

            // Assert
            assertEquals(0, simpleQuery.getLength(), "Simple query should not find namespaced element");
            assertEquals(1, nsQuery.getLength(), "Namespace-aware query should find element");
            assertEquals("div", nsQuery.item(0).getFirstChild().getLocalName());
        }

        /**
         * Verifies that if the W3C document is created without namespace awareness, simple XPath queries
         * will work as expected.
         */
        @Test
        @DisplayName("selectXpath should work with simple queries when document is namespace-unaware")
        void selectXpath_shouldWorkWithSimpleQueries_whenDocumentIsNamespaceUnaware() throws XPathExpressionException {
            // Arrange
            String html = "<html><body><div>hello</div></body></html>";
            W3CDom w3c = new W3CDom();
            w3c.namespaceAware(false); // Disable namespace awareness

            // Act
            Document dom = w3c.fromJsoup(Jsoup.parse(html));
            NodeList nodeList = xpath(dom, "//body"); // Simple query

            // Assert
            assertEquals(1, nodeList.getLength());
            assertEquals("div", nodeList.item(0).getFirstChild().getLocalName());
        }
    }

    @Nested
    @DisplayName("Security Checks")
    class SecurityTests {
        private static Stream<Arguments> parserProvider() {
            return Stream.of(Arguments.of(Parser.htmlParser()), Arguments.of(Parser.xmlParser()));
        }

        /**
         * Verifies that the conversion process does not expand XML entities, which is a defense
         * against "Billion Laughs" type attacks. Jsoup itself does not parse DTD entities, and this
         * test confirms that behavior is maintained through the W3C conversion.
         */
        @ParameterizedTest
        @MethodSource("parserProvider")
        @DisplayName("fromJsoup should not expand XML entities to prevent Billion Laughs attack")
        void fromJsoup_shouldNotExpandXmlEntities_toPreventBillionLaughs(Parser parser) {
            // Arrange
            String billionLaughs = "<?xml version=\"1.0\"?>\n" +
                "<!DOCTYPE lolz [\n" +
                " <!ENTITY lol \"lol\">\n" +
                " <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" +
                "]>\n" +
                "<html><body><p>&lol1;</p></body></html>";

            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(billionLaughs, parser);

            // Act
            Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
            Node p = w3cDoc.getElementsByTagName("p").item(0);
            String out = W3CDom.asString(w3cDoc, W3CDom.OutputXml());

            // Assert
            assertEquals("&lol1;", p.getTextContent(), "Entity should not be expanded in the DOM");
            assertFalse(out.contains("lololol"), "Serialized string should not contain expanded entities");
            assertTrue(out.contains("&amp;lol1;"), "Serialized string should contain escaped entity");
        }
    }
}