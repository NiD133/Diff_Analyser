package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

public class CharSetTestTest4 extends AbstractLangTest {

    @Test
    void testConstructor_String_oddCombinations() {
        CharSet set;
        CharRange[] array;
        set = CharSet.getInstance("a-^c");
        array = set.getCharRanges();
        // "a-^"
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('a', '^')));
        // "c"
        assertTrue(ArrayUtils.contains(array, CharRange.is('c')));
        assertFalse(set.contains('b'));
        assertTrue(set.contains('^'));
        // between ^ and a
        assertTrue(set.contains('_'));
        assertTrue(set.contains('c'));
        set = CharSet.getInstance("^a-^c");
        array = set.getCharRanges();
        // "^a-^"
        assertTrue(ArrayUtils.contains(array, CharRange.isNotIn('a', '^')));
        // "c"
        assertTrue(ArrayUtils.contains(array, CharRange.is('c')));
        assertTrue(set.contains('b'));
        assertFalse(set.contains('^'));
        // between ^ and a
        assertFalse(set.contains('_'));
        //contains everything
        set = CharSet.getInstance("a- ^-- ");
        array = set.getCharRanges();
        // "a- "
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('a', ' ')));
        // "^-- "
        assertTrue(ArrayUtils.contains(array, CharRange.isNotIn('-', ' ')));
        assertTrue(set.contains('#'));
        assertTrue(set.contains('^'));
        assertTrue(set.contains('a'));
        assertTrue(set.contains('*'));
        assertTrue(set.contains('A'));
        set = CharSet.getInstance("^-b");
        array = set.getCharRanges();
        // "^-b"
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('^', 'b')));
        assertTrue(set.contains('b'));
        // between ^ and a
        assertTrue(set.contains('_'));
        assertFalse(set.contains('A'));
        assertTrue(set.contains('^'));
        set = CharSet.getInstance("b-^");
        array = set.getCharRanges();
        // "b-^"
        assertTrue(ArrayUtils.contains(array, CharRange.isIn('^', 'b')));
        assertTrue(set.contains('b'));
        assertTrue(set.contains('^'));
        // between ^ and b
        assertTrue(set.contains('a'));
        assertFalse(set.contains('c'));
    }
}
