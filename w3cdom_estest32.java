package org.jsoup.helper;

import org.junit.Test;
import java.util.Map;

/**
 * Tests for the {@link W3CDom#asString(org.w3c.dom.Document, Map)} method.
 */
public class W3CDomAsStringTest {

    /**
     * Verifies that calling asString with a null W3C Document throws a NullPointerException,
     * as this is an invalid argument.
     */
    @Test(expected = NullPointerException.class)
    public void asStringWithNullDocumentThrowsNullPointerException() {
        // Arrange: Prepare a valid properties map. The content of the map is not
        // relevant for this test, as the null document should be checked first.
        Map<String, String> htmlOutputProperties = W3CDom.OutputHtml();

        // Act & Assert: Calling asString with a null document should throw the exception.
        W3CDom.asString(null, htmlOutputProperties);
    }
}