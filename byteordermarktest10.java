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

public class ByteOrderMarkTestTest10 {

    private static final ByteOrderMark TEST_BOM_1 = new ByteOrderMark("test1", 1);

    private static final ByteOrderMark TEST_BOM_2 = new ByteOrderMark("test2", 1, 2);

    private static final ByteOrderMark TEST_BOM_3 = new ByteOrderMark("test3", 1, 2, 3);

    /**
     * Tests {@link ByteOrderMark#toString()}
     */
    @Test
    void testToString() {
        assertEquals("ByteOrderMark[test1: 0x1]", TEST_BOM_1.toString(), "test1 ");
        assertEquals("ByteOrderMark[test2: 0x1,0x2]", TEST_BOM_2.toString(), "test2 ");
        assertEquals("ByteOrderMark[test3: 0x1,0x2,0x3]", TEST_BOM_3.toString(), "test3 ");
    }
}
