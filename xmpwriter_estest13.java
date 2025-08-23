package com.itextpdf.text.xml.xmp;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Contains unit tests for the {@link XmpWriter} class, focusing on exception handling and edge cases.
 */
public class XmpWriterTest {

    /**
     * Verifies that calling appendOrderedArrayItem throws a NullPointerException
     * if the internal XMPMeta object is null. This is a white-box test that
     * simulates an invalid internal state to ensure robustness.
     */
    @Test(expected = NullPointerException.class)
    public void appendOrderedArrayItem_whenInternalMetaIsNull_throwsNullPointerException() throws IOException {
        // Arrange: Create an XmpWriter and then manually set its internal xmpMeta
        // field to null to simulate the state under test.
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null, XmpWriter.UTF8, 0);
        xmpWriter.xmpMeta = null; // This is the specific condition being tested.

        // Act: Attempt to append an item. This should trigger the exception because
        // the method internally dereferences the null xmpMeta object.
        xmpWriter.appendOrderedArrayItem("test_namespace", "test_arrayName", "test_value");

        // Assert: The test passes if a NullPointerException is thrown, which is
        // handled by the @Test(expected = ...) annotation.
    }
}