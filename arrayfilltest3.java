package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.jupiter.api.Test;

public class ArrayFillTestTest3 extends AbstractLangTest {

    @Test
    void testFillByteArray() {
        final byte[] array = new byte[3];
        final byte val = (byte) 1;
        final byte[] actual = ArrayFill.fill(array, val);
        assertSame(array, actual);
        for (final byte v : actual) {
            assertEquals(val, v);
        }
    }
}
