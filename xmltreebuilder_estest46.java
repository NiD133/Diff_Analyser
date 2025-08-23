package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link XmlTreeBuilder}.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that the newInstance() method creates a new builder
     * that has the correct default XML namespace.
     */
    @Test
    public void newInstanceCreatesBuilderWithCorrectDefaultNamespace() {
        // Arrange: An initial builder is required to create a new instance.
        XmlTreeBuilder originalBuilder = new XmlTreeBuilder();
        String expectedNamespace = "http://www.w3.org/XML/1998/namespace";

        // Act: Create a new builder instance.
        XmlTreeBuilder newBuilder = originalBuilder.newInstance();

        // Assert: The new instance should have the correct default namespace.
        assertEquals(expectedNamespace, newBuilder.defaultNamespace());
    }
}