// Improved for clarity and maintainability
package com.itextpdf.text.pdf;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;

/**
 * Documents expected behavior of MappedRandomAccessFile when the underlying file is empty.
 * The class should mirror java.io.RandomAccessFile semantics:
 * - length() returns 0
 * - initial file pointer is 0
 * - read() and read(byte[], off, len) return -1 to indicate EOF
 */
public class MappedRandomAccessFileTest {

    private File zeroSizedPdf;

    @Before
    public void setUp() throws Exception {
        TestResourceUtils.purgeTempFiles();
        // Arrange: copy the zero-length test resource to a temp file
        zeroSizedPdf = TestResourceUtils.getResourceAsTempFile(getClass(), "zerosizedfile.pdf");
    }

    @After
    public void tearDown() throws Exception {
        TestResourceUtils.purgeTempFiles();
    }

    @Test
    public void readReturnsMinusOneForZeroLengthFile() throws Exception {
        // Open in read-write to exercise the same path used by most callers
        final MappedRandomAccessFile raf =
                new MappedRandomAccessFile(zeroSizedPdf.getCanonicalPath(), "rw");
        try {
            // Then: basic invariants for an empty file
            Assert.assertEquals("A zero-length file must report length 0.", 0L, raf.length());
            Assert.assertEquals("Initial file pointer must be at position 0.", 0L, raf.getFilePointer());

            // When/Then: reading from an empty file yields EOF (-1)
            Assert.assertEquals("read() must return -1 (EOF) for zero-length files.", -1, raf.read());

            // Subsequent reads after EOF should continue to return -1
            Assert.assertEquals("Subsequent read() calls after EOF must also return -1.", -1, raf.read());

            // read(byte[], off, len) should also return -1 for zero-length files
            byte[] buffer = new byte[8];
            Assert.assertEquals("read(byte[],off,len) must return -1 (EOF) for zero-length files.",
                    -1, raf.read(buffer, 0, buffer.length));
        } finally {
            raf.close();
        }
    }
}