package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.apache.commons.compress.utils.ByteUtils.toLittleEndian;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.compress.utils.ByteUtils.InputStreamByteSupplier;
import org.apache.commons.compress.utils.ByteUtils.OutputStreamByteConsumer;
import org.junit.jupiter.api.Test;

public class ByteUtilsTestTest24 {

    @Test
    void testToLittleEndianToDataOutputUnsignedInt32() throws IOException {
        final byte[] byteArray;
        final byte[] expected = { 2, 3, 4, (byte) 128 };
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            final DataOutput dos = new DataOutputStream(bos);
            toLittleEndian(dos, 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, 4);
            byteArray = bos.toByteArray();
            assertArrayEquals(expected, byteArray);
        }
        assertArrayEquals(expected, byteArray);
    }
}
