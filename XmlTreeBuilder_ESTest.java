package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link XmlTreeBuilder}.
 * This class focuses on verifying the behavior of the XML parser, including node insertion,
 * stack manipulation, namespace handling, and error conditions.
 */
public class XmlTreeBuilderTest {

    @Test
    public void defaultSettingsArePreserved() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // Act & Assert
        assertNotNull(builder.defaultSettings());
        assertTrue("XML parser should preserve tag case by default", builder.defaultSettings().preserveTagCase());
        assertNotNull(builder.defaultTagSet());
    }

    @Test
    public void newInstanceCreatesIndependentBuilder() {
        // Arrange
        XmlTreeBuilder originalBuilder = new XmlTreeBuilder();

        // Act
        XmlTreeBuilder newBuilder = originalBuilder.newInstance();

        // Assert
        assertNotNull(newBuilder);
        assertEquals(originalBuilder.defaultNamespace(), newBuilder.defaultNamespace());
    }

    @Test
    public void simpleXmlParse() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<doc><item>Hello</item></doc>";

        // Act
        Document doc = builder.parse(xml, "http://example.com/");

        // Assert
        assertEquals("<doc><item>Hello</item></doc>", doc.body().html());
        Element item = doc.expectFirst("item");
        assertEquals("Hello", item.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseWithNullStringThrowsException() {
        new XmlTreeBuilder().parse((String) null, "http://example.com/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseWithNullBaseUriThrowsException() {
        new XmlTreeBuilder().parse("<doc/>", null);
    }

    @Test
    public void parseFragmentInitializesBuilderState() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element context = new Element("div");

        // Act
        builder.initialiseParseFragment(context);

        // Assert
        assertEquals("div", builder.currentElement().tagName());
        assertEquals(Parser.NamespaceXml, builder.defaultNamespace());
    }

    @Test
    public void insertElementForStartTag() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader(""), "http://example.com/", new Parser(builder));

        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("p", new Attributes());

        // Act
        builder.insertElementFor(startTag);

        // Assert
        Element inserted = builder.currentElement();
        assertEquals("p", inserted.tagName());
        assertEquals("#root", inserted.parent().tagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertElementForStartTagWithEmptyNameThrowsException() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader(""), "http://example.com/", new Parser(builder));
        Token.StartTag startTag = new Token.StartTag(); // Name is null by default
        startTag.nameAttr("", new Attributes()); // Set to empty

        // Act
        builder.insertElementFor(startTag);
    }

    @Test
    public void insertLeafNodeAttachesToCurrentElement() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader(""), "http://example.com/", new Parser(builder)); // current is #root
        LeafNode comment = new Comment("test");

        // Act
        builder.insertLeafNode(comment);

        // Assert
        assertEquals(1, builder.currentElement().childNodeSize());
        assertEquals(comment, builder.currentElement().childNode(0));
    }

    @Test
    public void insertCDataNode() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<doc></doc>", "http://example.com/");
        Token.CData cdata = new Token.CData("This is CDATA");

        // Act
        builder.insertCharacterFor(cdata);

        // Assert
        Element docEl = doc.expectFirst("doc");
        assertEquals(1, docEl.childNodeSize());
        assertTrue(docEl.childNode(0) instanceof CDataNode);
        assertEquals("This is CDATA", ((CDataNode) docEl.childNode(0)).text());
    }
    
    @Test
    public void popRemovesAndReturnsTopElementFromStack() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader(""), "http://example.com/", new Parser(builder));
        builder.processStartTag("div");
        assertEquals("div", builder.currentElement().tagName());

        // Act
        Element popped = builder.pop();

        // Assert
        assertEquals("div", popped.tagName());
        assertEquals("#root", builder.currentElement().tagName());
    }

    @Test(expected = NoSuchElementException.class)
    public void popOnEmptyStackThrowsException() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        // Do not initialize, so stack is empty
        
        // Act
        builder.pop();
    }

    @Test
    public void popStackToCloseRemovesElementsUntilTarget() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader(""), "http://example.com/", new Parser(builder));
        builder.processStartTag("div");
        builder.processStartTag("p");
        builder.processStartTag("span");
        assertEquals("span", builder.currentElement().tagName());

        Token.EndTag endTag = new Token.EndTag();
        endTag.name("p");

        // Act
        builder.popStackToClose(endTag);

        // Assert
        // The stack was [#root, div, p, span]. popStackToClose("p") should pop "span" and "p".
        assertEquals("div", builder.currentElement().tagName());
    }

    @Test
    public void processEndTagPopsElementFromStack() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Parser parser = new Parser(builder);
        parser.parseInput("<div><p></p></div>", "http://example.com/");
        
        // Manually process tokens to control state
        builder.process(new Token.StartTag().name("div"));
        builder.process(new Token.StartTag().name("p"));
        assertEquals("p", builder.currentElement().tagName());

        // Act
        boolean result = builder.process(new Token.EndTag().name("p"));

        // Assert
        assertTrue(result);
        assertEquals("div", builder.currentElement().tagName());
    }

    @Test
    public void correctlyParsesAndRepresentsNamespaces() {
        // Arrange
        String xml = "<root xmlns:ex='http://example.com/'><ex:item>Test</ex:item></root>";
        
        // Act
        Document doc = Parser.xmlParser().parseInput(xml, "http://example.com/");

        // Assert
        Element root = doc.expectFirst("root");
        assertNotNull(root);
        
        Element item = doc.expectFirst("ex:item");
        assertEquals("ex:item", item.tagName());
        assertEquals("Test", item.text());
        
        // Verify namespace is correctly associated with the tag
        assertEquals("http://example.com/", item.tag().namespace());
    }

    @Test
    public void handlesXmlDeclarations() {
        // Arrange
        String xml = "<?xml version='1.0' encoding='UTF-8' something='true'?> <doc/>";
        
        // Act
        Document doc = new XmlTreeBuilder().parse(xml, "http://example.com/");
        
        // Assert
        assertEquals(1, doc.childNodeSize()); // XmlDeclaration is a child of Document
        org.jsoup.nodes.XmlDeclaration decl = (org.jsoup.nodes.XmlDeclaration) doc.childNode(0);
        assertEquals("1.0", decl.attr("version"));
        assertEquals("UTF-8", decl.attr("encoding"));
        assertEquals("true", decl.attr("something"));
        assertEquals("doc", doc.expectFirst("doc").tagName());
    }
}