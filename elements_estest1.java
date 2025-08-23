package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements} class.
 */
public class Elements_ESTestTest1 {

    /**
     * Tests that calling the eq() method with an index that is out of the list's bounds
     * returns an empty Elements collection, rather than throwing an exception or returning null.
     */
    @Test
    public void eqWithOutOfBoundsIndexReturnsEmptyElements() {
        // Arrange: Create a document and select its elements.
        // A default shell document contains <html>, <head>, and <body> tags.
        Document doc = Document.createShell("");
        Elements elements = doc.getAllElements();
        assertEquals("A shell document should contain 3 elements", 3, elements.size());

        // Act: Call eq() with an index equal to the list size, which is out of bounds.
        int outOfBoundsIndex = elements.size();
        Elements result = elements.eq(outOfBoundsIndex);

        // Assert: The resulting Elements collection should be empty.
        assertNotNull("The result of eq() should never be null.", result);
        assertTrue("eq() with an out-of-bounds index should return an empty Elements object.", result.isEmpty());
    }
}