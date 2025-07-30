package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class NodeIterator_ESTest extends NodeIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testNodeHasParentAfterIteration() throws Throwable {
        Document document = Parser.parseBodyFragment("org.jsouphnodes.NodeIterator", "org.jsouphnodes.NodeIterator");
        NodeIterator<Node> nodeIterator = NodeIterator.from(document);
        nodeIterator.next(); // Move to the first node
        Node node = nodeIterator.next(); // Move to the second node
        assertTrue(node.hasParent());
    }

    @Test(timeout = 4000)
    public void testNodeIteratorHasNext() throws Throwable {
        Document document = Parser.parseBodyFragment("org.jsoup.nodes.NodeIterator", "org.jsoup.nodes.NodeIterator");
        NodeIterator<Node> nodeIterator = NodeIterator.from(document);
        assertTrue(nodeIterator.hasNext());
    }

    @Test(timeout = 4000)
    public void testEmptyDocumentHasNoNextFormElement() throws Throwable {
        Document document = Parser.parseBodyFragment("", "");
        NodeIterator<FormElement> nodeIterator = new NodeIterator<>(document, FormElement.class);
        assertFalse(nodeIterator.hasNext());
    }

    @Test(timeout = 4000)
    public void testRemoveOnEmptyNodeIteratorThrowsException() throws Throwable {
        Document document = new Document("org.jsoup.nodes.NodeIterator", "");
        Element element = document.doClone(document);
        NodeIterator<FormElement> nodeIterator = new NodeIterator<>(element, FormElement.class);
        try {
            nodeIterator.remove();
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.ArrayList", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextOnEmptyNodeIteratorThrowsException() throws Throwable {
        Document document = Parser.parseBodyFragment("   ", "   ");
        Element element = document.prependChild(document);
        NodeIterator<FormElement> nodeIterator = new NodeIterator<>(element, FormElement.class);
        try {
            nodeIterator.next();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.jsoup.nodes.NodeIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextOnNullChildNodesThrowsException() throws Throwable {
        Document document = new Document("", null);
        document.childNodes = null;
        NodeIterator<FormElement> nodeIterator = new NodeIterator<>(document, FormElement.class);
        try {
            nodeIterator.next();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testFromWithNullNodeThrowsException() throws Throwable {
        try {
            NodeIterator.from(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testFromWithInvalidNodeThrowsException() throws Throwable {
        Document document = Document.createShell("org.jsoup.nodes.NodeIterator");
        CDataNode cDataNode = new CDataNode("org.jsoup.nodes.NodeIterator");
        Element element = document.doClone(cDataNode);
        try {
            NodeIterator.from(element);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullNodeThrowsException() throws Throwable {
        try {
            new NodeIterator<>(null, FormElement.class);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithInvalidNodeThrowsException() throws Throwable {
        Document document = Document.createShell("et\"yoy;tZU`>?U[");
        CDataNode cDataNode = new CDataNode("et\"yoy;tZU`>?U[");
        Element element = document.doClone(cDataNode);
        document.parentNode = element;
        try {
            new NodeIterator<>(document.parentNode, FormElement.class);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testRestartNodeIterator() throws Throwable {
        Document document = Parser.parseBodyFragment("", "");
        NodeIterator<Node> nodeIterator = NodeIterator.from(document);
        nodeIterator.restart(document);
        assertFalse(document.isBlock());
    }

    @Test(timeout = 4000)
    public void testRestartWithNullNodeThrowsException() throws Throwable {
        Document document = Parser.parse("http://www.w3.org/1999/xhtml", "http://www.w3.org/2000/svg");
        NodeIterator<FormElement> nodeIterator = new NodeIterator<>(document, FormElement.class);
        try {
            nodeIterator.restart(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.nodes.NodeIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testWholeTextOnDocumentWithElement() throws Throwable {
        Document document = Document.createShell("org.jsoup.>odes.NodeIterator");
        document.appendElement("org.jsoup.>odes.NodeIterator");
        String text = document.wholeText();
        assertEquals("", text);
    }

    @Test(timeout = 4000)
    public void testWholeTextOnEmptyDocument() throws Throwable {
        Document document = new Document("org.jsoup.>odes.NodeIterator", "org.jsoup.>odes.NodeIterator");
        String text = document.wholeText();
        assertEquals("", text);
    }

    @Test(timeout = 4000)
    public void testNextAfterRemoveThrowsException() throws Throwable {
        Document document = Parser.parseBodyFragment("org.jsoup.nodes.NodeIterator", "org.jsoup.nodes.NodeIterator");
        Element element = document.doClone(document);
        NodeIterator<FormElement> nodeIterator = new NodeIterator<>(element, FormElement.class);
        nodeIterator.remove();
        try {
            nodeIterator.next();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.jsoup.nodes.NodeIterator", e);
        }
    }
}