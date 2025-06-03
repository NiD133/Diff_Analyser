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
     * Tests {@link ByteOrderMark#hashCode()}
     */
    @Test
    public void testHashCode() {
        final int bomClassHash = ByteOrderMark.class.hashCode();
        assertEquals(bomClassHash + 1, TEST_BOM_1.hashCode(), "hash test1 ");
        assertEquals(bomClassHash + 3, TEST_BOM_2.hashCode(), "hash test2 ");
        assertEquals(bomClassHash + 6, TEST_BOM_3.hashCode(), "hash test3 ");
    }
}
