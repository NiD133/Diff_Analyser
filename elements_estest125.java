package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements} class.
 */
// The original class name "Elements_ESTestTest125" and scaffolding are preserved
// to match the input, but would typically be simplified to "ElementsTest".
public class Elements_ESTestTest125 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that calling append() with an empty HTML string does not modify the
     * Elements collection and maintains its original state.
     */
    @Test
    public void appendWithEmptyHtmlStringShouldNotChangeElements() {
        // Arrange: Create a document and select a non-empty list of elements.
        Document doc = Jsoup.parse("<div></div><p></p>");
        Elements elements = doc.select("div, p");
        int initialSize = elements.size();

        // A pre-condition check to ensure our setup is correct.
        assertEquals("Test setup should find two elements.", 2, initialSize);

        // Act: Call the method under test with an empty string.
        Elements result = elements.append("");

        // Assert: Verify the collection remains unchanged and the method supports chaining.
        assertSame("The append() method should return the same instance for chaining.", elements, result);
        assertEquals("The number of elements should not change after appending an empty string.", initialSize, result.size());
        assertFalse("The elements collection should remain non-empty.", result.isEmpty());
    }
}