package org.jsoup.select;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertTrue;

/**
 * This class contains tests for the Elements class. The original test was auto-generated.
 * This refactored version aims for improved clarity and maintainability.
 */
public class Elements_ESTestTest159 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that the comments() method returns an empty list when called on a collection
     * of elements that do not contain any direct child comment nodes.
     */
    @Test
    public void commentsShouldReturnEmptyListWhenNoCommentsExist() {
        // Arrange: Create a document with a basic structure but no comment nodes.
        // A new Document() creates a shell with <html>, <head>, and <body> tags.
        Document document = new Document("https://example.com");
        Elements allElements = document.getAllElements();

        // Act: Call the method under test to retrieve all direct child comments.
        List<Comment> comments = allElements.comments();

        // Assert: Verify that the resulting list is empty, as expected.
        assertTrue("Expected an empty list because no comment nodes were present.", comments.isEmpty());
    }
}