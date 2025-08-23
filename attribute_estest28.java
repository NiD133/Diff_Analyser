package org.jsoup.nodes;

import org.junit.Test;

/**
 * This test class focuses on the behavior of the Attribute class.
 * This specific test case verifies the handling of null inputs in the createFromEncoded factory method.
 */
public class AttributeTest {

    /**
     * Verifies that Attribute.createFromEncoded() throws a NullPointerException
     * when the attribute key is null. The method's contract requires a non-null key.
     */
    @Test(expected = NullPointerException.class)
    public void createFromEncodedWithNullKeyThrowsNullPointerException() {
        // Act & Assert: Call the method with a null key.
        // The value argument is irrelevant as the key validation happens first.
        // The test expects a NullPointerException to be thrown, as declared in the @Test annotation.
        Attribute.createFromEncoded(null, "any-value");
    }
}