package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.jupiter.api.Test;

public class ArrayFillTestTest14 extends AbstractLangTest {

    @Test
    void testFillLongArray() {
        final long[] array = new long[3];
        final long val = 1;
        final long[] actual = ArrayFill.fill(array, val);
        assertSame(array, actual);
        for (final long v : actual) {
            assertEquals(val, v);
        }
    }
}
