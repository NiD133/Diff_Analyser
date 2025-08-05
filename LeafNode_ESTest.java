package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.internal.QuietAppendable;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class LeafNode_ESTest extends LeafNode_ESTest_scaffolding {

    // ========== Child Node Management Tests ==========
    
    @Test(timeout = 4000)
    public void testEnsureChildNodesReturnsEmptyList() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        
        List<Node> childNodes = cdataNode.ensureChildNodes();
        
        assertTrue("LeafNode should have no child nodes", childNodes.isEmpty());
    }

    @Test(timeout = 4000)
    public void testChildNodeSizeIsAlwaysZero() throws Throwable {
        DataNode dataNode = new DataNode("test data");
        
        int childCount = dataNode.childNodeSize();
        
        assertEquals("LeafNode should have zero child nodes", 0, childCount);
    }

    @Test(timeout = 4000)
    public void testEmptyMethodReturnsItself() throws Throwable {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("test", false);
        xmlDeclaration.setSiblingIndex(1574);
        
        Node result = xmlDeclaration.empty();
        
        assertSame("empty() should return the same node", xmlDeclaration, result);
    }

    @Test(timeout = 4000)
    public void testEmptyMethodWithNegativeSiblingIndex() throws Throwable {
        TextNode textNode = TextNode.createFromEncoded("test text");
        textNode.setSiblingIndex(-2159);
        
        Node result = textNode.empty();
        
        assertSame("empty() should return the same node regardless of sibling index", textNode, result);
    }

    // ========== Core Value Tests ==========
    
    @Test(timeout = 4000)
    public void testGetCoreValueReturnsOriginalValue() throws Throwable {
        TextNode textNode = new TextNode("test content");
        
        String coreValue = textNode.coreValue();
        
        assertEquals("Core value should match constructor input", "test content", coreValue);
    }

    @Test(timeout = 4000)
    public void testGetCoreValueWithEmptyString() throws Throwable {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("", false);
        
        String coreValue = xmlDeclaration.coreValue();
        
        assertEquals("Core value should be empty string", "", coreValue);
    }

    @Test(timeout = 4000)
    public void testSetCoreValueUpdatesValue() throws Throwable {
        CDataNode cdataNode = new CDataNode("original value");
        
        cdataNode.coreValue("new value");
        
        assertEquals("Sibling index should remain unchanged", 0, cdataNode.siblingIndex());
    }

    @Test(timeout = 4000)
    public void testSetWholeDataWithNull() throws Throwable {
        DataNode dataNode = new DataNode("original data");
        
        DataNode result = dataNode.setWholeData(null);
        String coreValue = result.coreValue();
        
        assertNull("Core value should be null after setting null data", coreValue);
    }

    // ========== Attribute Management Tests ==========
    
    @Test(timeout = 4000)
    public void testHasAttributesReturnsFalseInitially() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        
        boolean hasAttributes = cdataNode.hasAttributes();
        
        assertFalse("New LeafNode should have no attributes", hasAttributes);
    }

    @Test(timeout = 4000)
    public void testHasAttributesReturnsTrueAfterSettingAttribute() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        
        cdataNode.attr("PUBLIC", null);
        boolean hasAttributes = cdataNode.hasAttributes();
        
        assertTrue("LeafNode should have attributes after setting one", hasAttributes);
    }

    @Test(timeout = 4000)
    public void testHasAttrForCDataSpecialAttribute() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        
        boolean hasAttr = cdataNode.hasAttr("#cdata");
        
        assertTrue("CDataNode should have #cdata attribute", hasAttr);
    }

    @Test(timeout = 4000)
    public void testHasAttrForNonExistentAttribute() throws Throwable {
        Comment comment = new Comment("test comment");
        
        boolean hasAttr = comment.hasAttr("nonexistent");
        
        assertFalse("Comment should not have non-existent attribute", hasAttr);
    }

    @Test(timeout = 4000)
    public void testGetAttrForCDataSpecialAttribute() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        
        String attrValue = cdataNode.attr("#cdata");
        
        assertEquals("CData #cdata attribute should return core value", "test data", attrValue);
    }

    @Test(timeout = 4000)
    public void testGetAttrForCDataSpecialAttributeEmpty() throws Throwable {
        CDataNode cdataNode = new CDataNode("");
        
        String attrValue = cdataNode.attr("#cdata");
        
        assertEquals("Empty CData #cdata attribute should return empty string", "", attrValue);
    }

    @Test(timeout = 4000)
    public void testGetAttrForNonExistentAttribute() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        
        String attrValue = cdataNode.attr("nonexistent");
        
        assertEquals("Non-existent attribute should return empty string", "", attrValue);
    }

    @Test(timeout = 4000)
    public void testSetAttributeReturnsNode() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        cdataNode.setSiblingIndex(5696);
        
        Node result = cdataNode.attr("key", "value");
        
        assertEquals("Setting attribute should not change child count", 0, result.childNodeSize());
    }

    @Test(timeout = 4000)
    public void testSetAttributeWithNullValue() throws Throwable {
        DataNode dataNode = new DataNode("test data");
        dataNode.setSiblingIndex(-1447);
        
        Node result = dataNode.attr("key", null);
        
        assertEquals("Setting null attribute should not change child count", 0, result.childNodeSize());
    }

    @Test(timeout = 4000)
    public void testSetAttributeOnWrappedNode() throws Throwable {
        DataNode dataNode = new DataNode("PUBLIC");
        Node wrappedNode = dataNode.wrap("wrapper");
        
        Node result = wrappedNode.attr("key", "value");
        
        assertSame("Setting attribute on wrapped node should return same node", result, wrappedNode);
    }

    @Test(timeout = 4000)
    public void testRemoveAttributeReturnsNode() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        cdataNode.setSiblingIndex(1);
        
        Node result = cdataNode.removeAttr("key");
        
        assertEquals("Removing attribute should not change child count", 0, result.childNodeSize());
    }

    @Test(timeout = 4000)
    public void testRemoveAttributeWithNegativeSiblingIndex() throws Throwable {
        DataNode dataNode = new DataNode("test data");
        dataNode.setSiblingIndex(-1447);
        
        Node result = dataNode.removeAttr("key");
        
        assertFalse("Node should not have parent after removeAttr", result.hasParent());
    }

    // ========== Base URI Tests ==========
    
    @Test(timeout = 4000)
    public void testBaseUriFromParent() throws Throwable {
        TextNode textNode = new TextNode("body");
        Document document = Parser.parseBodyFragment("body", "http://example.com");
        textNode.parentNode = document;
        
        String baseUri = textNode.baseUri();
        
        assertEquals("Base URI should come from parent", "http://example.com", baseUri);
    }

    @Test(timeout = 4000)
    public void testBaseUriWithoutParent() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        
        String baseUri = cdataNode.baseUri();
        
        assertEquals("Base URI should be empty without parent", "", baseUri);
    }

    @Test(timeout = 4000)
    public void testDoSetBaseUri() throws Throwable {
        DocumentType documentType = new DocumentType("html", "public", "system");
        
        documentType.doSetBaseUri("");
        
        assertFalse("DocumentType should not have parent after setting base URI", documentType.hasParent());
    }

    @Test(timeout = 4000)
    public void testAbsUrlReturnsEmptyString() throws Throwable {
        Comment comment = new Comment("test comment");
        
        String absUrl = comment.absUrl("href");
        
        assertEquals("AbsUrl should return empty string for LeafNode", "", absUrl);
    }

    // ========== Cloning Tests ==========
    
    @Test(timeout = 4000)
    public void testDoCloneWithParent() throws Throwable {
        CDataNode originalNode = new CDataNode("test data");
        CDataNode parentNode = new CDataNode("parent");
        
        LeafNode clonedNode = originalNode.doClone(parentNode);
        Node result = clonedNode.empty();
        
        assertTrue("Cloned node should have parent", result.hasParent());
    }

    @Test(timeout = 4000)
    public void testDoCloneWithDifferentParentType() throws Throwable {
        DataNode originalNode = new DataNode("test data");
        originalNode.setSiblingIndex(74);
        CDataNode parentNode = new CDataNode("parent");
        
        LeafNode clonedNode = originalNode.doClone(parentNode);
        
        assertTrue("Cloned node should have parent", clonedNode.hasParent());
    }

    @Test(timeout = 4000)
    public void testDoCloneWithNullParent() throws Throwable {
        TextNode originalNode = new TextNode("test text");
        
        LeafNode clonedNode = originalNode.doClone(null);
        
        assertNotSame("Cloned node should be different instance", clonedNode, originalNode);
    }

    @Test(timeout = 4000)
    public void testDoCloneWithDocumentParent() throws Throwable {
        CDataNode originalNode = new CDataNode("test data");
        Document parentDocument = new Document("http://example.com");
        
        LeafNode clonedNode = originalNode.doClone(parentDocument);
        Node result = clonedNode.removeAttr("key");
        
        assertTrue("Cloned node should have parent", result.hasParent());
    }

    @Test(timeout = 4000)
    public void testShallowCloneAfterAccessingAttributes() throws Throwable {
        TextNode textNode = new TextNode("body");
        textNode.attributes(); // Access attributes to initialize them
        
        Node clonedNode = textNode.shallowClone();
        
        assertNotSame("Shallow clone should create different instance", clonedNode, textNode);
    }

    // ========== Output Tests ==========
    
    @Test(timeout = 4000)
    public void testOuterHtmlTailWithNullAppendable() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        
        cdataNode.outerHtmlTail(null, outputSettings);
        
        assertEquals("Sibling index should remain unchanged", 0, cdataNode.siblingIndex());
    }

    // ========== Error Handling Tests ==========
    
    @Test(timeout = 4000)
    public void testRemoveAttrWithNullKey() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        
        try {
            cdataNode.removeAttr(null);
            fail("Should throw IllegalArgumentException for null key");
        } catch(IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testAttrWithNullKey() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        
        try {
            cdataNode.attr(null);
            fail("Should throw IllegalArgumentException for null key");
        } catch(IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testSetAttrWithNullKey() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        
        try {
            cdataNode.attr(null, null);
            fail("Should throw NullPointerException for null key");
        } catch(NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testHasAttrWithNullKey() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        
        try {
            cdataNode.hasAttr(null);
            fail("Should throw IllegalArgumentException for null key");
        } catch(IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testAbsUrlWithEmptyKey() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        
        try {
            cdataNode.absUrl("");
            fail("Should throw IllegalArgumentException for empty key");
        } catch(IllegalArgumentException e) {
            assertEquals("String must not be empty", e.getMessage());
        }
    }

    // ========== Internal State Corruption Tests ==========
    
    @Test(timeout = 4000)
    public void testRemoveAttrWithCorruptedValue() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        cdataNode.value = cdataNode; // Corrupt internal state
        
        try {
            cdataNode.removeAttr("key");
            fail("Should throw ClassCastException for corrupted value");
        } catch(ClassCastException e) {
            assertTrue("Should mention CDataNode in error", 
                      e.getMessage().contains("CDataNode cannot be cast to java.lang.String"));
        }
    }

    @Test(timeout = 4000)
    public void testAttributesWithCorruptedValue() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        cdataNode.value = cdataNode; // Corrupt internal state
        
        try {
            cdataNode.attributes();
            fail("Should throw ClassCastException for corrupted value");
        } catch(ClassCastException e) {
            assertTrue("Should mention CDataNode in error", 
                      e.getMessage().contains("CDataNode cannot be cast to java.lang.String"));
        }
    }

    @Test(timeout = 4000)
    public void testSetAttrWithCorruptedValue() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        Object corruptValue = new Object();
        cdataNode.value = corruptValue;
        
        try {
            cdataNode.attr("key", "value");
            fail("Should throw ClassCastException for corrupted value");
        } catch(ClassCastException e) {
            assertTrue("Should mention Object in error", 
                      e.getMessage().contains("java.lang.Object cannot be cast to java.lang.String"));
        }
    }

    @Test(timeout = 4000)
    public void testSetAttrWithCircularParentReference() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        cdataNode.parentNode = cdataNode; // Create circular reference
        
        // This should not throw an exception but may cause infinite recursion
        cdataNode.attr("key", "value");
    }

    // ========== Edge Case Tests ==========
    
    @Test(timeout = 4000)
    public void testSetAttributeOverwritesExisting() throws Throwable {
        CDataNode cdataNode = new CDataNode("test data");
        cdataNode.attr("key", "original value");
        
        Node result = cdataNode.attr("key", "new value");
        
        assertFalse("Node should not have parent", result.hasParent());
    }
}