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
     * Tests {@link ByteOrderMark#getCharsetName()}
     */
    @Test
    public void testGetCharsetName() {
        assertEquals("test1", TEST_BOM_1.getCharsetName(), "test1 name");
        assertEquals("test2", TEST_BOM_2.getCharsetName(), "test2 name");
        assertEquals("test3", TEST_BOM_3.getCharsetName(), "test3 name");
    }
}
