package org.jfree.chart.title;

import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ShortTextTitle} class.
 */
public class ShortTextTitleTest {

    /**
     * Tests the {@code equals()} method to ensure it correctly distinguishes
     * between different field values.
     */
    @Test
    public void testEqualsMethod() {
        ShortTextTitle title1 = new ShortTextTitle("ABC");
        ShortTextTitle title2 = new ShortTextTitle("ABC");
        
        // Initially, both titles should be equal
        assertEquals(title1, title2);

        // Change the text of title1 and verify they are no longer equal
        title1.setText("Test 1");
        assertNotEquals(title1, title2);

        // Set the text of title2 to match title1 and verify they are equal again
        title2.setText("Test 1");
        assertEquals(title1, title2);
    }

    /**
     * Tests that two equal objects have the same hash code.
     */
    @Test
    public void testHashCodeConsistency() {
        ShortTextTitle title1 = new ShortTextTitle("ABC");
        ShortTextTitle title2 = new ShortTextTitle("ABC");

        // Verify that equal objects have the same hash code
        assertEquals(title1, title2);
        assertEquals(title1.hashCode(), title2.hashCode());
    }

    /**
     * Tests that the {@code clone()} method creates a distinct but equal copy.
     */
    @Test
    public void testCloningFunctionality() throws CloneNotSupportedException {
        ShortTextTitle originalTitle = new ShortTextTitle("ABC");
        ShortTextTitle clonedTitle = CloneUtils.clone(originalTitle);

        // Verify that the cloned object is not the same instance but is equal
        assertNotSame(originalTitle, clonedTitle);
        assertEquals(originalTitle, clonedTitle);
    }

    /**
     * Tests that serialization and deserialization maintain object equality.
     */
    @Test
    public void testSerializationIntegrity() {
        ShortTextTitle originalTitle = new ShortTextTitle("ABC");
        ShortTextTitle deserializedTitle = TestUtils.serialised(originalTitle);

        // Verify that the deserialized object is equal to the original
        assertEquals(originalTitle, deserializedTitle);
    }
}