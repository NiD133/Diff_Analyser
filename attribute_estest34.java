package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link Attribute} class.
 */
public class AttributeTest {

    @Test
    public void namespaceShouldBeEmptyForAttributeWithoutPrefix() {
        // Arrange: Create a standard attribute without a namespace prefix (e.g., "key" instead of "ns:key").
        // The parent Attributes object is not relevant for testing the namespace() method's logic.
        Attribute attribute = new Attribute("AttributeName", "someValue", null);

        // Act: Retrieve the namespace from the attribute.
        String namespace = attribute.namespace();

        // Assert: The namespace should be an empty string, as specified in the method's documentation
        // for attributes that do not have a prefix.
        assertEquals("", namespace);
    }
}