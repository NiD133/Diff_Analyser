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

public class GeneratedTestCase {

    /**
     * Tests {@link ByteOrderMark#length()}
     */
    @Test
    public void testLength() {
        assertEquals(1, TEST_BOM_1.length(), "test1 length");
        assertEquals(2, TEST_BOM_2.length(), "test2 length");
        assertEquals(3, TEST_BOM_3.length(), "test3 length");
    }
}
