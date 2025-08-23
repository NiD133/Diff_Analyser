package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Test suite for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that the {@link Attribute#clone()} method creates a new Attribute instance
     * that is equal to the original but is not the same object reference.
     */
    @Test
    public void cloneCreatesEqualButNotSameInstance() {
        // Arrange: Create an original attribute with a common key and value.
        Attribute original = new Attribute("href", "/index.html");

        // Act: Clone the attribute.
        Attribute cloned = original.clone();

        // Assert: The clone should be a different object instance but equal in value.
        // The equals() method in Attribute compares the key and value.
        assertNotSame("A cloned attribute should be a new object instance.", original, cloned);
        assertEquals("A cloned attribute should be equal to the original.", original, cloned);
    }
}