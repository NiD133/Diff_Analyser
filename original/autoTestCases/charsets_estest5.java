package org.apache.commons.io;

import org.junit.Test;
import java.nio.charset.Charset;
import static org.junit.Assert.assertNull;

public class CharsetsTest {

    @Test
    public void testToCharset_NullName_ReturnsNull() {
        // Arrange: No charset or name is provided (both are null).
        String charsetName = null;
        Charset defaultCharset = null;

        // Act: Attempt to convert the null charset name to a Charset.
        Charset result = Charsets.toCharset(charsetName, defaultCharset);

        // Assert: The result should be null, as a null name should not produce a valid Charset.
        assertNull(result);
    }
}