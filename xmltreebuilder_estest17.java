package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link XmlTreeBuilder} class.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that the default ParseSettings for the XmlTreeBuilder are configured
     * to preserve the case of tags, which is the expected behavior for XML parsing.
     */
    @Test
    public void defaultSettingsShouldPreserveTagCase() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // Act
        ParseSettings settings = xmlTreeBuilder.defaultSettings();

        // Assert
        assertTrue("XML parser should preserve tag case by default", settings.preserveTagCase());
    }
}