package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that getKey() returns the correct key for an Attribute
     * created with the static factory method createFromEncoded().
     */
    @Test
    public void getKeyShouldReturnCorrectKeyForAttributeCreatedFromEncoded() {
        // Arrange: Define the expected key and value and create an Attribute instance.
        String expectedKey = "_Tr_2_";
        String expectedValue = "ope3n";
        Attribute attribute = Attribute.createFromEncoded(expectedKey, expectedValue);

        // Act: Call the method under test.
        String actualKey = attribute.getKey();

        // Assert: Verify that the key and value are what we expect.
        assertEquals(expectedKey, actualKey);
        assertEquals("The value should also be correctly set upon creation",
            expectedValue, attribute.getValue());
    }
}