package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

public class CharSetTestTest3 extends AbstractLangTest {

    @Test
    void testConstructor_String_comboNegated() {
        CharSet set;
        CharRange[] array;
        set = CharSet.getInstance("^abc");
        array = set.getCharRanges();
        assertEquals(3, array.length);
        assertTrue(ArrayUtils.contains(array, CharRange.isNot('a')));
        assertTrue(ArrayUtils.contains(array, CharRange.is('b')));
        assertTrue(ArrayUtils.contains(array, CharRange.is('c')));
        set = CharSet.getInstance("b^ac");
        array = set.getCharRanges();
        assertEquals(3, array.length);
        assertTrue(ArrayUtils.contains(array, CharRange.is('b')));
        assertTrue(ArrayUtils.contains(array, CharRange.isNot('a')));
        assertTrue(ArrayUtils.contains(array, CharRange.is('c')));
        set = CharSet.getInstance("db^ac");
        array = set.getCharRanges();
        assertEquals(4, array.length);
        assertTrue(ArrayUtils.contains(array, CharRange.is('d')));
        assertTrue(ArrayUtils.contains(array, CharRange.is('b')));
        assertTrue(ArrayUtils.contains(array, CharRange.isNot('a')));
        assertTrue(ArrayUtils.contains(array, CharRange.is('c')));
        set = CharSet.getInstance("^b^a");
        array = set.getCharRanges();
        assertEquals(2, array.length);
        assertTrue(ArrayUtils.contains(array, CharRange.isNot('b')));
        assertTrue(ArrayUtils.contains(array, CharRange.isNot('a')));
        set = CharSet.getInstance("b^a-c^z");
        array = set.getCharRanges();
        assertEquals(3, array.length);
        assertTrue(ArrayUtils.contains(array, CharRange.isNotIn('a', 'c')));
        assertTrue(ArrayUtils.contains(array, CharRange.isNot('z')));
        assertTrue(ArrayUtils.contains(array, CharRange.is('b')));
    }
}
