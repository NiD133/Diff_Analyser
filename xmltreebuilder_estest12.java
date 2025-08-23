package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the XmlTreeBuilder.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class XmlTreeBuilder_ESTestTest12 extends XmlTreeBuilder_ESTest_scaffolding {

    /**
     * Verifies that the defaultNamespace() method in XmlTreeBuilder consistently returns
     * the standard XML namespace URI ("http://www.w3.org/XML/1998/namespace").
     */
    @Test(timeout = 4000)
    public void defaultNamespaceReturnsStandardXmlNamespace() {
        // Arrange: Create an instance of the XmlTreeBuilder.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String expectedNamespace = "http://www.w3.org/XML/1998/namespace";

        // Act: Get the default namespace from the builder.
        String actualNamespace = xmlTreeBuilder.defaultNamespace();

        // Assert: Verify that the returned namespace matches the expected constant.
        assertEquals("The default namespace for the XML tree builder should be the standard XML namespace.",
                expectedNamespace, actualNamespace);
    }
}