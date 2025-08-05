package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Test suite for W3CDom class functionality.
 * Tests conversion between JSoup DOM and W3C DOM, XPath queries, and serialization.
 */
public class W3CDom_ESTest {

    // ========== Basic Conversion Tests ==========
    
    @Test(timeout = 4000)
    public void testConvertJSoupDocumentWithNamespaces() throws Throwable {
        // Test converting a JSoup document with namespace attributes to W3C DOM
        Document jsoupDoc = Parser.parse("", "V5");
        Document clonedDoc = jsoupDoc.clone();
        Element elementWithNamespaces = clonedDoc.attr("http://www.w3.org/1998/Math/MathML", "http://www.w3.org/2000/svg");
        
        W3CDom converter = new W3CDom();
        Element prependedElement = jsoupDoc.prependChild(elementWithNamespaces);
        converter.fromJsoup(prependedElement);
        
        assertTrue("Converter should be namespace aware", converter.namespaceAware());
    }

    @Test(timeout = 4000)
    public void testConvertSimpleJSoupDocument() throws Throwable {
        // Test basic conversion of JSoup document to W3C DOM
        Document jsoupDoc = Parser.parse(")S", "jsoupSource");
        org.w3c.dom.Document w3cDoc = W3CDom.convert(jsoupDoc);
        String xmlString = W3CDom.asString(w3cDoc, null);
        
        assertEquals("Should produce valid XHTML", 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body>)S</body></html>", 
            xmlString);
    }

    @Test(timeout = 4000)
    public void testConvertBodyFragment() throws Throwable {
        // Test converting HTML body fragment
        Document bodyFragment = Parser.parseBodyFragment("<!DOCMTYPE", "<!DOCMTYPE");
        org.w3c.dom.Document w3cDoc = W3CDom.convert(bodyFragment);
        
        assertNotNull("Conversion should succeed", w3cDoc);
    }

    // ========== Namespace Awareness Tests ==========
    
    @Test(timeout = 4000)
    public void testNamespaceAwarenessSetting() throws Throwable {
        W3CDom converter = new W3CDom();
        assertTrue("Should be namespace aware by default", converter.namespaceAware());
        
        converter.namespaceAware(false);
        assertFalse("Should be able to disable namespace awareness", converter.namespaceAware());
    }

    @Test(timeout = 4000)
    public void testConvertWithNamespaceAwarenessDisabled() throws Throwable {
        W3CDom converter = new W3CDom();
        Element simpleElement = new Element("jsoupSource", "jsoupSource");
        simpleElement.addClass("jsoupSource");
        
        converter.namespaceAware(false);
        converter.fromJsoup(simpleElement);
        
        assertFalse("Namespace awareness should remain disabled", converter.namespaceAware());
    }

    // ========== XPath Query Tests ==========
    
    @Test(timeout = 4000)
    public void testValidXPathQuery() throws Throwable {
        W3CDom converter = new W3CDom();
        Document jsoupDoc = Document.createShell("javax.xml.xpath.XPathFactory:jsoup");
        org.w3c.dom.Document w3cDoc = converter.fromJsoup(jsoupDoc);
        
        // Should not throw exception for valid XPath
        converter.selectXpath("jsoupSource", w3cDoc);
        assertTrue("Converter should remain namespace aware", converter.namespaceAware());
    }

    @Test(timeout = 4000)
    public void testXPathQueryWithContextNode() throws Throwable {
        W3CDom converter = new W3CDom();
        Document jsoupDoc = Parser.parse("javax.xml.xpath.xpathfactory", "javax.xml.xpath.xpathfactory");
        org.w3c.dom.Document w3cDoc = W3CDom.convert(jsoupDoc);
        Node contextNode = converter.contextNode(w3cDoc);
        
        assertNotNull("Context node should be available", contextNode);
        converter.selectXpath("javax.xml.xpath.xpathfactory", contextNode);
        assertTrue("Converter should remain namespace aware", converter.namespaceAware());
    }

    @Test(timeout = 4000)
    public void testInvalidXPathQuery() throws Throwable {
        W3CDom converter = new W3CDom();
        IIOMetadataNode testNode = new IIOMetadataNode();
        
        try {
            converter.selectXpath("-", testNode);
            fail("Should throw exception for invalid XPath");
        } catch(IllegalStateException e) {
            assertTrue("Should contain XPath error message", 
                e.getMessage().contains("Could not evaluate XPath query"));
        }
    }

    @Test(timeout = 4000)
    public void testXPathQueryWithNullContext() throws Throwable {
        W3CDom converter = new W3CDom();
        
        try {
            converter.selectXpath("jsoupSource", (Node) null);
            fail("Should throw exception for null context node");
        } catch(IllegalArgumentException e) {
            assertEquals("Should validate null parameter", 
                "The parameter 'contextNode' must not be null.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testXPathQueryReturningNonNodeList() throws Throwable {
        W3CDom converter = new W3CDom();
        Document jsoupDoc = Document.createShell("javax.xml.xpath.XPathFactory:jsoup");
        org.w3c.dom.Document w3cDoc = converter.fromJsoup(jsoupDoc);
        
        try {
            converter.selectXpath("2", w3cDoc); // Returns number, not NodeList
            fail("Should throw exception when XPath doesn't return NodeList");
        } catch(IllegalStateException e) {
            assertTrue("Should contain conversion error message", 
                e.getMessage().contains("Can not convert #NUMBER to a NodeList"));
        }
    }

    // ========== Source Node Retrieval Tests ==========
    
    @Test(timeout = 4000)
    public void testRetrieveSourceNodesFromConvertedDocument() throws Throwable {
        W3CDom converter = new W3CDom();
        Document jsoupDoc = Parser.parseBodyFragment("jsoupSource", "jsoupSource");
        DocumentImpl w3cDoc = (DocumentImpl) converter.fromJsoup(jsoupDoc);
        
        List<Element> sourceElements = converter.sourceNodes(w3cDoc, Element.class);
        
        assertTrue("Should find source elements", !sourceElements.isEmpty());
        assertTrue("Converter should remain namespace aware", converter.namespaceAware());
    }

    @Test(timeout = 4000)
    public void testRetrieveSourceNodesWithNullNodeList() throws Throwable {
        W3CDom converter = new W3CDom();
        
        try {
            converter.sourceNodes(null, DocumentType.class);
            fail("Should throw exception for null NodeList");
        } catch(IllegalArgumentException e) {
            assertEquals("Should validate null parameter", 
                "Object must not be null", e.getMessage());
        }
    }

    // ========== Serialization Tests ==========
    
    @Test(timeout = 4000)
    public void testSerializeDocumentToString() throws Throwable {
        W3CDom converter = new W3CDom();
        Document jsoupDoc = Parser.parseBodyFragment("jsoupSource", "jsoupSource");
        org.w3c.dom.Document w3cDoc = converter.fromJsoup(jsoupDoc);
        
        String result = converter.asString(w3cDoc);
        assertNotNull("Serialization should produce result", result);
    }

    @Test(timeout = 4000)
    public void testSerializeWithCustomProperties() throws Throwable {
        HashMap<String, String> outputProperties = W3CDom.OutputXml();
        Properties properties = W3CDom.propertiesFromMap(outputProperties);
        
        assertEquals("Should contain XML output properties", 1, properties.size());
    }

    @Test(timeout = 4000)
    public void testSerializeWithEmptyProperties() throws Throwable {
        Attributes attributes = new Attributes();
        Map<String, String> emptyMap = attributes.dataset();
        Properties properties = W3CDom.propertiesFromMap(emptyMap);
        
        assertEquals("Should handle empty properties", 0, properties.size());
    }

    @Test(timeout = 4000)
    public void testSerializeWithNullDocument() throws Throwable {
        W3CDom converter = new W3CDom();
        
        try {
            converter.asString(null);
            fail("Should throw exception for null document");
        } catch(NullPointerException e) {
            // Expected behavior
        }
    }

    // ========== Error Handling Tests ==========
    
    @Test(timeout = 4000)
    public void testConvertNullJSoupDocument() throws Throwable {
        try {
            W3CDom.convert(null);
            fail("Should throw exception for null JSoup document");
        } catch(IllegalArgumentException e) {
            assertEquals("Should validate null parameter", 
                "Object must not be null", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testConvertNullJSoupElement() throws Throwable {
        W3CDom converter = new W3CDom();
        
        try {
            converter.fromJsoup((Element) null);
            fail("Should throw exception for null JSoup element");
        } catch(IllegalArgumentException e) {
            assertEquals("Should validate null parameter", 
                "Object must not be null", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testConvertWithInvalidElementName() throws Throwable {
        W3CDom converter = new W3CDom();
        Element invalidElement = new Element("xmlns", "org.jsoup.nodes.Entities");
        
        try {
            converter.fromJsoup(invalidElement);
            fail("Should throw exception for invalid element name");
        } catch(DOMException e) {
            // Expected - invalid XML element name
        }
    }

    @Test(timeout = 4000)
    public void testConvertWithInvalidTagName() throws Throwable {
        W3CDom converter = new W3CDom();
        Tag invalidTag = new Tag("s#A4<>v~!I!6^W3m");
        Element elementWithInvalidTag = new Element(invalidTag, "org.jsoup.helper.W3CDom$W3CBuilder");
        Document jsoupDoc = Document.createShell("javax.xml.xpath.XPathFactory:jsoup");
        org.w3c.dom.Document w3cDoc = converter.fromJsoup(jsoupDoc);
        
        try {
            converter.convert(elementWithInvalidTag, w3cDoc);
            fail("Should throw exception for invalid tag name");
        } catch(DOMException e) {
            // Expected - invalid XML tag name
        }
    }

    // ========== Builder Tests ==========
    
    @Test(timeout = 4000)
    public void testBuilderWithDocumentType() throws Throwable {
        W3CDom converter = new W3CDom();
        Document jsoupDoc = Document.createShell("javax.xml.xpath.XPathFactory:jsoup");
        org.w3c.dom.Document w3cDoc = converter.fromJsoup(jsoupDoc);
        DocumentType docType = new DocumentType("jsoupSource", "&", "jsoupSource");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        
        // Should handle DocumentType without throwing exception
        builder.traverse(docType);
        assertTrue("Converter should remain namespace aware", converter.namespaceAware());
    }

    @Test(timeout = 4000)
    public void testBuilderTailMethod() throws Throwable {
        Document jsoupDoc = Parser.parse("", "V5");
        org.w3c.dom.Document w3cDoc = W3CDom.convert(jsoupDoc);
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        
        // Should handle tail method call without throwing exception
        builder.tail(jsoupDoc, 88);
        assertEquals("Document quirks mode should be preserved", 
            Document.QuirksMode.quirks, jsoupDoc.quirksMode());
    }

    // ========== Utility Method Tests ==========
    
    @Test(timeout = 4000)
    public void testPropertiesFromMapWithNullInput() throws Throwable {
        try {
            W3CDom.propertiesFromMap(null);
            fail("Should throw exception for null map");
        } catch(NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testOutputHtmlProperties() throws Throwable {
        HashMap<String, String> htmlProperties = W3CDom.OutputHtml();
        assertNotNull("Should return HTML output properties", htmlProperties);
    }

    @Test(timeout = 4000)
    public void testOutputXmlProperties() throws Throwable {
        HashMap<String, String> xmlProperties = W3CDom.OutputXml();
        assertNotNull("Should return XML output properties", xmlProperties);
    }

    // ========== Edge Case Tests ==========
    
    @Test(timeout = 4000)
    public void testConvertDocumentWithSelfReference() throws Throwable {
        Document jsoupDoc = Document.createShell("");
        jsoupDoc.appendChild(jsoupDoc); // Create circular reference
        
        // Should handle self-reference without infinite loop
        W3CDom.convert(jsoupDoc);
    }

    @Test(timeout = 4000)
    public void testConvertElementWithSelfReference() throws Throwable {
        W3CDom converter = new W3CDom();
        Element element = new Element("jsoupSource", "javax.xml.xpath.XPathFactory:jsoup");
        element.prependChild(element); // Create circular reference
        
        // Should handle self-reference without infinite loop
        converter.fromJsoup(element);
    }
}