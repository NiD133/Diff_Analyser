package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.NoSuchElementException;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.NodeIterator;
import org.jsoup.parser.Parser;

/**
 * Test suite for NodeIterator functionality.
 * Tests iterator behavior for traversing DOM nodes with type filtering.
 */
public class NodeIteratorTest {

    @Test
    public void shouldIterateOverDocumentNodes() {
        // Given: A document with some content
        Document document = Parser.parseBodyFragment("org.jsoup.nodes.NodeIterator", "org.jsoup.nodes.NodeIterator");
        NodeIterator<Node> iterator = NodeIterator.from(document);
        
        // When: We iterate through the first two nodes
        iterator.next(); // First node
        Node secondNode = iterator.next(); // Second node
        
        // Then: The second node should have a parent
        assertTrue("Second node should have a parent", secondNode.hasParent());
    }

    @Test
    public void shouldReturnTrueWhenDocumentHasNodes() {
        // Given: A document with content
        Document document = Parser.parseBodyFragment("org.jsoup.nodes.NodeIterator", "org.jsoup.nodes.NodeIterator");
        NodeIterator<Node> iterator = NodeIterator.from(document);
        
        // When: We check if there are more nodes
        boolean hasNext = iterator.hasNext();
        
        // Then: It should return true
        assertTrue("Document with content should have nodes to iterate", hasNext);
    }

    @Test
    public void shouldReturnFalseWhenNoMatchingNodesExist() {
        // Given: An empty document and iterator looking for FormElements
        Document emptyDocument = Parser.parseBodyFragment("", "");
        NodeIterator<FormElement> formIterator = new NodeIterator<>(emptyDocument, FormElement.class);
        
        // When: We check if there are FormElements
        boolean hasNext = formIterator.hasNext();
        
        // Then: It should return false (no FormElements in empty document)
        assertFalse("Empty document should not have FormElements", hasNext);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowExceptionWhenRemovingFromEmptyIterator() {
        // Given: An empty document and FormElement iterator
        Document document = new Document("org.jsoup.nodes.NodeIterator", "");
        Element clonedElement = document.doClone(document);
        NodeIterator<FormElement> iterator = new NodeIterator<>(clonedElement, FormElement.class);
        
        // When: We try to remove without iterating
        // Then: Should throw IndexOutOfBoundsException
        iterator.remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenIteratingEmptyFormElementCollection() {
        // Given: A document with whitespace content (no FormElements)
        Document document = Parser.parseBodyFragment("   ", "   ");
        Element elementWithSelfAsChild = document.prependChild(document);
        NodeIterator<FormElement> iterator = new NodeIterator<>(elementWithSelfAsChild, FormElement.class);
        
        // When: We try to get next FormElement
        // Then: Should throw NoSuchElementException
        iterator.next();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenChildNodesIsNull() {
        // Given: A document with null childNodes (corrupted state)
        Document document = new Document("", null);
        document.childNodes = null; // Simulate corrupted state
        NodeIterator<FormElement> iterator = new NodeIterator<>(document, FormElement.class);
        
        // When: We try to iterate
        // Then: Should throw NullPointerException
        iterator.next();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenCreatingIteratorFromNullNode() {
        // When: We try to create iterator from null node
        // Then: Should throw IllegalArgumentException
        NodeIterator.from(null);
    }

    @Test(expected = ClassCastException.class)
    public void shouldThrowExceptionWhenCreatingIteratorFromIncompatibleNode() {
        // Given: A document with CDataNode that's incompatible with expected structure
        Document document = Document.createShell("org.jsoup.nodes.NodeIterator");
        CDataNode cDataNode = new CDataNode("org.jsoup.nodes.NodeIterator");
        Element incompatibleElement = document.doClone(cDataNode);
        
        // When: We try to create iterator from incompatible element
        // Then: Should throw ClassCastException
        NodeIterator.from(incompatibleElement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConstructorReceivesNullNode() {
        // When: We try to create iterator with null node
        // Then: Should throw IllegalArgumentException
        new NodeIterator<>(null, FormElement.class);
    }

    @Test(expected = ClassCastException.class)
    public void shouldThrowExceptionWhenParentNodeIsIncompatible() {
        // Given: A document with incompatible parent node structure
        Document document = Document.createShell("et\"yoy;tZU`>?U[");
        CDataNode cDataNode = new CDataNode("et\"yoy;tZU`>?U[");
        Element incompatibleElement = document.doClone(cDataNode);
        document.parentNode = incompatibleElement; // Set incompatible parent
        
        // When: We try to create iterator from node with incompatible parent
        // Then: Should throw ClassCastException
        new NodeIterator<>(document.parentNode, FormElement.class);
    }

    @Test
    public void shouldRestartIteratorFromNewNode() {
        // Given: An empty document and iterator
        Document emptyDocument = Parser.parseBodyFragment("", "");
        NodeIterator<Node> iterator = NodeIterator.from(emptyDocument);
        
        // When: We restart the iterator with the same document
        iterator.restart(emptyDocument);
        
        // Then: The document should maintain its properties
        assertFalse("Document should not be a block element", emptyDocument.isBlock());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenRestartingWithNullNode() {
        // Given: A valid iterator
        Document document = Parser.parse("http://www.w3.org/1999/xhtml", "http://www.w3.org/2000/svg");
        NodeIterator<FormElement> iterator = new NodeIterator<>(document, FormElement.class);
        
        // When: We try to restart with null
        // Then: Should throw NullPointerException
        iterator.restart(null);
    }

    @Test
    public void shouldReturnEmptyTextForDocumentWithElements() {
        // Given: A document with an appended element
        Document document = Document.createShell("org.jsoup.>odes.NodeIterator");
        document.appendElement("org.jsoup.>odes.NodeIterator");
        
        // When: We get the whole text
        String wholeText = document.wholeText();
        
        // Then: It should be empty (elements don't contribute to wholeText)
        assertEquals("Document with only elements should have empty wholeText", "", wholeText);
    }

    @Test
    public void shouldReturnEmptyTextForEmptyDocument() {
        // Given: An empty document
        Document emptyDocument = new Document("org.jsoup.>odes.NodeIterator", "org.jsoup.>odes.NodeIterator");
        
        // When: We get the whole text
        String wholeText = emptyDocument.wholeText();
        
        // Then: It should be empty
        assertEquals("Empty document should have empty wholeText", "", wholeText);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenIteratingAfterRemoval() {
        // Given: A document with content and FormElement iterator
        Document document = Parser.parseBodyFragment("org.jsoup.nodes.NodeIterator", "org.jsoup.nodes.NodeIterator");
        Element clonedElement = document.doClone(document);
        NodeIterator<FormElement> iterator = new NodeIterator<>(clonedElement, FormElement.class);
        
        // When: We remove an element and then try to iterate
        iterator.remove();
        
        // Then: Should throw NoSuchElementException
        iterator.next();
    }
}