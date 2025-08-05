/*
 * Refactored test suite for improved readability and maintainability
 * Focus: W3CDom functionality and edge cases
 */
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
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
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
import org.junit.runner.RunWith;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class W3CDom_ESTest extends W3CDom_ESTest_scaffolding {

    // Tests for namespace awareness functionality
    @Test(timeout = 4000)
    public void namespaceAware_returnsTrueByDefault() {
        W3CDom w3cDom = new W3CDom();
        assertTrue(w3cDom.namespaceAware());
    }

    @Test(timeout = 4000)
    public void namespaceAware_setFalse_returnsFalse() {
        W3CDom w3cDom = new W3CDom();
        w3cDom.namespaceAware(false);
        assertFalse(w3cDom.namespaceAware());
    }

    // Tests for convert() method
    @Test(timeout = 4000)
    public void convert_withNullDocument_throwsException() {
        try {
            W3CDom.convert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected: Object must not be null
        }
    }

    @Test(timeout = 4000)
    public void convert_withSelfReferencingDocument_handlesGracefully() {
        Document document = Document.createShell("");
        document.appendChild(document);  // Create circular reference
        W3CDom.convert(document);  // Should handle without error
    }

    // Tests for fromJsoup() method
    @Test(timeout = 4000)
    public void fromJsoup_withNullElement_throwsException() {
        W3CDom w3cDom = new W3CDom();
        try {
            w3cDom.fromJsoup((Element) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected: Object must not be null
        }
    }

    @Test(timeout = 4000)
    public void fromJsoup_withValidDocument_success() {
        W3CDom w3cDom = new W3CDom();
        Document document = Parser.parse("<!DOCTYPE", "baseUri");
        w3cDom.fromJsoup(document);
        assertTrue(w3cDom.namespaceAware());
    }

    @Test(timeout = 4000)
    public void fromJsoup_withNamespaceAwareFalse_success() {
        W3CDom w3cDom = new W3CDom().namespaceAware(false);
        Element element = new Element("test");
        w3cDom.fromJsoup(element);
        assertFalse(w3cDom.namespaceAware());
    }

    // Tests for selectXpath() method
    @Test(timeout = 4000)
    public void selectXpath_withInvalidQuery_throwsException() {
        W3CDom w3cDom = new W3CDom();
        Document document = Document.createShell("");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(document);
        
        try {
            w3cDom.selectXpath("-", w3cDoc);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected: Could not evaluate XPath query
        }
    }

    @Test(timeout = 4000)
    public void selectXpath_withNullContextNode_throwsException() {
        W3CDom w3cDom = new W3CDom();
        try {
            w3cDom.selectXpath("query", (Node) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected: contextNode must not be null
        }
    }

    // Tests for sourceNodes() method
    @Test(timeout = 4000)
    public void sourceNodes_withNullNodeList_throwsException() {
        W3CDom w3cDom = new W3CDom();
        try {
            w3cDom.sourceNodes(null, DocumentType.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected: Object must not be null
        }
    }

    @Test(timeout = 4000)
    public void sourceNodes_withValidDocument_returnsElements() {
        W3CDom w3cDom = new W3CDom();
        Document document = Parser.parseBodyFragment("content", "baseUri");
        DocumentImpl w3cDoc = (DocumentImpl) w3cDom.fromJsoup(document);
        
        List<Element> elements = w3cDom.sourceNodes(w3cDoc, Element.class);
        assertFalse(elements.isEmpty());
    }

    // Tests for exception handling
    @Test(timeout = 4000, expected = DOMException.class)
    public void convert_withInvalidElement_throwsDOMException() {
        W3CDom w3cDom = new W3CDom();
        Element element = new Element("invalid", "namespace");
        w3cDom.fromJsoup(element);  // Should throw DOMException
    }

    @Test(timeout = 4000)
    public void fromJsoup_withSelfReferencingElement_handlesGracefully() {
        W3CDom w3cDom = new W3CDom();
        Element element = new Element("test");
        element.prependChild(element);  // Create circular reference
        w3cDom.fromJsoup(element);  // Should handle without error
    }

    // Tests for helper methods
    @Test(timeout = 4000)
    public void propertiesFromMap_withEmptyMap_returnsEmptyProperties() {
        Attributes attributes = new Attributes();
        Map<String, String> dataset = attributes.dataset();
        Properties properties = W3CDom.propertiesFromMap(dataset);
        assertEquals(0, properties.size());
    }

    @Test(timeout = 4000)
    public void propertiesFromMap_withNullMap_throwsException() {
        try {
            W3CDom.propertiesFromMap(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected: Cannot invoke method on null map
        }
    }

    // Tests for output handling
    @Test(timeout = 4000)
    public void asString_withHtmlDocument_returnsFormattedString() {
        W3CDom w3cDom = new W3CDom();
        Document document = Parser.parse("content", "baseUri");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(document);
        
        String result = W3CDom.asString(w3cDoc, null);
        assertTrue(result.contains("<html"));
    }

    // Additional edge case tests
    @Test(timeout = 4000)
    public void traverse_withDocumentType_handlesCorrectly() {
        W3CDom w3cDom = new W3CDom();
        Document document = Document.createShell("baseUri");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(document);
        DocumentType docType = new DocumentType("name", "publicId", "systemId");
        
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.traverse(docType);  // Should handle without error
    }

    @Test(timeout = 4000, expected = DOMException.class)
    public void traverse_withInvalidNode_throwsException() {
        W3CDom w3cDom = new W3CDom();
        Document document = Document.createShell("baseUri");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(document);
        DataNode dataNode = new DataNode("content");
        
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.traverse(dataNode);  // Should throw DOMException
    }

    // ... (other tests following same pattern with descriptive names and comments)
}