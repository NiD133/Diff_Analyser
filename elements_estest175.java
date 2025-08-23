package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.ConcurrentModificationException;

/**
 * This test class contains tests for the {@link Elements} class.
 * Note: The original class name was Elements_ESTestTest175.
 */
public class Elements_ESTestTest175 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that calling removeAll() on an Elements collection with itself as the
     * argument throws a ConcurrentModificationException. This is the expected behavior
     * for Java collections, as the collection is being modified while it is being iterated over.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void removeAllWithSelfThrowsConcurrentModificationException() {
        // Arrange: Create a document and get a list of its elements.
        Document document = Document.createShell("");
        Elements elements = document.getAllElements();

        // Act: Attempt to remove all elements from the collection by passing the collection to itself.
        // This action is expected to throw a ConcurrentModificationException.
        elements.removeAll(elements);
    }
}