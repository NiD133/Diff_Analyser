package org.apache.commons.io.output;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.junit.Test;

/**
 * Contains tests for the {@link XmlStreamWriter} class.
 */
public class XmlStreamWriterTest {

    /**
     * Tests that the deprecated constructor {@code XmlStreamWriter(File, String)}
     * correctly defaults to UTF-8 when the specified encoding is null.
     * This ensures backward compatibility is maintained.
     */
    @Test(timeout = 4000)
    @SuppressWarnings("deprecation") // Intentionally testing a deprecated constructor.
    public void constructorWithFileAndNullEncodingShouldDefaultToUtf8() throws IOException {
        // Arrange: Create a mock file to avoid actual file system interaction.
        final File testFile = new MockFile("test.xml");
        final String nullEncoding = null;

        // Act & Assert: Instantiate the writer and verify its default encoding.
        // Using try-with-resources ensures the writer is closed automatically.
        try (final XmlStreamWriter writer = new XmlStreamWriter(testFile, nullEncoding)) {
            final String expectedEncoding = StandardCharsets.UTF_8.name();
            final String actualEncoding = writer.getDefaultEncoding();
            assertEquals("Default encoding should be UTF-8 when null is provided.", expectedEncoding, actualEncoding);
        }
    }
}