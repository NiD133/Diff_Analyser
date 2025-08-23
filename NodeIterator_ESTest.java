package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for NodeIterator.
 * Emphasizes clear Arrange-Act-Assert steps and avoids brittle/internal assumptions.
 */
public class NodeIteratorTest {

    @Test
    public void from_returnsStartNodeFirst() {
        // Arrange
        Document doc = Jsoup.parse("<p>Hello</p>");

        // Act
        NodeIterator<Node> it = NodeIterator.from(doc);
        Node first = it.next();

        // Assert
        assertSame("The first node returned should be the start node", doc, first);
    }

    @Test
    public void hasNext_trueOnNonEmptyDocument() {
        // Arrange
        Document doc = Jsoup.parse("<p>Hello</p>");

        // Act
        NodeIterator<Node> it = NodeIterator.from(doc);

        // Assert
        assertTrue("Iterator over a real document should have at least one node", it.hasNext());
    }

    @Test
    public void typedIterator_noMatches_hasNextFalse() {
        // Arrange: no <form> in this document
        Document doc = Jsoup.parse("<div><p>No forms here</p></div>");

        // Act
        NodeIterator<FormElement> it = new NodeIterator<>(doc, FormElement.class);

        // Assert
        assertFalse("Iterator should report no next when no nodes of type are present", it.hasNext());
    }

    @Test
    public void typedIterator_findsFormElements() {
        // Arrange
        Document doc = Jsoup.parse("<form id='f'><input name='a'/></form>");

        // Act
        NodeIterator<FormElement> it = new NodeIterator<>(doc, FormElement.class);

        // Assert
        assertTrue("Should find at least one form element", it.hasNext());
        FormElement form = it.next();
        assertEquals("f", form.id());
        assertFalse("Only a single form is present", it.hasNext());
    }

    @Test
    public void next_onEmptyTypedIterator_throwsNoSuchElementException() {
        // Arrange
        Document doc = Jsoup.parse("<div><p>No forms here</p></div>");
        NodeIterator<FormElement> it = new NodeIterator<>(doc, FormElement.class);

        // Act + Assert
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void from_nullStart_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> NodeIterator.from(null));
    }

    @Test
    public void constructor_nullStart_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new NodeIterator<Element>(null, Element.class));
    }

    @Test
    public void constructor_nullType_throwsIllegalArgumentException() {
        Document doc = Jsoup.parse("<p>Hello</p>");
        assertThrows(IllegalArgumentException.class, () -> new NodeIterator<Element>(doc, (Class<Element>) null));
    }

    @Test
    public void restart_resetsIterationToNewStart() {
        // Arrange
        Document doc = Jsoup.parse("<div id='a'><p id='b'>x</p></div>");
        NodeIterator<Node> it = NodeIterator.from(doc);

        // Act
        Node firstBefore = it.next(); // should be the document
        it.next(); // advance at least one more
        it.restart(doc);
        Node firstAfter = it.next();

        // Assert
        assertSame("After restart(start), the first element should again be the start node", doc, firstAfter);

        // And restarting at a different start should change the first node
        it.restart(doc.body());
        Node bodyStart = it.next();
        assertSame("Restarting with body should make body the first returned node", doc.body(), bodyStart);
    }
}