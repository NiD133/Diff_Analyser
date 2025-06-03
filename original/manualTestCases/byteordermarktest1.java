package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;

/**
 * This test case verifies that the character set names associated with the {@link ByteOrderMark}
 * constants can be successfully used to retrieve a {@link Charset} instance.  In other words,
 * it checks if the `Charset.forName()` method can recognize and load the character sets
 * specified by the {@link ByteOrderMark#getCharsetName()} method.
 */
public class ByteOrderMarkCharsetNameTest {

    /**
     * Tests that the charset names associated with various Byte Order Marks are valid
     * and can be used to obtain a corresponding Charset object.
     */
    @Test
    public void testByteOrderMarkCharsetNamesAreValid() {
        // Verify that the UTF-8 charset name can be loaded.
        assertCharsetIsLoadable(ByteOrderMark.UTF_8);

        // Verify that the UTF-16BE charset name can be loaded.
        assertCharsetIsLoadable(ByteOrderMark.UTF_16BE);

        // Verify that the UTF-16LE charset name can be loaded.
        assertCharsetIsLoadable(ByteOrderMark.UTF_16LE);

        // Verify that the UTF-32BE charset name can be loaded.
        assertCharsetIsLoadable(ByteOrderMark.UTF_32BE);

        // Verify that the UTF-32LE charset name can be loaded.
        assertCharsetIsLoadable(ByteOrderMark.UTF_32LE);
    }

    /**
     * Helper method to assert that the character set associated with a given
     * {@link ByteOrderMark} can be successfully loaded using {@link Charset#forName(String)}.
     *
     * @param bom The {@link ByteOrderMark} whose character set name is to be tested.
     */
    private void assertCharsetIsLoadable(ByteOrderMark bom) {
        String charsetName = bom.getCharsetName();
        assertNotNull(Charset.forName(charsetName),
                      "The charset '" + charsetName + "' for BOM " + bom + " should be loadable.");
    }
}