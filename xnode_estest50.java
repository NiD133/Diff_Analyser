package org.apache.ibatis.parsing;

import org.junit.Test;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.Properties;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that calling getLongAttribute with a null attribute name
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void getLongAttributeShouldThrowNullPointerExceptionWhenNameIsNull() {
        // Arrange: Create an XNode with a mock Node and empty properties.
        // The XPathParser is not needed for this attribute-related test, so it can be null.
        XNode xNode = new XNode(null, new IIOMetadataNode(), new Properties());

        // Act: Attempt to get an attribute with a null name.
        // The @Test(expected) annotation will assert that a NullPointerException is thrown.
        xNode.getLongAttribute(null);
    }
}