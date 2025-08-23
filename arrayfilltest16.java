package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.jupiter.api.Test;

public class ArrayFillTestTest16 extends AbstractLangTest {

    @Test
    void testFillObjectArray() {
        final String[] array = new String[3];
        final String val = "A";
        final String[] actual = ArrayFill.fill(array, val);
        assertSame(array, actual);
        for (final String v : actual) {
            assertEquals(val, v);
        }
    }
}
