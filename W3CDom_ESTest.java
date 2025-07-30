package org.jsoup.helper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.metadata.IIOMetadataNode;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class W3CDom_ESTest extends W3CDom_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testNamespaceAwareConversion() throws Throwable {
        Document document = Parser.parse("", "V5");
        Document clonedDocument = document.clone();
        Element element = clonedDocument.attr("http://www.w3.org/1998/Math/MathML", "http://www.w3.org/2000/svg");
        W3CDom w3CDom = new W3CDom();
        document.prependChild(element);
        w3CDom.fromJsoup(element);
        assertTrue(w3CDom.namespaceAware());
    }

    @Test(timeout = 4000)
    public void testFromJsoupWithElementThrowsAssertionError() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Element element = new Element("jsoupSource", "javax.xml.xpath.XPathFactory:jsoup");
        Document document = new Document("javax.xml.xpath.XPathFactory:jsoup");
        element.prependChild(document);
        try {
            w3CDom.fromJsoup((Element) document);
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testConvertDocumentQuirksMode() throws Throwable {
        Document document = Parser.parse("", "V5");
        org.w3c.dom.Document w3cDocument = W3CDom.convert(document);
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        builder.tail(document, 88);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }

    @Test(timeout = 4000)
    public void testHeadThrowsDOMException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Document.createShell("");
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(document);
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        try {
            builder.head(document, 0);
            fail("Expecting exception: DOMException");
        } catch (DOMException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testSelectXpathWithNamespaceAware() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Document.createShell("javax.xml.xpath.XPathFactory:jsoup");
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(document);
        w3CDom.selectXpath("jsoupSource", w3cDocument);
        assertTrue(w3CDom.namespaceAware());
    }

    @Test(timeout = 4000)
    public void testPropertiesFromEmptyMap() throws Throwable {
        Attributes attributes = new Attributes();
        Map<String, String> map = attributes.dataset();
        Properties properties = W3CDom.propertiesFromMap(map);
        assertEquals(0, properties.size());
    }

    @Test(timeout = 4000)
    public void testNamespaceAwareSetting() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        w3CDom.namespaceAware(true);
        assertTrue(w3CDom.namespaceAware());
    }

    @Test(timeout = 4000)
    public void testSourceNodesThrowsDOMException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Class<Document> documentClass = Document.class;
        IIOMetadataNode metadataNode = new IIOMetadataNode("jsoupSource");
        NodeList nodeList = metadataNode.getElementsByTagName("jsoupSource");
        try {
            w3CDom.sourceNodes(nodeList, documentClass);
            fail("Expecting exception: DOMException");
        } catch (DOMException e) {
            // Expected exception
            verifyException("javax.imageio.metadata.IIOMetadataNode", e);
        }
    }

    @Test(timeout = 4000)
    public void testSourceNodesWithNullNodeListThrowsIllegalArgumentException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Class<DocumentType> documentTypeClass = DocumentType.class;
        try {
            w3CDom.sourceNodes(null, documentTypeClass);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testSourceNodesWithNullNodeListThrowsNullPointerException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        IIOMetadataNode metadataNode = new IIOMetadataNode();
        metadataNode.insertBefore(metadataNode, metadataNode);
        Class<XmlDeclaration> xmlDeclarationClass = XmlDeclaration.class;
        try {
            w3CDom.sourceNodes((NodeList) metadataNode, xmlDeclarationClass);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("org.jsoup.helper.W3CDom", e);
        }
    }

    @Test(timeout = 4000)
    public void testSelectXpathWithInvalidExpressionThrowsIllegalStateException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        IIOMetadataNode metadataNode = new IIOMetadataNode();
        try {
            w3CDom.selectXpath("-", (Node) metadataNode);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
            verifyException("org.jsoup.helper.W3CDom", e);
        }
    }

    @Test(timeout = 4000)
    public void testSelectXpathWithNullContextNodeThrowsIllegalArgumentException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        try {
            w3CDom.selectXpath("jsoupSource", (Node) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testSelectXpathWithInvalidQueryThrowsIllegalStateException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Document.createShell("javax.xml.xpath.XPathFactory:jsoup");
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(document);
        try {
            w3CDom.selectXpath("2", w3cDocument);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
            verifyException("org.jsoup.helper.W3CDom", e);
        }
    }

    @Test(timeout = 4000)
    public void testPropertiesFromNullMapThrowsNullPointerException() throws Throwable {
        try {
            W3CDom.propertiesFromMap(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("java.util.Hashtable", e);
        }
    }

    @Test(timeout = 4000)
    public void testFromJsoupWithElementThrowsDOMException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Element element = new Element("xmlns", "org.jsoup.nodes.Entities");
        try {
            w3CDom.fromJsoup(element);
            fail("Expecting exception: DOMException");
        } catch (DOMException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testFromJsoupWithNullElementThrowsIllegalArgumentException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        try {
            w3CDom.fromJsoup((Element) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testFromJsoupWithSelfReferencingElement() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Element element = new Element("jsoupSource", "javax.xml.xpath.XPathFactory:jsoup");
        element.prependChild(element);
        w3CDom.fromJsoup(element);
    }

    @Test(timeout = 4000)
    public void testFromJsoupWithNullFactoryThrowsNullPointerException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Element element = new Element("javax.xml.xpath.XPathFactory:jsoup", "javax.xml.xpath.XPathFactory:jsoup");
        w3CDom.factory = null;
        try {
            w3CDom.fromJsoup(element);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("org.jsoup.helper.W3CDom", e);
        }
    }

    @Test(timeout = 4000)
    public void testFromJsoupWithInvalidDocumentThrowsDOMException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Document.createShell("=68QmV3%[sf)G~");
        document.prependElement("::/~/%3V}");
        try {
            w3CDom.fromJsoup(document);
            fail("Expecting exception: DOMException");
        } catch (DOMException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testFromJsoupWithNullDocumentThrowsIllegalArgumentException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        try {
            w3CDom.fromJsoup((Document) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testFromJsoupWithSelfReferencingDocument() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Parser.parse("jsoupSource", "javax.xml.xpath.XPathFactory:jsoup");
        document.prependChild(document);
        w3CDom.fromJsoup(document);
    }

    @Test(timeout = 4000)
    public void testFromJsoupWithNullFactoryAndDocumentThrowsNullPointerException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        w3CDom.factory = null;
        Document document = Parser.parse("jsoupSource", "jsoupSource");
        try {
            w3CDom.fromJsoup(document);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("org.jsoup.helper.W3CDom", e);
        }
    }

    @Test(timeout = 4000)
    public void testFromJsoupWithInvalidDocumentThrowsAssertionError() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = new Document("jsoupSource");
        try {
            w3CDom.fromJsoup(document);
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testConvertWithNullElementAndDocumentThrowsNullPointerException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        try {
            w3CDom.convert((Element) null, (org.w3c.dom.Document) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("org.jsoup.helper.W3CDom$W3CBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testConvertWithNullDocumentThrowsNullPointerException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Element element = new Element("javax.xml.xpath.XPathFactory:jsoup");
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(element);
        try {
            w3CDom.convert((Document) null, w3cDocument);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("org.jsoup.helper.W3CDom", e);
        }
    }

    @Test(timeout = 4000)
    public void testConvertWithNullDocumentThrowsIllegalArgumentException() throws Throwable {
        try {
            W3CDom.convert((Document) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testConvertWithSelfReferencingDocument() throws Throwable {
        Document document = Document.createShell("");
        document.appendChild(document);
        W3CDom.convert(document);
    }

    @Test(timeout = 4000)
    public void testConvertWithInvalidDocumentThrowsAssertionError() throws Throwable {
        Document document = new Document("*zHMY,@{AzE8{");
        try {
            W3CDom.convert(document);
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testContextNodeWithNullDocumentThrowsNullPointerException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        try {
            w3CDom.contextNode((org.w3c.dom.Document) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("org.jsoup.helper.W3CDom", e);
        }
    }

    @Test(timeout = 4000)
    public void testAsStringWithEmptyKeyThrowsIllegalArgumentException() throws Throwable {
        Document document = Document.createShell("554\"");
        W3CDom w3CDom = new W3CDom();
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(document);
        Attributes attributes = new Attributes();
        attributes.put("", true);
        Map<String, String> map = attributes.dataset();
        try {
            W3CDom.asString(w3cDocument, map);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testAsStringWithEmptyKeyThrowsStringIndexOutOfBoundsException() throws Throwable {
        HashMap<String, String> outputHtmlMap = W3CDom.OutputHtml();
        outputHtmlMap.put("", "FS");
        try {
            W3CDom.asString((org.w3c.dom.Document) null, outputHtmlMap);
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testAsStringWithNullDocumentThrowsNullPointerException() throws Throwable {
        HashMap<String, String> outputHtmlMap = W3CDom.OutputHtml();
        try {
            W3CDom.asString((org.w3c.dom.Document) null, outputHtmlMap);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("org.jsoup.helper.W3CDom", e);
        }
    }

    @Test(timeout = 4000)
    public void testAsStringWithNullDocumentThrowsNullPointerException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        try {
            w3CDom.asString((org.w3c.dom.Document) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("org.jsoup.helper.W3CDom", e);
        }
    }

    @Test(timeout = 4000)
    public void testConvertWithInvalidElementThrowsAssertionError() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = new Document("javax.xml.xpath.XPathFactory:jsoup");
        Document parsedDocument = Parser.parse("e93?", "&f:_3v-DWZ");
        org.w3c.dom.Document w3cDocument = W3CDom.convert(parsedDocument);
        try {
            w3CDom.convert((Element) document, w3cDocument);
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testConvertWithInvalidElementThrowsDOMException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Parser.parseBodyFragment("", "");
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup((Element) document);
        try {
            w3CDom.convert((Element) document, w3cDocument);
            fail("Expecting exception: DOMException");
        } catch (DOMException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testConvertWithInvalidElementThrowsDOMException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Tag tag = new Tag("s#A4<>v~!I!6^W3m");
        Element element = new Element(tag, "org.jsoup.helper.W3CDom$W3CBuilder");
        Document document = Document.createShell("javax.xml.xpath.XPathFactory:jsoup");
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(document);
        try {
            w3CDom.convert(element, w3cDocument);
            fail("Expecting exception: DOMException");
        } catch (DOMException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testFromJsoupWithDoctype() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Parser.parse("<!DOCTYPE", "javax.xml.xpath.XPathFactory:jsoup");
        w3CDom.fromJsoup((Element) document);
        assertTrue(w3CDom.namespaceAware());
    }

    @Test(timeout = 4000)
    public void testAsStringWithNullProperties() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Parser.parse(")S", "jsoupSource");
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(document);
        String result = W3CDom.asString(w3cDocument, null);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body>)S</body></html>", result);
    }

    @Test(timeout = 4000)
    public void testTraverseWithInvalidElementThrowsDOMException() throws Throwable {
        Document document = Parser.parse("", "V5");
        org.w3c.dom.Document w3cDocument = W3CDom.convert(document);
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        Parser parser = Parser.xmlParser();
        StringReader stringReader = new StringReader("xmlns");
        Document parsedDocument = parser.parseInput(stringReader, "$.|O:LS7]cD\u0001o5k\"5h");
        Element element = parsedDocument.attr("http://www.w3.org/1998/Math/MathML", "http://www.w3.org/2000/svg");
        try {
            builder.traverse(element);
            fail("Expecting exception: DOMException");
        } catch (DOMException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testTraverseWithInvalidElementThrowsDOMException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Parser.parseBodyFragment("javax.xml.xpath.XPathFactory:jsoup", "xmlns:");
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(document);
        document.attr("xmlns:", "xmlns:");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        try {
            builder.traverse(document);
            fail("Expecting exception: DOMException");
        } catch (DOMException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testTraverseWithInvalidElementThrowsDOMException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Parser.parseBodyFragment("javax.xml.xpath.XPathFactory:jsoup", "xmlns:");
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(document);
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        Element element = document.id("javax.xml.xpath.XPathFactory:jsoup");
        try {
            builder.traverse(element);
            fail("Expecting exception: DOMException");
        } catch (DOMException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNamespaceAwareSettingFalse() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        assertTrue(w3CDom.namespaceAware());

        Element element = new Element("jsoupSource", "jsoupSource");
        element.addClass("jsoupSource");
        W3CDom updatedW3CDom = w3CDom.namespaceAware(false);
        updatedW3CDom.fromJsoup(element);
        assertFalse(w3CDom.namespaceAware());
    }

    @Test(timeout = 4000)
    public void testTraverseWithInvalidElementThrowsDOMException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Parser.parseBodyFragment("javax.xml.xpath.XPathFactory:jsoup", "javax.xml.xpath.XPathFactory:jsoup");
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(document);
        Element element = document.attr("520&v[}X+}:5ZJj 0", "jsoupSource");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        try {
            builder.traverse(element);
            fail("Expecting exception: DOMException");
        } catch (DOMException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testTraverseWithDocumentType() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Document.createShell("javax.xml.xpath.XPathFactory:jsoup");
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(document);
        DocumentType documentType = new DocumentType("jsoupSource", "&", "jsoupSource");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        builder.traverse(documentType);
        assertTrue(w3CDom.namespaceAware());
    }

    @Test(timeout = 4000)
    public void testTraverseWithInvalidDataNodeThrowsDOMException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Parser.parse("jsoupSource", "javax.xml.xpath.XPathFactory:jsoup");
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(document);
        DataNode dataNode = new DataNode("javax.xml.xpath.XPathFactory:jsoup");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDocument);
        try {
            builder.traverse(dataNode);
            fail("Expecting exception: DOMException");
        } catch (DOMException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testConvertWithValidDocument() throws Throwable {
        Document document = Parser.parseBodyFragment("<!DOCMTYPE", "<!DOCMTYPE");
        org.w3c.dom.Document w3cDocument = W3CDom.convert(document);
        assertNotNull(w3cDocument);
    }

    @Test(timeout = 4000)
    public void testNamespaceAwareSettingFalse() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        assertTrue(w3CDom.namespaceAware());

        Element element = new Element("javax.xml.xpath.XPathFactory:jsoup");
        W3CDom updatedW3CDom = w3CDom.namespaceAware(false);
        updatedW3CDom.fromJsoup(element);
        assertFalse(w3CDom.namespaceAware());
    }

    @Test(timeout = 4000)
    public void testSourceNodesWithFormElement() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Document.createShell("javax.xml.xpath.XPathFactory:jsoup");
        DocumentImpl documentImpl = (DocumentImpl) w3CDom.fromJsoup(document);
        Class<FormElement> formElementClass = FormElement.class;
        w3CDom.sourceNodes((NodeList) documentImpl, formElementClass);
        assertTrue(w3CDom.namespaceAware());
    }

    @Test(timeout = 4000)
    public void testSourceNodesWithElement() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Parser.parseBodyFragment("jsoupSource", "jsoupSource");
        DocumentImpl documentImpl = (DocumentImpl) w3CDom.fromJsoup(document);
        Class<Element> elementClass = Element.class;
        List<Element> elements = w3CDom.sourceNodes((NodeList) documentImpl, elementClass);
        assertTrue(w3CDom.namespaceAware());
        assertFalse(elements.isEmpty());
    }

    @Test(timeout = 4000)
    public void testConvertWithShallowCloneThrowsAssertionError() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Parser.parse("jsoupSource", "jsoupSource");
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(document);
        Document shallowClone = document.shallowClone();
        try {
            w3CDom.convert(shallowClone, w3cDocument);
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testAsStringWithNullDocumentThrowsIllegalArgumentException() throws Throwable {
        HashMap<String, String> outputHtmlMap = W3CDom.OutputHtml();
        outputHtmlMap.put("jsoupSource", "javax.xml.xpath.XPathFactory:jsoup");
        try {
            W3CDom.asString((org.w3c.dom.Document) null, outputHtmlMap);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNamespaceAwareDefaultSetting() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        assertTrue(w3CDom.namespaceAware());
    }

    @Test(timeout = 4000)
    public void testConvertWithInvalidDocumentThrowsDOMException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Parser.parse("jsoupSource", "jsoupSource");
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(document);
        try {
            w3CDom.convert(document, w3cDocument);
            fail("Expecting exception: DOMException");
        } catch (DOMException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testPropertiesFromOutputXml() throws Throwable {
        HashMap<String, String> outputXmlMap = W3CDom.OutputXml();
        Properties properties = W3CDom.propertiesFromMap(outputXmlMap);
        assertEquals(1, properties.size());
    }

    @Test(timeout = 4000)
    public void testSelectXpathWithNullDocumentThrowsIllegalArgumentException() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        try {
            w3CDom.selectXpath("z4oSaE[\")", (org.w3c.dom.Document) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNamespaceAwareSettingFalse() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        assertTrue(w3CDom.namespaceAware());

        W3CDom updatedW3CDom = w3CDom.namespaceAware(false);
        assertFalse(updatedW3CDom.namespaceAware());
    }

    @Test(timeout = 4000)
    public void testSelectXpathWithValidNode() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Parser.parse("javax.xml.xpath.xpathfactory", "javax.xml.xpath.xpathfactory");
        org.w3c.dom.Document w3cDocument = W3CDom.convert(document);
        Node contextNode = w3CDom.contextNode(w3cDocument);
        assertNotNull(contextNode);

        w3CDom.selectXpath("javax.xml.xpath.xpathfactory", contextNode);
        assertTrue(w3CDom.namespaceAware());
    }

    @Test(timeout = 4000)
    public void testAsStringWithValidDocument() throws Throwable {
        W3CDom w3CDom = new W3CDom();
        Document document = Parser.parseBodyFragment("jsoupSource", "jsoupSource");
        org.w3c.dom.Document w3cDocument = w3CDom.fromJsoup(document);
        w3CDom.asString(w3cDocument);
    }
}