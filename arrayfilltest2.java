package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.jupiter.api.Test;

public class ArrayFillTestTest2 extends AbstractLangTest {

    @Test
    void testFillBooleanArrayNull() {
        final boolean[] array = null;
        final boolean val = true;
        final boolean[] actual = ArrayFill.fill(array, val);
        assertSame(array, actual);
    }
}
