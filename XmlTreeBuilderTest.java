package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
 * Comprehensive tests for the {@link XmlTreeBuilder}.
 * These tests verify its ability to parse various XML constructs, handle errors,
 * manage namespaces, and respect configuration settings.
 */
@DisplayName("XmlTreeBuilder")
public class XmlTreeBuilderTest {
    private static final String BASE_URI = "http://foo.com/";

    /**
     * Asserts that the stripped HTML output of a document matches the expected XML string.
     * @param expectedXml The expected XML string, without newlines.
     * @param actualDoc The parsed document to check.
     */
    private void assertXmlOutputEquals(String expectedXml, Document actualDoc) {
        assertEquals(expectedXml, TextUtil.stripNewlines(actualDoc.html()));
    }

    @Nested
    @DisplayName("Basic Parsing")
    class BasicParsingTests {
        @Test
        void shouldParseSimpleXmlAndResolveAbsoluteUrl() {
            // Arrange
            String xml = "<doc id=2 href='/bar'>Foo <br /><link>One</link><link>Two</link></doc>";
            XmlTreeBuilder tb = new XmlTreeBuilder();

            // Act
            Document doc = tb.parse(xml, BASE_URI);

            // Assert
            assertXmlOutputEquals("<doc id=\"2\" href=\"/bar\">Foo <br /><link>One</link><link>Two</link></doc>", doc);
            assertEquals("http://foo.com/bar", doc.getElementById("2").absUrl("href"));
        }

        @Test
        void shouldParseXmlFragment() {
            // Arrange
            String xml = "<one src='/foo/' />Two<three><four /></three>";

            // Act
            List<Node> nodes = Parser.parseXmlFragment(xml, "http://example.com/");

            // Assert
            assertEquals(3, nodes.size());
            assertEquals("one", nodes.get(0).nodeName());
            assertEquals("http://example.com/foo/", nodes.get(0).absUrl("src"));
            assertEquals("Two", ((TextNode) nodes.get(1)).text());
        }

        @Test
        void shouldDetectCharsetFromXmlDeclaration() throws IOException, URISyntaxException {
            // Arrange
            File xmlFile = new File(XmlTreeBuilder.class.getResource("/htmltests/xml-charset.xml").toURI());
            InputStream inStream = new FileInputStream(xmlFile);

            // Act
            Document doc = Jsoup.parse(inStream, null, "http://example.com/", Parser.xmlParser());

            // Assert
            assertEquals("ISO-8859-1", doc.charset().name());
            assertEquals("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><data>äöåéü</data>",
                TextUtil.stripNewlines(doc.html()));
        }

        @Test
        void shouldNotForceSelfCloseOnKnownHtmlTags() {
            // In HTML, "<br>one</br>" is parsed as "<br />one<br />".
            // In XML, it should remain as parsed: "<br>one</br>".
            // Arrange
            String xml = "<br>one</br>";

            // Act
            Document xmlDoc = Jsoup.parse(xml, "", Parser.xmlParser());

            // Assert
            assertEquals("<br>one</br>", xmlDoc.html());
        }

        @Test
        void shouldPreserveTagAndAttributeCaseByDefault() {
            // Arrange
            String xml = "<CHECK>One</CHECK><TEST ID=1>Check</TEST>";

            // Act
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());

            // Assert
            assertXmlOutputEquals("<CHECK>One</CHECK><TEST ID=\"1\">Check</TEST>", doc);
        }

        @Test
        void shouldPreserveCaseWhenAppendingXmlFragment() {
            // Arrange
            String xml = "<One>One</One>";
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
            Element one = doc.selectFirst("One");

            // Act
            one.append("<Two ID=2>Two</Two>");

            // Assert
            assertXmlOutputEquals("<One>One<Two ID=\"2\">Two</Two></One>", doc);
        }

        @Test
        void shouldAllowAnyTagToBeSelfClosing() {
            // In XML, any tag can be self-closing.
            // Arrange
            String xml = "<div id='1'/><p/><div>Foo</div><div></div><foo></foo>";
            Parser parser = Parser.xmlParser().setTrackErrors(10);

            // Act
            Document doc = Jsoup.parse(xml, "", parser);

            // Assert
            assertEquals(0, parser.getErrors().size());
            // Jsoup infers that empty elements can be represented with a self-closing tag.
            assertEquals("<div id=\"1\" /><p /><div>Foo</div><div /><foo></foo>", TextUtil.stripNewlines(doc.outerHtml()));
        }

        @Test
        void shouldConsiderXmlHeaderWithOrWithoutSpaceAsValid() {
            // Arrange
            String xmlWithNoSpace = "<?xml version=\"1.0\"?>\n<root></root>";
            String xmlWithSpace = "<?xml version=\"1.0\" ?>\n<root></root>";
            String expected = "<?xml version=\"1.0\"?>\n<root></root>";

            // Act
            Document doc1 = Jsoup.parse(xmlWithNoSpace, Parser.xmlParser().setTrackErrors(10));
            Document doc2 = Jsoup.parse(xmlWithSpace, Parser.xmlParser().setTrackErrors(10));

            // Assert
            assertEquals(0, doc1.parser().getErrors().size());
            assertEquals(expected, doc1.html());

            assertEquals(0, doc2.parser().getErrors().size());
            assertEquals(expected, doc2.html());
        }

        @Test
        void shouldUseFlyweightForCustomTags() {
            // Arrange
            String xml = "<foo>Foo</foo><foo>Foo</foo><FOO>FOO</FOO><FOO>FOO</FOO>";

            // Act
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            Elements els = doc.children();

            // Assert
            Tag t1 = els.get(0).tag();
            Tag t2 = els.get(1).tag();
            Tag t3 = els.get(2).tag();
            Tag t4 = els.get(3).tag();

            assertEquals("foo", t1.getName());
            assertEquals("FOO", t3.getName());
            assertSame(t1, t2, "Tags with the same name and case should be the same instance");
            assertSame(t3, t4, "Tags with the same name and case should be the same instance");
        }

        @Test
        void shouldCloseReaderAfterParsing() {
            // Arrange
            Document doc = Jsoup.parse("Hello", "", Parser.xmlParser());

            // Act
            TreeBuilder treeBuilder = doc.parser().getTreeBuilder();

            // Assert
            assertNull(treeBuilder.reader, "Reader should be null after parse");
            assertNull(treeBuilder.tokeniser, "Tokeniser should be null after parse");
        }
    }

    @Nested
    @DisplayName("Parser Configuration and Settings")
    class ConfigurationAndSettingsTests {
        @Test
        void shouldParseCorrectlyWhenSuppliedToJsoupParse() {
            // Arrange
            String xml = "<doc><val>One<val>Two</val></bar>Three</doc>";

            // Act
            Document doc = Jsoup.parse(xml, BASE_URI, Parser.xmlParser());

            // Assert
            assertXmlOutputEquals("<doc><val>One<val>Two</val>Three</val></doc>", doc);
        }

        @Test
        void shouldParseCorrectlyFromInputStream() throws IOException, URISyntaxException {
            // Arrange
            File xmlFile = new File(XmlTreeBuilder.class.getResource("/htmltests/xml-test.xml").toURI());
            InputStream inStream = new FileInputStream(xmlFile);

            // Act
            Document doc = Jsoup.parse(inStream, null, BASE_URI, Parser.xmlParser());

            // Assert
            assertXmlOutputEquals("<doc><val>One<val>Two</val>Three</val></doc>", doc);
        }

        @Test
        void shouldNormalizeCaseWhenUsingHtmlSettings() {
            // Arrange
            String xml = "<TEST ID=1>Check</TEST>";
            Parser parser = Parser.xmlParser().settings(ParseSettings.htmlDefault);

            // Act
            Document doc = Jsoup.parse(xml, "", parser);

            // Assert
            assertXmlOutputEquals("<test id=\"1\">Check</test>", doc);
        }

        @Test
        void shouldNormalizeMismatchedEndTagsWhenUsingHtmlSettings() {
            // Arrange
            String xml = "<div>test</DIV><p></p>";
            Parser parser = Parser.xmlParser().settings(ParseSettings.htmlDefault);

            // Act
            Document document = Jsoup.parse(xml, "", parser);

            // Assert
            assertEquals("<div>test</div><p></p>", document.html());
        }

        @Test
        void shouldApplyXmlSpecificSettingsToParser() {
            // Arrange
            Document doc = Jsoup.parse("<foo>", Parser.xmlParser());

            // Act
            Parser parser = doc.parser();
            ParseSettings settings = parser.settings();

            // Assert
            assertTrue(settings.preserveTagCase());
            assertTrue(settings.preserveAttributeCase());
            assertEquals(NamespaceXml, parser.defaultNamespace());
        }
    }

    @Nested
    @DisplayName("Node-Specific Parsing")
    class NodeSpecificParsingTests {
        @Test
        void shouldParseCommentsAndDocTypes() {
            // Arrange
            String xml = "<!DOCTYPE HTML><!-- a comment -->One <qux />Two";

            // Act
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());

            // Assert
            assertEquals("<!DOCTYPE HTML><!-- a comment -->One <qux />Two", TextUtil.stripNewlines(doc.html()));
        }

        @Test
        void shouldParseXmlDeclarationAsDeclarationNode() {
            // Arrange
            String xml = "<?xml encoding='UTF-8' ?><body>One</body><!-- comment -->";

            // Act
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());

            // Assert
            assertEquals("<?xml encoding=\"UTF-8\"?><body>One</body><!-- comment -->", doc.outerHtml());
            assertEquals("#declaration", doc.childNode(0).nodeName());
            assertEquals("xml", ((XmlDeclaration) doc.childNode(0)).name());
            assertEquals("#comment", doc.childNode(2).nodeName());
        }

        @Test
        void shouldParseAttributesInXmlDeclaration() {
            // Arrange
            String xml = "<?xml version='1' encoding='UTF-8' something='else'?><val>One</val>";

            // Act
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
            XmlDeclaration decl = (XmlDeclaration) doc.childNode(0);

            // Assert
            assertEquals("1", decl.attr("version"));
            assertEquals("UTF-8", decl.attr("encoding"));
            assertEquals("else", decl.attr("something"));
            assertEquals("version=\"1\" encoding=\"UTF-8\" something=\"else\"", decl.getWholeDeclaration());
            assertEquals("<?xml version=\"1\" encoding=\"UTF-8\" something=\"else\"?>", decl.outerHtml());
        }

        @Test
        void shouldParseProcessingInstruction() {
            // A processing instruction like <?name value?> is parsed as an XmlDeclaration node.
            // Arrange
            String xml = "<?myProcessingInstruction My Processing instruction.?>";

            // Act
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
            XmlDeclaration decl = (XmlDeclaration) doc.childNode(0);

            // Assert
            assertEquals("myProcessingInstruction", decl.name());
            assertTrue(decl.hasAttr("My"));
            assertEquals("My Processing instruction.", decl.getWholeDeclaration());
            assertEquals("<?myProcessingInstruction My Processing instruction.?>", decl.outerHtml());
        }

        @Test
        void shouldPreserveCaseInDeclarationName() {
            // Arrange
            String xml = "<?XML version='1' encoding='UTF-8' something='else'?>";

            // Act
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());

            // Assert
            assertEquals("<?XML version=\"1\" encoding=\"UTF-8\" something=\"else\"?>", doc.outerHtml());
        }

        @Test
        void shouldPreserveCdataContent() {
            // Arrange
            String xml = "<div id=1><
![CDATA[\n<html>\n <foo><&amp;]]></div>";

            // Act
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser()
);
            Element div = doc.getElementById("1");
            CDataNode cdata = (CDataNode) div.textNodes().get(0);

            // Assert
            assertEquals("<html>\n <foo><&amp;", div.text());
            assertEquals(0, div.children().size());
            assertEquals(1, div.childNodeSize()); // one CData node
            assertEquals("<div id=\"1\"><
![CDATA[\n<html>\n <foo><&amp;]]></div>", div.outerHtml()
);
            assertEquals("\n<html>\n <foo><&amp;", cdata.text());
        }

        @Test
        void shouldPreserveWhitespaceInCdata() {
            // Arrange
            String xml = "<script type=\"text/javascript\">//<
![CDATA[\n\n  foo()
;\n//]]></script>";

            // Act
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());

            // Assert
            assertEquals(xml, doc.outerHtml());
            assertEquals("//\n\n  foo();\n//", doc.selectFirst("script").text());
        }

        @Test
        void shouldParseDeclarationContainingGtCharacter() {
            // See: https://github.com/jhy/jsoup/issues/1947
            // Arrange
            String xml = "<x><?xmlDeclaration att1=\"value1\" att2=\"&lt;val2>\"?></x>";

            // Act
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            XmlDeclaration decl = (XmlDeclaration) doc.expectFirst("x").childNode(0);

            // Assert
            assertEquals("<val2>", decl.attr("att2"));
        }

        @Test
        void shouldParseXmlDeclaration() {
            // Arrange
            String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

            // Act
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            XmlDeclaration decl = (XmlDeclaration) doc.childNode(0);

            // Assert
            assertEquals("xml", decl.name());
            assertEquals("1.0", decl.attr("version"));
            assertEquals("utf-8", decl.attr("encoding"));
            assertEquals("version=\"1.0\" encoding=\"utf-8\"", decl.getWholeDeclaration());
            assertEquals("<?xml version=\"1.0\" encoding=\"utf-8\"?>", decl.outerHtml());
        }

        @Test
        void shouldParseDocumentType() {
            // Arrange
            String xml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";

            // Act
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            DocumentType doctype = (DocumentType) doc.childNode(0);

            // Assert
            assertEquals("html", doctype.name());
            assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.attr("publicId"));
            assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.attr("systemId"));
            assertEquals(xml, doctype.outerHtml());
        }

        @Test
        void shouldParseElementDeclarationAsProcessingInstruction() {
            // Jsoup's XML parser treats <!ELEMENT ...> as an XmlDeclaration node (a processing instruction).
            // Arrange
            String xml = "<!ELEMENT footnote (#PCDATA|a)*>";

            // Act
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            XmlDeclaration decl = (XmlDeclaration) doc.childNode(0);

            // Assert
            assertEquals("ELEMENT", decl.name());
            assertEquals("footnote (#PCDATA|a)*", decl.getWholeDeclaration());
            assertEquals(xml, decl.outerHtml());
        }
    }

    @Nested
    @DisplayName("Error Handling and Quirks")
    class ErrorHandlingAndQuirksTests {
        @Test
        void shouldHandleMismatchedEndTagsGracefully() {
            // The </bar> tag is ignored, and the document is closed implicitly.
            // Arrange
            String xml = "<doc><val>One<val>Two</val></bar>Three</doc>";

            // Act
            Document doc = Jsoup.parse(xml, BASE_URI, Parser.xmlParser());

            // Assert
            assertXmlOutputEquals("<doc><val>One<val>Two</val>Three</val></doc>", doc);
        }

        @Test
        void shouldHandleUnexpectedEofInTag() {
            // Arrange
            String xml = "<img src=asdf onerror=\"alert(1)\" x=";

            // Act
            Document xmlDoc = Jsoup.parse(xml, "", Parser.xmlParser());

            // Assert
            assertEquals("<img src=\"asdf\" onerror=\"alert(1)\" x=\"\"></img>", xmlDoc.html());
        }

        @Test
        void shouldHandleXmlDeclarationWithoutClosingQuestionMark() {
            // Arrange
            String xml = "<?xml version='1.0'><val>One</val>";

            // Act
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());

            // Assert
            assertEquals("One", doc.select("val").text());
        }

        @Test
        void shouldHandleXmlDeclarationLikeTokensInScript() {
            // See: https://github.com/jhy/jsoup/issues/1139
            // The parser should not treat '<?' and '?>' inside a script tag as a declaration.
            // Arrange
            String xml = "<script> var a=\"<?\"; var b=\"?>\"; </script>";

            // Act
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());

            // Assert
            // It gets converted to a comment.
            assertEquals("<script> var a=\"<!--?\"; var b=\"?-->\"; </script>", doc.html());
        }

        @Test
        void shouldDropDuplicateAttributes() {
            // Attributes are case-sensitive in XML. 'One' and 'ONE' are different.
            // The second 'One' and 'ONE' should be dropped.
            // Arrange
            String xml = "<p One=One ONE=Two one=Three One=Four ONE=Five two=Six two=Seven Two=Eight>Text</p>";
            Parser parser = Parser.xmlParser().setTrackErrors(10);

            // Act
            Document doc = parser.parseInput(xml, "");

            // Assert
            assertEquals("<p One=\"One\" ONE=\"Two\" one=\"Three\" two=\"Six\" Two=\"Eight\">Text</p>", doc.selectFirst("p").outerHtml());
        }
    }

    @Nested
    @DisplayName("Output and Formatting")
    class OutputAndFormattingTests {
        @Test
        void shouldDefaultToXmlSyntaxOutputSettings() {
            // Arrange
            Document doc = Jsoup.parse("x", "", Parser.xmlParser());

            // Act & Assert
            assertEquals(Syntax.xml, doc.outputSettings().syntax());
        }

        @Test
        void shouldDefaultToXmlSyntaxAndXhtmlEscapeMode() {
            // Arrange
            Document doc = Jsoup.parse("<root/>", "", Parser.xmlParser());

            // Act & Assert
            assertEquals(Syntax.xml, doc.outputSettings().syntax());
            assertEquals(Entities.EscapeMode.xhtml, doc.outputSettings().escapeMode());
        }

        @Test
        void shouldCreateValidXmlPrologForNewDocument() {
            // Arrange
            Document document = Document.createShell("");
            document.outputSettings().syntax(Syntax.xml);
            document.charset(StandardCharsets.UTF_8);

            // Act
            String out = document.outerHtml();

            // Assert
            String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                " <head></head>\n" +
                " <body></body>\n" +
                "</html>";
            assertEquals(expected, out);
        }

        @Test
        void shouldNotPrettyPrintByDefault() {
            // Arrange
            String xml = "\n\n<div><one>One</one><one>\n Two</one>\n</div>\n ";

            // Act
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());

            // Assert
            assertEquals(xml, doc.html());
        }

        @Test
        void shouldAlwaysEscapeLtAndGtInAttributeValuesForXmlSyntax() {
            // See: https://github.com/jhy/jsoup/issues/2337
            // Arrange
            Document doc = Jsoup.parse("<p one='&lt;two&gt;'>Three</p>", "", Parser.xmlParser());
            doc.outputSettings().escapeMode(Entities.EscapeMode.extended); // Set a different escape mode

            // Act
            String html = doc.html();

            // Assert
            assertEquals(Syntax.xml, doc.outputSettings().syntax());
            assertEquals("<p one=\"&lt;two&gt;\">Three</p>", html, "Even with extended escapes, < and > must be escaped in XML attributes.");
        }

        @Test
        void shouldCorrectInvalidAttributeNamesOnOutput() {
            // Arrange
            String xml = "<body style=\"color: red\" \" name\"><div =\"\"></div></body>";
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            doc.outputSettings().syntax(Syntax.xml);

            // Act
            String out = doc.html();

            // Assert
            assertEquals("<body style=\"color: red\" _=\"\" name_=\"\"><div _=\"\"></div></body>", out);
        }

        @Test
        void shouldHandleVariousValidAttributeNameCharacters() {
            // Arrange
            String xml = "<a bB1-_:.=foo _9!=bar xmlns:p1=qux>One</a>";
            Document doc = Jsoup.parse(xml, Parser.xmlParser());

            // Act
            String out = doc.html();

            // Assert
            // '!' is invalid in an attribute name and is coerced to '_'.
            assertEquals("<a bB1-_:.=\"foo\" _9_=\"bar\" xmlns:p1=\"qux\">One</a>", out);
        }

        @Test
        void shouldFormatTextNodesInlineByDefaultWhenPrettyPrinting() {
            // See: https://github.com/jhy/jsoup/issues/2141
            // Arrange
            String xml = "<package><metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
                "<dc:identifier id=\"pub-id\">id</dc:identifier>\n" +
                "<dc:title>title</dc:title>\n" +
                "<dc:language>ja</dc:language>\n" +
                "<dc:description>desc</dc:description>\n" +
                "</metadata></package>";
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            doc.outputSettings().prettyPrint(true);

            // Act
            String out = doc.html();

            // Assert
            String expected = "<package>\n" +
                " <metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
                "  <dc:identifier id=\"pub-id\">id</dc:identifier> <dc:title>title</dc:title> <dc:language>ja</dc:language> <dc:description>desc</dc:description>\n" +
                " </metadata>\n" +
                "</package>";
            assertEquals(expected, out);
        }

        @Test
        void shouldAllowBlockFormattingByModifyingTagProperties() {
            // See: https://github.com/jhy/jsoup/issues/2141
            // Arrange
            String xml = "<package><metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
                "<dc:identifier id=\"pub-id\">id</dc:identifier>\n" +
                "<dc:title>title</dc:title>\n" +
                "<dc:language>ja</dc:language>\n" +
                "<dc:description>desc</dc:description>\n" +
                "</metadata></package>";
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            doc.outputSettings().prettyPrint(true);

            // Customize tags to be block-level for different formatting
            Element meta = doc.expectFirst("metadata");
            meta.tag().set(Tag.Block);
            for (Element inner : meta.children()) {
                inner.tag().set(Tag.Block);
            }

            // Act
            String out = doc.html();

            // Assert
            String expected = "<package>\n" +
                " <metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
                "  <dc:identifier id=\"pub-id\">id</dc:identifier>\n" +
                "  <dc:title>title</dc:title>\n" +
                "  <dc:language>ja</dc:language>\n" +
                "  <dc:description>desc</dc:description>\n" +
                " </metadata>\n" +
                "</package>";
            assertEquals(expected, out);
        }
    }

    @Nested
    @DisplayName("Namespace Handling")
    class NamespaceTests {
        private void assertIsInXmlNamespace(Element el) {
            assertEquals(NamespaceXml, el.tag().namespace(), String.format("Element <%s> not in XML namespace", el.tagName()));
        }

        @Test
        void shouldAssignXmlNamespaceToAllElementsByDefault() {
            // Arrange
            String xml = "<foo><bar><div><svg><math>Qux</bar></foo>";

            // Act
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            Document clonedDoc = doc.clone();
            Document shallowClonedDoc = doc.shallowClone();

            // Assert
            assertIsInXmlNamespace(doc);
            for (Element el : doc.select("*")) {
                assertIsInXmlNamespace(el);
            }

            assertIsInXmlNamespace(clonedDoc);
            assertIsInXmlNamespace(clonedDoc.expectFirst("bar"));
            assertIsInXmlNamespace(shallowClonedDoc);
        }

        @Test
        void shouldParseNamespacePrefixedElements() {
            // Example from the XML namespace spec: https://www.w3.org/TR/xml-names/
            // Arrange
            String xml = "<?xml version=\"1.0\"?>\n" +
                "<!-- both namespace prefixes are available throughout -->\n" +
                "<bk:book xmlns:bk=\"urn:loc.gov:books\" xmlns:isbn=\"urn:ISBN:0-395-36341-6\">\n" +
                "    <bk:title>Cheaper by the Dozen</bk:title>\n" +
                "    <isbn:number>1568491379</isbn:number>\n" +
                "</bk:book>";

            // Act
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            Element book = doc.expectFirst("bk|book");
            Element title = doc.expectFirst("bk|title");
            Element number = doc.expectFirst("isbn|number");

            // Assert
            assertEquals("bk:book", book.tagName());
            assertEquals("urn:loc.gov:books", book.tag().namespace());
            assertEquals("bk:title", title.tagName());
            assertEquals("urn:loc.gov:books", title.tag().namespace());
            assertEquals("isbn:number", number.tagName());
            assertEquals("urn:ISBN:0-395-36341-6", number.tag().namespace());
            assertEquals(xml, doc.html());
        }

        @Test
        void shouldHandleDefaultNamespaces() {
            // Arrange
            String xml = "<?xml version=\"1.0\"?>\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "  <head><title>Frobnostication</title></head>\n" +
                "  <body><p>Moved to <a href=\"http://frob.example.com\">here</a>.</p></body>\n" +
                "</html>";

            // Act
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            Element html = doc.expectFirst("html");
            Element a = doc.expectFirst("a");

            // Assert
            assertEquals(NamespaceHtml, html.tag().namespace());
            assertEquals(NamespaceHtml, a.tag().namespace());
        }

        @Test
        void shouldHandleEmptyDefaultNamespaceScope() {
            // Arrange
            String xml = "<?xml version='1.0'?>\n" +
                "<Beers>\n" +
                "  <table xmlns='http://www.w3.org/1999/xhtml'>\n" +
                "   <td><brandName xmlns=\"\">Huntsman</brandName></td>\n" +
                "  </table>\n" +
                "</Beers>";

            // Act
            Document doc = Jsoup.parse(xml, Parser.xmlParser());

            // Assert
            assertEquals(NamespaceXml, doc.expectFirst("Beers").tag().namespace());
            assertEquals(NamespaceHtml, doc.expectFirst("td").tag().namespace());
            assertEquals("", doc.expectFirst("brandName").tag().namespace());
        }

        @Test
        void shouldParseNamespacedAttributes() {
            // Arrange
            String xml = "<x xmlns:edi='http://ecommerce.example.org/schema'>\n" +
                "  <lineItem edi:taxClass=\"exempt\" other=foo>Baby food</lineItem>\n" +
                "</x>";

            // Act
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            Element lineItem = doc.expectFirst("lineItem");
            Attribute taxClass = lineItem.attribute("edi:taxClass");
            Attribute other = lineItem.attribute("other");

            // Assert
            assertNotNull(taxClass);
            assertEquals("edi", taxClass.prefix());
            assertEquals("taxClass", taxClass.localName());
            assertEquals("http://ecommerce.example.org/schema", taxClass.namespace());

            assertNotNull(other);
            assertEquals("", other.prefix());
            assertEquals("other", other.localName());
            assertEquals("", other.namespace());
        }

        @Test
        void shouldCorrectlyNamespaceElementsAppendedAsHtml() {
            // Appended fragments should inherit the namespace of their parent.
            // Arrange
            String xml = "<out xmlns='/out'><bk:book xmlns:bk='/books' xmlns:edi='/edi'><bk:title>Test</bk:title></bk:book></out>";
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            Element book = doc.expectFirst("bk|book");

            // Act
            book.append("<bk:content edi:foo=qux>Content</bk:content>");
            Element content = book.expectFirst("bk|content");
            content.append("<data>Data</data><html xmlns='/html' xmlns:bk='/update'><p>Foo</p><bk:news>News</bk:news></html>");

            // Assert
            assertEquals("/out", doc.expectFirst("out").tag().namespace());
            assertEquals("/books", content.tag().namespace());
            assertEquals("/edi", content.attribute("edi:foo").namespace());
            assertEquals("/out", content.expectFirst("data").tag().namespace(), "Inherits from grandparent");
            assertEquals("/html", content.expectFirst("p").tag().namespace(), "Uses new default namespace");
            assertEquals("/update", content.expectFirst("bk|news").tag().namespace(), "Uses overridden prefix namespace");
        }
    }

    @Nested
    @DisplayName("Custom Tag Sets")
    class CustomTagSetTests {
        @Test
        void shouldRespectCustomRcdataTagSet() {
            // Arrange
            String inner = "Blah\nblah\n<foo></foo>&quot;";
            String innerText = "Blah\nblah\n<foo></foo>\"";
            String xml = "<x><y><z>" + inner + "</z></y></x>";

            TagSet custom = new TagSet();
            custom.valueOf("z", NamespaceXml, ParseSettings.preserveCase).set(Tag.RcData);

            // Act
            Document doc = Jsoup.parse(xml, Parser.xmlParser().tagSet(custom));
            Element zEl = doc.expectFirst("z");
            Node child = zEl.childNode(0);

            // Assert
            assertTrue(child instanceof TextNode);
            assertEquals(innerText, ((TextNode) child).getWholeText());
        }

        @Test
        void shouldRespectCustomDataTagSet() {
            // Arrange
            String inner = "Blah\nblah\n<foo></foo>&quot;";
            String xml = "<x><y><z>" + inner + "</z></y></x>";

            TagSet custom = new TagSet();
            custom.valueOf("z", NamespaceXml, ParseSettings.preserveCase).set(Tag.Data);

            // Act
            Document doc = Jsoup.parse(xml, Parser.xmlParser().tagSet(custom));
            Element zEl = doc.expectFirst("z");
            Node child = zEl.childNode(0);

            // Assert
            assertTrue(child instanceof DataNode);
            assertEquals(inner, ((DataNode) child).getWholeData());
            assertEquals(inner, zEl.data());
        }

        @Test
        void shouldRespectCustomVoidTagSet() {
            // Arrange
            String ns = "custom";
            String xml = "<x xmlns=custom><foo><link><meta>";
            TagSet custom = new TagSet();
            custom.valueOf("link", ns).set(Tag.Void);
            custom.valueOf("meta", ns).set(Tag.Void);

            // Act
            Document doc = Jsoup.parse(xml, Parser.xmlParser().tagSet(custom));

            // Assert
            String expected = "<x xmlns=\"custom\"><foo><link /><meta /></foo></x>";
            assertEquals(expected, doc.html());
        }

        @Test
        void shouldAllowUsingHtmlTagSetWithXmlParser() {
            // Use HTML tag properties (e.g. script is CDATA, img is void) but with XML parsing rules.
            // Arrange
            String xml = "<html xmlns=" + NamespaceHtml + "><div><script>a<b</script><img><p>";
            Document doc = Jsoup.parse(xml, Parser.xmlParser().tagSet(TagSet.Html()));
            doc.outputSettings().prettyPrint(true);

            // Act & Assert for XML output
            String expectedXml = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                " <div>\n" +
                "  <script>//<
![CDATA[\na<b\n//]]></script>\n" +
                "  <img />\n" +
                "  <p></p>\n" +
                " </div>\n" +
                "</html>";
            assertEquals(expectedXml, doc.html()
);

            // Act & Assert for HTML output
            doc.outputSettings().syntax(Syntax.html);
            String expectedHtml = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                " <div>\n" +
                "  <script>a<b</script>\n" +
                "  <img>\n" +
                "  <p></p>\n" +
                " </div>\n" +
                "</html>";
            assertEquals(expectedHtml, doc.html());
        }
    }
}