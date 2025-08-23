package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.nio.charset.Charset;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the constructor accepting a charset name correctly resolves
     * a known alias to its canonical charset name.
     */
    @Test
    public void constructorShouldSetCorrectCharsetWhenAliasIsProvided() {
        // Arrange
        // "l4" is a known alias for the "ISO-8859-4" charset.
        final String charsetAlias = "l4";
        final String expectedCanonicalCharsetName = "ISO-8859-4";

        // Act
        final QuotedPrintableCodec codec = new QuotedPrintableCodec(charsetAlias);
        final Charset actualCharset = codec.getCharset();

        // Assert
        assertEquals("The codec should resolve the charset alias to its canonical name.",
                     expectedCanonicalCharsetName, actualCharset.name());
    }
}