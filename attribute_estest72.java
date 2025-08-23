package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that calling setKey() on an Attribute that has a parent Attributes collection
     * correctly updates the key within that parent collection.
     */
    @Test
    public void setKeyOnAttributeWithParentAlsoUpdatesKeyInParent() {
        // Arrange
        // 1. Create a parent Attributes collection and add an initial attribute.
        Attributes parentAttributes = new Attributes();
        final String initialKey = "id";
        final String valueInParent = "123";
        parentAttributes.put(initialKey, valueInParent);

        // 2. Create an Attribute instance that is linked to the parent Attributes.
        //    Note: The value of this standalone Attribute object is distinct from the value in the parent.
        Attribute attribute = new Attribute(initialKey, "some-other-value", parentAttributes);
        
        // 3. Verify the initial state before the action.
        assertTrue("Parent should initially have the key.", parentAttributes.hasKey(initialKey));
        assertEquals("Parent should have the correct initial value.", valueInParent, parentAttributes.get(initialKey));

        // Act
        // Change the key of the Attribute. This should trigger an update in the parent.
        final String newKey = "name";
        attribute.setKey(newKey);

        // Assert
        // 1. The key of the Attribute object itself is updated.
        assertEquals("The attribute's own key should be updated.", newKey, attribute.getKey());

        // 2. The parent Attributes collection is correctly modified.
        assertFalse("The old key should be removed from the parent.", parentAttributes.hasKey(initialKey));
        assertTrue("The new key should be present in the parent.", parentAttributes.hasKey(newKey));
        
        // 3. The value associated with the new key in the parent should be the original value from the parent.
        assertEquals("The value in the parent should be preserved under the new key.", valueInParent, parentAttributes.get(newKey));
    }
}