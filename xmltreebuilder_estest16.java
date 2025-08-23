package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the {@link XmlTreeBuilder} class.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that the defaultTagSet() method returns a non-null TagSet instance.
     * This ensures the builder is initialized with a valid set of tag definitions.
     */
    @Test
    public void defaultTagSetReturnsNonNull() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // Act
        TagSet tagSet = xmlTreeBuilder.defaultTagSet();

        // Assert
        assertNotNull("The default tag set should not be null.", tagSet);
    }
}