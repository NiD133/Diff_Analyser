package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.jupiter.api.Test;

public class ArrayFillTestTest17 extends AbstractLangTest {

    @Test
    void testFillObjectArrayNull() {
        final Object[] array = null;
        final Object val = 1;
        final Object[] actual = ArrayFill.fill(array, val);
        assertSame(array, actual);
    }
}
