package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Test suite for the equals() method in the Attribute class.
 */
public class AttributeEqualsTest {

    @Test
    public void equalsShouldReturnFalseForAttributesWithDifferentKeysAndValues() {
        // Arrange: Create two distinct attributes with different keys and values.
        // The original test used different creation methods (factory vs. constructor),
        // which is preserved here to maintain test coverage.
        Attribute attributeA = Attribute.createFromEncoded("key-A", "value-A");
        Attribute attributeB = new Attribute("key-B", "value-B");

        // Act & Assert: Verify that the two attributes are not considered equal.
        // The equals method should return false when both key and value differ.
        assertNotEquals(attributeA, attributeB);

        // Also assert the symmetric property of the equals contract.
        assertNotEquals(attributeB, attributeA);
    }
}