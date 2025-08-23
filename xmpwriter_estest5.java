package com.itextpdf.text.xml.xmp;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Test suite for the {@link XmpWriter} class.
 */
public class XmpWriterTest {

    /**
     * Verifies that the deprecated method {@code addRdfDescription(String, String)}
     * does not throw an exception when called with empty string arguments.
     * <p>
     * This test is important for ensuring backward compatibility with older code
     * that might still use this deprecated method. The test passes if the method
     * call completes without any errors.
     * </p>
     */
    @Test(timeout = 4000)
    public void addRdfDescriptionWithEmptyParametersShouldNotThrowException() throws IOException {
        // Arrange: Set up the necessary objects for the test.
        // Use a standard ByteArrayOutputStream to act as a sink for any output.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // The XmpWriter constructor requires a Map; an empty one suffices for this test.
        Map<String, String> documentInfo = new HashMap<>();
        
        XmpWriter xmpWriter = new XmpWriter(outputStream, documentInfo);

        // Act: Call the deprecated method with empty strings.
        xmpWriter.addRdfDescription("", "");

        // Assert: The test succeeds if no exception is thrown.
        // This is handled automatically by the JUnit test runner. No explicit assertion is needed.
    }
}