package com.itextpdf.text.xml.xmp;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Contains tests for the exception handling of the {@link XmpWriter} class.
 */
// The original test class name and inheritance are preserved as per the task.
public class XmpWriter_ESTestTest11 extends XmpWriter_ESTest_scaffolding {

    /**
     * Verifies that the serialize() method throws a NullPointerException when passed a null
     * OutputStream. This is the expected behavior, as serialization requires a valid output target.
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void serializeWithNullOutputStreamThrowsNullPointerException() throws IOException {
        // Arrange: Create an XmpWriter instance. The constructor's OutputStream
        // is not used by the serialize(OutputStream) method, so passing null is the simplest setup.
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null);

        // Act: Attempt to serialize to a null OutputStream.
        // The call is expected to throw a NullPointerException.
        xmpWriter.serialize(null);

        // Assert: The @Test(expected) annotation handles the assertion, ensuring the
        // test passes only if a NullPointerException is thrown.
    }
}