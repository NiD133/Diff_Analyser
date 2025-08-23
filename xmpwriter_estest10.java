package com.itextpdf.text.xml.xmp;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Contains tests for the {@link XmpWriter} class.
 * This refactored test focuses on verifying exception handling in edge cases.
 */
public class XmpWriter_ESTestTest10 extends XmpWriter_ESTest_scaffolding {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Verifies that calling serialize() on an XmpWriter with a null internal
     * XMPMeta object throws an UnsupportedOperationException.
     * <p>
     * This test case simulates an invalid internal state to ensure the method
     * fails predictably rather than causing a NullPointerException.
     */
    @Test
    public void serializeThrowsUnsupportedOperationExceptionWhenXmpMetaIsNull() throws IOException {
        // Arrange: Create an XmpWriter and manually set its internal xmpMeta to null.
        // This is a white-box test to simulate a specific invalid state.
        XmpWriter xmpWriter = new XmpWriter((OutputStream) null);
        xmpWriter.xmpMeta = null;

        // Assert: Define the expected exception type and message that should be thrown.
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("The serializing service works onlywith the XMPMeta implementation of this library");

        // Act: Attempt to serialize with the null xmpMeta, which should trigger the exception.
        xmpWriter.serialize((OutputStream) null);
    }
}