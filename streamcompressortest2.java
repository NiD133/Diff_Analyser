package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import org.junit.jupiter.api.Test;

public class StreamCompressorTestTest2 {

    @Test
    void testDeflatedEntries() throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (StreamCompressor sc = StreamCompressor.create(baos)) {
            sc.deflate(new ByteArrayInputStream("AAAAAABBBBBB".getBytes()), ZipEntry.DEFLATED);
            assertEquals(12, sc.getBytesRead());
            assertEquals(8, sc.getBytesWrittenForLastEntry());
            assertEquals(3299542, sc.getCrc32());
            final byte[] actuals = baos.toByteArray();
            final byte[] expected = { 115, 116, 4, 1, 39, 48, 0, 0 };
            // Note that this test really asserts stuff about the java Deflater, which might be a little bit brittle
            assertArrayEquals(expected, actuals);
        }
    }
}
