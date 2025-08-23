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

public class StreamCompressorTestTest1 {

    @Test
    void testCreateDataOutputCompressor() throws IOException {
        final DataOutput dataOutputStream = new DataOutputStream(new ByteArrayOutputStream());
        try (StreamCompressor streamCompressor = StreamCompressor.create(dataOutputStream, new Deflater(9))) {
            assertNotNull(streamCompressor);
        }
    }
}
