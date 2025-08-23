package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPException;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Contains tests for the {@link XmpWriter} class, focusing on its error handling and edge cases.
 */
public class XmpWriterTest {

    /**
     * Verifies that setProperty throws a NullPointerException when the internal
     * XMPMeta object is null.
     * <p>
     * This is a white-box test that simulates an invalid internal state.
     * The purpose is to ensure the method is robust and fails immediately
     * under unexpected conditions, which could prevent more obscure errors later on.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void setPropertyShouldThrowNPEWhenInternalMetaIsNull() throws IOException, XMPException {
        // Arrange: Create an XmpWriter and simulate an invalid internal state.
        // A ByteArrayOutputStream is used as a dummy stream since no output is written in this test.
        XmpWriter xmpWriter = new XmpWriter(new ByteArrayOutputStream());

        // Manually set the internal xmpMeta field to null to trigger the expected exception.
        xmpWriter.xmpMeta = null;

        // Define realistic, descriptive parameters for the method call.
        String schemaNS = "http://ns.adobe.com/pdf/1.3/";
        String propName = "Producer";
        Object value = "My Application";

        // Act & Assert: This call is expected to throw a NullPointerException because it
        // internally attempts to call a method on the null xmpMeta object.
        // The @Test(expected) annotation handles the assertion.
        xmpWriter.setProperty(schemaNS, propName, value);
    }
}