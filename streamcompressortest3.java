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

public class StreamCompressorTestTest3 {

    @Test
    void testStoredEntries() throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (StreamCompressor sc = StreamCompressor.create(baos)) {
            sc.deflate(new ByteArrayInputStream("A".getBytes()), ZipEntry.STORED);
            sc.deflate(new ByteArrayInputStream("BAD".getBytes()), ZipEntry.STORED);
            assertEquals(3, sc.getBytesRead());
            assertEquals(3, sc.getBytesWrittenForLastEntry());
            assertEquals(344750961, sc.getCrc32());
            sc.deflate(new ByteArrayInputStream("CAFE".getBytes()), ZipEntry.STORED);
            assertEquals("ABADCAFE", baos.toString());
        }
    }
}
