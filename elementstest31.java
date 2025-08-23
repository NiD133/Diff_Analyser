package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the {@link Elements} class.
 * This specific test file focuses on the comments() method.
 */
// The original class name `ElementsTestTest31` is preserved. 
// A more conventional name would be `ElementsTest`.
public class ElementsTestTest31 {

    /**
     * Tests that the comments() method correctly extracts only the direct child
     * comment nodes from the selected elements.
     */
    @Test
    public void commentsShouldReturnDirectChildCommentsOfSelectedElements() {
        // Arrange: The HTML contains a comment outside any <p>, one inside the first <p>,
        // and one inside the second <p>. The `comments()` method should only find
        // comments that are direct children of the selected elements.
        String html = "<!-- comment1 --><p><!-- comment2 --><p class=two><!-- comment3 -->";
        Document doc = Jsoup.parse(html);

        // --- Scenario 1: Select all <p> elements ---
        
        // Act: Select all <p> elements and get their direct child comments.
        Elements allPElements = doc.select("p");
        List<Comment> allPComments = allPElements.comments();

        // Assert: Should find comments 2 and 3, but not comment 1, which is not a child of a <p>.
        List<String> allPCommentData = allPComments.stream()
            .map(Comment::getData)
            .collect(Collectors.toList());
        assertEquals(List.of(" comment2 ", " comment3 "), allPCommentData);

        // --- Scenario 2: Select a specific <p> element ---
        
        // Act: Select only the <p> with class "two" and get its comments.
        Elements specificPElement = doc.select("p.two");
        List<Comment> specificPComments = specificPElement.comments();

        // Assert: Should find only comment 3, which is the direct child of the selected element.
        List<String> specificPCommentData = specificPComments.stream()
            .map(Comment::getData)
            .collect(Collectors.toList());
        assertEquals(List.of(" comment3 "), specificPCommentData);
    }
}