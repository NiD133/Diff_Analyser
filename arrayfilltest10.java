package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.jupiter.api.Test;

public class ArrayFillTestTest10 extends AbstractLangTest {

    @Test
    void testFillFloatArrayNull() {
        final float[] array = null;
        final float val = 1;
        final float[] actual = ArrayFill.fill(array, val);
        assertSame(array, actual);
    }
}
