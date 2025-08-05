package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
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

class XmlTreeBuilderTest {

    // Tests for basic XML parsing functionality
    @Nested
    class BasicParsingTests {
        @Test
        void parsesSimpleXmlDocument() {
            String xml = "<doc id=2 href='/bar'>Foo <br /><link>One</link><link>Two</link></doc>";
            Document doc = Jsoup.parse(xml, "http://foo.com/", Parser.xmlParser());

            String expectedHtml = "<doc id=\"2\" href=\"/bar\">Foo <br /><link>One</link><link>Two</link></doc>";
            assertEquals(expectedHtml, TextUtil.stripNewlines(doc.html()));
            assertEquals("http://foo.com/bar", doc.getElementById("2").absUrl("href"));
        }

        @Test
        void handlesNestedTagsWithImproperClosing() {
            String xml = "<doc><val>One<val>Two</val></bar>Three</doc>";
            Document doc = Jsoup.parse(xml, "http://foo.com/", Parser.xmlParser());

            String expected = "<doc><val>One<val>Two</val>Three</val></doc>";
            assertEquals(expected, TextUtil.stripNewlines(doc.html()));
        }

        @Test
        void parsesCommentsAndDocType() {
            String xml = "<!DOCTYPE HTML><!-- a comment -->One <qux />Two";
            Document doc = Jsoup.parse(xml, "http://foo.com/", Parser.xmlParser());

            assertEquals("<!DOCTYPE HTML><!-- a comment -->One <qux />Two", 
                         TextUtil.stripNewlines(doc.html()));
        }
    }

    // Tests for parser configuration and input handling
    @Nested
    class ParserConfigurationTests {
        @Test
        void usesXmlParserViaJsoupClass() {
            String xml = "<doc><val>One<val>Two</val></bar>Three</doc>";
            Document doc = Jsoup.parse(xml, "http://foo.com/", Parser.xmlParser());

            String expected = "<doc><val>One<val>Two</val>Three</val></doc>";
            assertEquals(expected, TextUtil.stripNewlines(doc.html()));
        }

        @Test
        void parsesFromInputStream() throws IOException, URISyntaxException {
            File xmlFile = new File(XmlTreeBuilder.class.getResource("/htmltests/xml-test.xml").toURI());
            try (InputStream inStream = new FileInputStream(xmlFile)) {
                Document doc = Jsoup.parse(inStream, null, "http://foo.com", Parser.xmlParser());
                assertEquals("<doc><val>One<val>Two</val>Three</val></doc>", 
                            TextUtil.stripNewlines(doc.html()));
            }
        }

        @Test
        void closesReaderAfterParsing() {
            Document doc = Jsoup.parse("Hello", "", Parser.xmlParser());
            TreeBuilder treeBuilder = doc.parser().getTreeBuilder();
            
            assertNull(treeBuilder.reader);
            assertNull(treeBuilder.tokeniser);
        }
    }

    // Tests for XML-specific handling
    @Nested
    class XmlSpecificBehaviorTests {
        @Test
        void doesNotForceSelfClosingOnKnownTags() {
            Document htmlDoc = Jsoup.parse("<br>one</br>");
            assertEquals("<br>\none\n<br>", htmlDoc.body().html());

            Document xmlDoc = Jsoup.parse("<br>one</br>", "", Parser.xmlParser());
            assertEquals("<br>one</br>", xmlDoc.html());
        }

        @Test
        void handlesXmlDeclarationAsSpecialNode() {
            String xml = "<?xml encoding='UTF-8' ?><body>One</body><!-- comment -->";
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
            
            assertEquals("<?xml encoding=\"UTF-8\"?><body>One</body><!-- comment -->", doc.outerHtml());
            assertEquals("#declaration", doc.childNode(0).nodeName());
            assertEquals("#comment", doc.childNode(2).nodeName());
        }

        @Test
        void handlesIncompleteTagAtEof() {
            String xml = "<img src=asdf onerror=\"alert(1)\" x=";
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
            assertEquals("<img src=\"asdf\" onerror=\"alert(1)\" x=\"\"></img>", doc.html());
        }
    }

    // Tests for XML fragments
    @Nested
    class FragmentTests {
        @Test
        void parsesXmlFragments() {
            String xml = "<one src='/foo/' />Two<three><four /></three>";
            List<Node> nodes = Parser.parseXmlFragment(xml, "http://example.com/");
            
            assertEquals(3, nodes.size());
            assertEquals("http://example.com/foo/", nodes.get(0).absUrl("src"));
            assertEquals("one", nodes.get(0).nodeName());
            assertEquals("Two", ((TextNode) nodes.get(1)).text());
        }
    }

    // Tests for output settings
    @Nested
    class OutputSettingsTests {
        @Test
        void defaultsToXmlOutputSyntax() {
            Document doc = Jsoup.parse("x", "", Parser.xmlParser());
            assertEquals(Syntax.xml, doc.outputSettings().syntax());
        }

        @Test
        void disablesPrettyPrintingByDefault() {
            String xml = "\n\n<div><one>One</one><one>\n Two</one>\n</div>\n ";
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
            assertEquals(xml, doc.html());
        }

        @Test
        void createsValidXmlProlog() {
            Document document = Document.createShell("");
            document.outputSettings().syntax(Syntax.xml);
            document.charset(StandardCharsets.UTF_8);
            
            String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<html>\n"
                + " <head></head>\n"
                + " <body></body>\n"
                + "</html>";
            
            assertEquals(expected, document.outerHtml());
        }
    }

    // Tests for case sensitivity
    @Nested
    class CaseSensitivityTests {
        @Test
        void preservesTagCaseByDefault() {
            String xml = "<CHECK>One</CHECK><TEST ID=1>Check</TEST>";
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
            assertEquals("<CHECK>One</CHECK><TEST ID=\"1\">Check</TEST>", 
                         TextUtil.stripNewlines(doc.html()));
        }

        @Test
        void preservesCaseWhenAppending() {
            String xml = "<One>One</One>";
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
            doc.select("One").append("<Two ID=2>Two</Two>");
            
            assertEquals("<One>One<Two ID=\"2\">Two</Two></One>", 
                         TextUtil.stripNewlines(doc.html()));
        }

        @Test
        void normalizesCaseWhenConfigured() {
            String xml = "<TEST ID=1>Check</TEST>";
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser().settings(ParseSettings.htmlDefault));
            assertEquals("<test id=\"1\">Check</test>", TextUtil.stripNewlines(doc.html()));
        }

        @Test
        void normalizesMismatchedTags() {
            Parser parser = Parser.xmlParser().settings(ParseSettings.htmlDefault);
            Document doc = Jsoup.parse("<div>test</DIV><p></p>", "", parser);
            assertEquals("<div>test</div><p></p>", doc.html());
        }
    }

    // Tests for CDATA handling
    @Nested
    class CDataTests {
        @Test
        void roundTripsCdataContent() {
            String xml = "<div id=1><![CDATA[\n<html>\n <foo><&amp;]]></div>";
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
            Element div = doc.getElementById("1");
            
            assertEquals("<html>\n <foo><&amp;", div.text());
            assertEquals(0, div.children().size());
            assertEquals(1, div.childNodeSize());
            assertEquals("<div id=\"1\"><![CDATA[\n<html>\n <foo><&amp;]]></div>", div.outerHtml());
        }

        @Test
        void preservesWhitespaceInCdata() {
            String xml = "<script type=\"text/javascript\">//<![CDATA[\n\n  foo();\n//]]></script>";
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
            
            assertEquals(xml, doc.outerHtml());
            assertEquals("//\n\n  foo();\n//", doc.selectFirst("script").text());
        }
    }

    // Tests for special syntax handling
    @Nested
    class SpecialSyntaxTests {
        @Test
        void handlesMalformedXmlDeclaration() {
            String xml = "<?xml version='1.0'><val>One</val>";
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
            assertEquals("One", doc.select("val").text());
        }

        @Test
        void convertsLtInScriptToComment() {
            String html = "<script> var a=\"<?\"; var b=\"?>\"; </script>";
            Document doc = Jsoup.parse(html, "", Parser.xmlParser());
            assertEquals("<script> var a=\"<!--?\"; var b=\"?-->\"; </script>", doc.html());
        }

        @Test
        void dropsDuplicateCaseSensitiveAttributes() {
            String html = "<p One=One ONE=Two one=Three One=Four ONE=Five two=Six two=Seven Two=Eight>Text</p>";
            Parser parser = Parser.xmlParser().setTrackErrors(10);
            Document doc = parser.parseInput(html, "");
            
            String expected = "<p One=\"One\" ONE=\"Two\" one=\"Three\" two=\"Six\" Two=\"Eight\">Text</p>";
            assertEquals(expected, doc.selectFirst("p").outerHtml());
        }
    }

    // Tests for XML declarations
    @Nested
    class DeclarationTests {
        @Test
        void parsesDeclarationAttributes() {
            String xml = "<?xml version='1' encoding='UTF-8' something='else'?><val>One</val>";
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
            XmlDeclaration decl = (XmlDeclaration) doc.childNode(0);
            
            assertEquals("1", decl.attr("version"));
            assertEquals("UTF-8", decl.attr("encoding"));
            assertEquals("else", decl.attr("something"));
            assertEquals("version=\"1\" encoding=\"UTF-8\" something=\"else\"", decl.getWholeDeclaration());
            assertEquals("<?xml version=\"1\" encoding=\"UTF-8\" something=\"else\"?>", decl.outerHtml());
        }

        @Test
        void handlesDeclarationWithoutAttributes() {
            String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<?myProcessingInstruction My Processing instruction.?>";
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
            XmlDeclaration decl = (XmlDeclaration) doc.childNode(2);
            
            assertEquals("myProcessingInstruction", decl.name());
            assertTrue(decl.hasAttr("My"));
            assertEquals("<?myProcessingInstruction My Processing instruction.?>", decl.outerHtml());
        }

        @Test
        void preservesDeclarationCase() {
            String xml = "<?XML version='1' encoding='UTF-8' something='else'?>";
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
            assertEquals("<?XML version=\"1\" encoding=\"UTF-8\" something=\"else\"?>", doc.outerHtml());
        }

        @Test
        void handlesDeclarationWithGtInAttribute() {
            String xml = "<x><?xmlDeclaration att1=\"value1\" att2=\"&lt;val2>\"?></x>";
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            XmlDeclaration decl = (XmlDeclaration) doc.expectFirst("x").childNode(0);
            assertEquals("<val2>", decl.attr("att2"));
        }

        @Test
        void validatesXmlHeader() {
            String xml = "<?xml version=\"1.0\"?>\n<root></root>";
            Document doc = Jsoup.parse(xml, Parser.xmlParser().setTrackErrors(10));
            assertEquals(0, doc.parser().getErrors().size());
            assertEquals(xml, doc.html());
        }
    }

    // Tests for namespace handling
    @Nested
    class NamespaceTests {
        @Test
        void elementsInheritXmlNamespace() {
            String xml = "<foo><bar><div><svg><math>Qux</bar></foo>";
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            assertXmlNamespace(doc);

            for (Element el : doc.select("*")) {
                assertXmlNamespace(el);
            }
        }

        @Test
        void handlesXmlnsDefinitions() {
            String xml = "<?xml version=\"1.0\"?>\n"
                + "<!-- both namespace prefixes are available throughout -->\n"
                + "<bk:book xmlns:bk=\"urn:loc.gov:books\" xmlns:isbn=\"urn:ISBN:0-395-36341-6\">\n"
                + "    <bk:title>Cheaper by the Dozen</bk:title>\n"
                + "    <isbn:number>1568491379</isbn:number>\n"
                + "</bk:book>";
            
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            Element book = doc.expectFirst("bk|book");
            assertEquals("urn:loc.gov:books", book.tag().namespace());
        }

        @Test
        void handlesUnprefixedDefaultNamespace() {
            String xml = "<?xml version=\"1.0\"?>\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                + "  <head><title>Title</title></head>\n"
                + "  <body><p>Content</p></body>\n"
                + "</html>";
            
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            assertEquals(NamespaceHtml, doc.expectFirst("html").tag().namespace());
        }

        @Test
        void handlesNamespacedAttributes() {
            String xml = "<x xmlns:edi='http://ecommerce.example.org/schema'>\n"
                + "  <lineItem edi:taxClass=\"exempt\" other=foo>Item</lineItem>\n"
                + "</x>";
            
            Document doc = Jsoup.parse(xml, Parser.xmlParser());
            Attribute taxAttr = doc.expectFirst("lineItem").attribute("edi:taxClass");
            
            assertEquals("edi", taxAttr.prefix());
            assertEquals("taxClass", taxAttr.localName());
            assertEquals("http://ecommerce.example.org/schema", taxAttr.namespace());
        }
    }

    // Helper method for namespace assertions
    private static void assertXmlNamespace(Node node) {
        if (node instanceof Element) {
            Element el = (Element) node;
            assertEquals(NamespaceXml, el.tag().namespace(), 
                "Element " + el.tagName() + " not in XML namespace");
        }
    }
}