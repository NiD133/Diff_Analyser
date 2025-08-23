package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.jupiter.api.Test;

public class ArrayFillTestTest13 extends AbstractLangTest {

    @Test
    void testFillIntArrayNull() {
        final int[] array = null;
        final int val = 1;
        final int[] actual = ArrayFill.fill(array, val);
        assertSame(array, actual);
    }
}
