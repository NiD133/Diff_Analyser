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
     * Tests {@link ByteOrderMark#get(int)}
     */
    @Test
    public void testGetInt() {
        assertEquals(1, TEST_BOM_1.get(0), "test1 get(0)");
        assertEquals(1, TEST_BOM_2.get(0), "test2 get(0)");
        assertEquals(2, TEST_BOM_2.get(1), "test2 get(1)");
        assertEquals(1, TEST_BOM_3.get(0), "test3 get(0)");
        assertEquals(2, TEST_BOM_3.get(1), "test3 get(1)");
        assertEquals(3, TEST_BOM_3.get(2), "test3 get(2)");
    }
}
