package org.apache.commons.io.output;

import static org.junit.Assert.assertEquals;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Unit tests for the {@link XmlStreamWriter.Builder} class.
 */
public class XmlStreamWriterBuilderTest {

    /**
     * Tests that a new {@link XmlStreamWriter.Builder} instance is configured
     * with the default buffer size defined in {@link IOUtils}.
     */
    @Test
    public void newBuilderShouldUseDefaultBufferSize() {
        // Arrange
        final XmlStreamWriter.Builder builder = new XmlStreamWriter.Builder();
        final int expectedBufferSize = IOUtils.DEFAULT_BUFFER_SIZE;

        // Act
        final int actualBufferSize = builder.getBufferSizeDefault();

        // Assert
        assertEquals("The builder should be initialized with the default buffer size from IOUtils.",
                expectedBufferSize, actualBufferSize);
    }
}