package org.apache.ibatis.parsing;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Locale;
import java.util.Properties;

import static org.junit.Assert.assertSame;

/**
 * A test case for the {@link XNode} class, focusing on attribute parsing.
 */
public class XNodeTest {

    /**
     * Verifies that {@link XNode#getEnumAttribute(Class, String, Enum)} returns the
     * provided default value when the specified attribute is not found on the XML node.
     */
    @Test
    public void getEnumAttributeShouldReturnDefaultValueWhenAttributeIsMissing() {
        // Arrange: Set up the test objects and state.

        // 1. Create a DOM Node that has no attributes.
        Node nodeWithoutAttributes = new IIOMetadataNode("configuration");

        // 2. The XPathParser is a required dependency for XNode. Its internal state is
        //    not relevant for this test, so we can initialize it with a null document.
        XPathParser xPathParser = new XPathParser((Document) null, false);

        // 3. Instantiate the XNode to be tested.
        XNode xNode = new XNode(xPathParser, nodeWithoutAttributes, new Properties());

        // 4. Define the parameters for the method call: a non-existent attribute
        //    name and a default value to fall back on.
        String nonExistentAttribute = "countryCode";
        Locale.IsoCountryCode defaultValue = Locale.IsoCountryCode.PART1_ALPHA3;

        // Act: Call the method under test.
        Locale.IsoCountryCode actualValue = xNode.getEnumAttribute(
            Locale.IsoCountryCode.class,
            nonExistentAttribute,
            defaultValue
        );

        // Assert: Verify the outcome.
        assertSame("Expected the default enum value when the attribute is missing.", defaultValue, actualValue);
    }
}