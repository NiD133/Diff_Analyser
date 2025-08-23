package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPMeta;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertNull;

/**
 * Test suite for the {@link XmpWriter} class.
 */
public class XmpWriterTest {

    /**
     * Verifies that the getXmpMeta() method returns null if the internal
     * xmpMeta field has been explicitly set to null.
     *
     * <p><b>Note:</b> This is a white-box test that directly manipulates the
     * internal state of the object. In normal usage, the {@code xmpMeta} field is
     * initialized by the constructor and is not expected to be null. This test
     * simply ensures the getter method behaves as a simple accessor even in this
     * unusual state.</p>
     *
     * @throws IOException if an I/O error occurs during XmpWriter construction.
     */
    @Test
    public void getXmpMeta_shouldReturnNull_whenInternalMetaFieldIsManuallyNulled() throws IOException {
        // Arrange
        // An XmpWriter is created. Its internal xmpMeta object is initialized by the constructor.
        // We use a simple constructor and a dummy output stream, as they are not relevant to this test.
        XmpWriter xmpWriter = new XmpWriter(new ByteArrayOutputStream());

        // To test the getter's behavior in an edge case, we manually set the internal
        // (protected) field to null. This is not a standard use case.
        xmpWriter.xmpMeta = null;

        // Act
        // Call the getter method to retrieve the (now null) internal object.
        XMPMeta retrievedMeta = xmpWriter.getXmpMeta();

        // Assert
        // Verify that the getter returned the null value we assigned.
        assertNull("getXmpMeta() should return the value of the internal xmpMeta field, which was set to null.", retrievedMeta);
    }
}