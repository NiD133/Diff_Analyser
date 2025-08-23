package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link Elements} class.
 */
public class ElementsTest {

    @Test
    public void beforeShouldThrowExceptionWhenAnElementHasNoParent() {
        // Arrange
        // Create a document. The getAllElements() method returns a list that includes
        // the root Document node itself. A root node has no parent.
        Document doc = new Document("");
        Elements elementsContainingRoot = doc.getAllElements();
        String htmlToInsert = "<p>Some new HTML</p>";

        // Act & Assert
        try {
            // The before() method inserts HTML before each element in the collection.
            // This operation is invalid for a node without a parent, like the document root.
            elementsContainingRoot.before(htmlToInsert);
            fail("Expected an IllegalArgumentException because the root node has no parent.");
        } catch (IllegalArgumentException e) {
            // The underlying implementation validates that an element's parent is not null
            // before inserting a sibling.
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}