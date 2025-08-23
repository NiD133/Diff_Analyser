package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Tests that when an Attribute has a parent Attributes collection,
     * calling setValue() returns the value from the parent, not the
     * initial value of the Attribute object itself.
     *
     * This test verifies the behavior where an Attribute's state can be
     * out of sync with its parent, and setValue() uses the parent's value
     * as the "previous" value to return.
     */
    @Test
    public void setValueOnAttributeWithParentReturnsParentsOldValue() {
        // Arrange
        // 1. Create a parent Attributes collection with an initial key-value pair.
        Attributes parentAttributes = new Attributes();
        parentAttributes.put("id", "value-in-parent");

        // 2. Create an Attribute instance linked to the parent, but with a *different* initial value.
        // This simulates a state where the child Attribute is not in sync with the parent.
        Attribute attribute = new Attribute("id", "value-in-child", parentAttributes);
        assertEquals("Pre-condition: The attribute's initial value should be what it was constructed with.",
            "value-in-child", attribute.getValue());

        // Act
        // 3. Set a new value. The setValue method is expected to return the *parent's* old value.
        String previousValue = attribute.setValue("new-value");

        // Assert
        // 4. Verify that the returned "previous" value was the one from the parent collection.
        assertEquals("setValue should return the old value from the parent Attributes.",
            "value-in-parent", previousValue);

        // 5. Verify that the attribute's own value is now updated.
        assertEquals("The attribute's value should be updated to the new value.",
            "new-value", attribute.getValue());

        // 6. Verify that the value in the parent Attributes collection is also updated.
        assertEquals("The parent Attributes collection should also be updated.",
            "new-value", parentAttributes.get("id"));
    }
}