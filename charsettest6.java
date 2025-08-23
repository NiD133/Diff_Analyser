package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

public class CharSetTestTest6 extends AbstractLangTest {

    @Test
    void testConstructor_String_oddNegate() {
        CharSet set;
        CharRange[] array;
        set = CharSet.getInstance("^");
        array = set.getCharRanges();
        assertEquals(1, array.length);
        // "^"
        assertTrue(ArrayUtils.contains(array, CharRange.is('^')));
        set = CharSet.getInstance("^^");
        array = set.getCharRanges();
        assertEquals(1, array.length);
        // "^^"
        assertTrue(ArrayUtils.contains(array, CharRange.isNot('^')));
        set = CharSet.getInstance("^^^");
        array = set.getCharRanges();
        assertEquals(2, array.length);
        // "^^"
        assertTrue(ArrayUtils.contains(array, CharRange.isNot('^')));
        // "^"
        assertTrue(ArrayUtils.contains(array, CharRange.is('^')));
        set = CharSet.getInstance("^^^^");
        array = set.getCharRanges();
        assertEquals(1, array.length);
        // "^^" x2
        assertTrue(ArrayUtils.contains(array, CharRange.isNot('^')));
        set = CharSet.getInstance("a^");
        array = set.getCharRanges();
        assertEquals(2, array.length);
        // "a"
        assertTrue(ArrayUtils.contains(array, CharRange.is('a')));
        // "^"
        assertTrue(ArrayUtils.contains(array, CharRange.is('^')));
        set = CharSet.getInstance("^a-");
        array = set.getCharRanges();
        assertEquals(2, array.length);
        // "^a"
        assertTrue(ArrayUtils.contains(array, CharRange.isNot('a')));
        // "-"
        assertTrue(ArrayUtils.contains(array, CharRange.is('-')));
        set = CharSet.getInstance("^^-c");
        array = set.getCharRanges();
        assertEquals(1, array.length);
        // "^^-c"
        assertTrue(ArrayUtils.contains(array, CharRange.isNotIn('^', 'c')));
        set = CharSet.getInstance("^c-^");
        array = set.getCharRanges();
        assertEquals(1, array.length);
        // "^c-^"
        assertTrue(ArrayUtils.contains(array, CharRange.isNotIn('c', '^')));
        set = CharSet.getInstance("^c-^d");
        array = set.getCharRanges();
        assertEquals(2, array.length);
        // "^c-^"
        assertTrue(ArrayUtils.contains(array, CharRange.isNotIn('c', '^')));
        // "d"
        assertTrue(ArrayUtils.contains(array, CharRange.is('d')));
        set = CharSet.getInstance("^^-");
        array = set.getCharRanges();
        assertEquals(2, array.length);
        // "^^"
        assertTrue(ArrayUtils.contains(array, CharRange.isNot('^')));
        // "-"
        assertTrue(ArrayUtils.contains(array, CharRange.is('-')));
    }
}
