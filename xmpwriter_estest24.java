package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPException;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This test class contains tests for the {@link XmpWriter} class.
 * This specific test focuses on the addDocInfoProperty method.
 */
// The original class name is kept to match the file context.
public class XmpWriter_ESTestTest24 {

    /**
     * Verifies that adding a standard document information property, such as "Subject",
     * to the XmpWriter executes successfully without throwing an exception.
     */
    @Test(timeout = 4000)
    public void addDocInfoPropertyWithSubjectKeyShouldNotThrowException() throws IOException, XMPException {
        // Arrange: Set up a realistic XmpWriter instance.
        // Using a ByteArrayOutputStream is safer and clearer than using null.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // Use a standard, valid encoding constant instead of a magic string.
        XmpWriter xmpWriter = new XmpWriter(outputStream, XmpWriter.UTF8, 2000);

        String propertyKey = "Subject";
        String propertyValue = "This is a sample subject.";

        // Act: Call the method under test.
        xmpWriter.addDocInfoProperty(propertyKey, propertyValue);

        // Assert: The test implicitly asserts that no exception was thrown during the 'Act' phase.
        // A successful execution of the above line is the passing condition for this test.
    }
}