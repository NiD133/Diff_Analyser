package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

public class CharSetTestTest7 extends AbstractLangTest {

    @Test
    void testConstructor_String_simple() {
        CharSet set;
        CharRange[] array;
        set = CharSet.getInstance((String) null);
        array = set.getCharRanges();
        assertEquals("[]", set.toString());
        assertEquals(0, array.length);
        set = CharSet.getInstance("");
        array = set.getCharRanges();
        assertEquals("[]", set.toString());
        assertEquals(0, array.length);
        set = CharSet.getInstance("a");
        array = set.getCharRanges();
        assertEquals("[a]", set.toString());
        assertEquals(1, array.length);
        assertEquals("a", array[0].toString());
        set = CharSet.getInstance("^a");
        array = set.getCharRanges();
        assertEquals("[^a]", set.toString());
        assertEquals(1, array.length);
        assertEquals("^a", array[0].toString());
        set = CharSet.getInstance("a-e");
        array = set.getCharRanges();
        assertEquals("[a-e]", set.toString());
        assertEquals(1, array.length);
        assertEquals("a-e", array[0].toString());
        set = CharSet.getInstance("^a-e");
        array = set.getCharRanges();
        assertEquals("[^a-e]", set.toString());
        assertEquals(1, array.length);
        assertEquals("^a-e", array[0].toString());
    }
}
