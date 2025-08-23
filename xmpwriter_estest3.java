package com.itextpdf.text.xml.xmp;

import org.junit.Test;

import java.io.ByteArrayOutputStream;

/**
 * Test suite for the {@link XmpWriter} class.
 */
public class XmpWriterTest {

    /**
     * Tests that an XmpWriter can be successfully instantiated and closed without errors.
     * This acts as a basic "smoke test" to ensure the fundamental lifecycle of the writer
     * (creation with parameters and closing) works as expected.
     */
    @Test
    public void writerCanBeCreatedAndClosedSuccessfully() throws Exception {
        // Arrange: Set up the necessary objects for the test.
        // We use a standard ByteArrayOutputStream as a sink for the writer's output.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String encoding = XmpWriter.UTF8;
        int padding = 2048; // A typical padding size for XMP metadata.

        // Act: Create an instance of XmpWriter and immediately close it.
        // The close() method is expected to write the initial XMP structure to the stream.
        XmpWriter xmpWriter = new XmpWriter(outputStream, encoding, padding);
        xmpWriter.close();

        // Assert: The test passes if no exceptions were thrown during the Arrange and Act steps.
        // This confirms that the writer's constructor and close() method can execute
        // without issues under normal conditions.
    }
}