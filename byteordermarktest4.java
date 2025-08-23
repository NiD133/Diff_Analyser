package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.Charset;
import org.junit.jupiter.api.Test;

public class ByteOrderMarkTestTest4 {

    private static final ByteOrderMark TEST_BOM_1 = new ByteOrderMark("test1", 1);

    private static final ByteOrderMark TEST_BOM_2 = new ByteOrderMark("test2", 1, 2);

    private static final ByteOrderMark TEST_BOM_3 = new ByteOrderMark("test3", 1, 2, 3);

    /**
     * Tests {@link ByteOrderMark#getBytes()}
     */
    @Test
    void testGetBytes() {
        assertArrayEquals(TEST_BOM_1.getBytes(), new byte[] { (byte) 1 }, "test1 bytes");
        TEST_BOM_1.getBytes()[0] = 2;
        assertArrayEquals(TEST_BOM_1.getBytes(), new byte[] { (byte) 1 }, "test1 bytes");
        assertArrayEquals(TEST_BOM_2.getBytes(), new byte[] { (byte) 1, (byte) 2 }, "test1 bytes");
        assertArrayEquals(TEST_BOM_3.getBytes(), new byte[] { (byte) 1, (byte) 2, (byte) 3 }, "test1 bytes");
    }
}
