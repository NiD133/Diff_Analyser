package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

public class CharSetTestTest15 extends AbstractLangTest {

    @Test
    void testStatics() {
        CharRange[] array;
        array = CharSet.EMPTY.getCharRanges();
        assertEquals(0, array.length);
        array = CharSet.ASCII_ALPHA.getCharRanges();
        assertEquals(2, array.length);
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('a', 'z')));
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('A', 'Z')));
        array = CharSet.ASCII_ALPHA_LOWER.getCharRanges();
        assertEquals(1, array.length);
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('a', 'z')));
        array = CharSet.ASCII_ALPHA_UPPER.getCharRanges();
        assertEquals(1, array.length);
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('A', 'Z')));
        array = CharSet.ASCII_NUMERIC.getCharRanges();
        assertEquals(1, array.length);
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('0', '9')));
    }
}
