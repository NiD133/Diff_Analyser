package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.jupiter.api.Test;

public class ArrayFillTestTest1 extends AbstractLangTest {

    @Test
    void testFillBooleanArray() {
        final boolean[] array = new boolean[3];
        final boolean val = true;
        final boolean[] actual = ArrayFill.fill(array, val);
        assertSame(array, actual);
        for (final boolean v : actual) {
            assertEquals(val, v);
        }
    }
}
