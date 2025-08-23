package com.itextpdf.text.xml.xmp;

import org.junit.Test;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the {@link XmpWriter} class.
 */
public class XmpWriterTest {

    /**
     * Verifies that the XmpWriter constructor can be instantiated with a null
     * OutputStream and a null info Map without throwing an exception.
     * The constructor is expected to handle these null arguments gracefully.
     */
    @Test
    public void constructorShouldAcceptNullArguments() throws IOException {
        // Act: Attempt to create an XmpWriter with null arguments for the stream and info map.
        // The explicit casts are necessary to resolve ambiguity with other overloaded constructors.
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null, (Map<String, String>) null);

        // Assert: The object should be successfully created and not be null.
        assertNotNull("XmpWriter should be instantiated even with null arguments.", xmpWriter);
    }
}