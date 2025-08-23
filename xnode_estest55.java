package org.apache.ibatis.parsing;

import java.util.Locale;
import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Test suite for the {@link XNode} class.
 */
public class XNodeTest {

    /**
     * Verifies that calling getEnumAttribute with a null attribute name
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void getEnumAttributeShouldThrowNPEWhenNameIsNull() {
        // Arrange: Create an XNode instance. An empty node and properties are sufficient
        // for this test, and the XPathParser can be null as it's not used by the
        // method under test.
        Node node = new IIOMetadataNode();
        Properties variables = new Properties();
        XNode xNode = new XNode(null, node, variables);
        Class<Locale.Category> anyEnumType = Locale.Category.class;

        // Act: Attempt to get an enum attribute using a null name.
        xNode.getEnumAttribute(anyEnumType, null);

        // Assert: The test expects a NullPointerException, which is handled
        // by the @Test(expected) annotation. No further assertion is needed.
    }
}