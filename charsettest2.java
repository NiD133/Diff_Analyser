package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

public class CharSetTestTest2 extends AbstractLangTest {

    @Test
    void testConstructor_String_combo() {
        CharSet set;
        CharRange[] array;
        set = CharSet.getInstance("abc");
        array = set.getCharRanges();
        assertEquals(3, array.length);
        assertTrue(ArrayUtils.contains(array, CharRange.is('a')));
        assertTrue(ArrayUtils.contains(array, CharRange.is('b')));
        assertTrue(ArrayUtils.contains(array, CharRange.is('c')));
        set = CharSet.getInstance("a-ce-f");
        array = set.getCharRanges();
        assertEquals(2, array.length);
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('a', 'c')));
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('e', 'f')));
        set = CharSet.getInstance("ae-f");
        array = set.getCharRanges();
        assertEquals(2, array.length);
        assertTrue(ArrayUtils.contains(array, CharRange.is('a')));
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('e', 'f')));
        set = CharSet.getInstance("e-fa");
        array = set.getCharRanges();
        assertEquals(2, array.length);
        assertTrue(ArrayUtils.contains(array, CharRange.is('a')));
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('e', 'f')));
        set = CharSet.getInstance("ae-fm-pz");
        array = set.getCharRanges();
        assertEquals(4, array.length);
        assertTrue(ArrayUtils.contains(array, CharRange.is('a')));
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('e', 'f')));
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('m', 'p')));
        assertTrue(ArrayUtils.contains(array, CharRange.is('z')));
    }
}
