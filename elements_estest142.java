package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.junit.Test;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Elements} class.
 */
public class Elements_ESTestTest142 { // Note: Class name kept from original for consistency.

    /**
     * Verifies that `eachAttr` correctly retrieves the attribute values from all elements in the collection
     * after the attribute has been set using `attr(key, value)`.
     */
    @Test
    public void eachAttrRetrievesAttributeValuesFromAllElements() {
        // Arrange
        // A shell document contains <html>, <head>, <title>, and <body> elements.
        Document doc = Document.createShell("");
        Elements elements = doc.getAllElements();
        int elementCount = elements.size();
        assertTrue("Test setup should have multiple elements", elementCount > 1);

        String attributeKey = "data-id";
        String attributeValue = "test-value";

        // Act
        // Set the same attribute on all elements in the collection.
        elements.attr(attributeKey, attributeValue);
        // Retrieve the value of that attribute from each element.
        List<String> retrievedValues = elements.eachAttr(attributeKey);

        // Assert
        // The list of retrieved values should have one entry for each element.
        assertEquals("Should retrieve an attribute value for each element", elementCount, retrievedValues.size());

        // Verify that every retrieved value is the one we set.
        List<String> expectedValues = Collections.nCopies(elementCount, attributeValue);
        assertEquals("All retrieved attribute values should match the set value", expectedValues, retrievedValues);
    }
}