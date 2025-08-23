package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link XmlStreamWriter} focusing on default encoding behavior.
 */
public class XmlStreamWriterTest {

    /**
     * A string containing various characters to test Unicode support.
     */
    private static final String TEXT_UNICODE =
        "eacute: \u00E9, alpha: \u03B1, euro: \u20AC, hiragana A: \u3042";

    /**
     * Provides arguments for the parameterized test, covering various default encodings.
     *
     * @return A stream of arguments, where each argument is a default encoding name.
     *         The {@code null} case tests the writer's default fallback to UTF-8.
     */
    static Stream<Arguments> defaultEncodings() {
        return Stream.of(
            Arguments.of((String) null), // Should default to UTF-8
            Arguments.of(StandardCharsets.UTF_8.name()),
            Arguments.of(StandardCharsets.UTF_16.name()),
            Arguments.of(StandardCharsets.UTF_16BE.name()),
            Arguments.of(StandardCharsets.ISO_8859_1.name())
        );
    }

    /**
     * Creates a simple XML document string with an optional encoding declaration.
     *
     * @param text The text content for the XML element.
     * @param encoding The encoding to specify in the XML declaration. If null, no encoding is specified.
     * @return The complete XML document as a string.
     */
    private static String createXmlContent(final String text, final String encoding) {
        final String xmlDecl = encoding == null
            ? "<?xml version=\"1.0\"?>"
            : "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>";
        return xmlDecl + "\n<text>" + text + "</text>";
    }

    @ParameterizedTest(name = "Default encoding = {0}")
    @MethodSource("defaultEncodings")
    void shouldUseDefaultEncodingWhenNotSpecifiedInXmlDeclaration(final String defaultEncodingName) throws IOException {
        // Arrange
        final String expectedEncoding = defaultEncodingName == null ? StandardCharsets.UTF_8.name() : defaultEncodingName;
        final String xmlString = createXmlContent(TEXT_UNICODE, null); // XML without an 'encoding' attribute
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final XmlStreamWriter writer;

        // Act
        // The writer is inspected after the try-with-resources block closes it.
        try (XmlStreamWriter tempWriter = XmlStreamWriter.builder()
                .setOutputStream(out)
                .setCharset(defaultEncodingName) // Builder defaults to UTF-8 if this is null
                .get()) {
            writer = tempWriter;
            writer.write(xmlString);
        }

        // Assert
        // 1. Verify the writer detected and used the correct encoding.
        assertEquals(expectedEncoding, writer.getEncoding(),
            "Writer should have used the specified default encoding.");

        // 2. Verify the output bytes are correctly encoded.
        final byte[] expectedBytes = xmlString.getBytes(expectedEncoding);
        final byte[] actualBytes = out.toByteArray();
        assertArrayEquals(expectedBytes, actualBytes,
            "The written bytes should match the content encoded with the default encoding.");
    }
}