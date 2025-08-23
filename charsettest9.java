package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

public class CharSetTestTest9 extends AbstractLangTest {

    @Test
    void testEquals_Object() {
        final CharSet abc = CharSet.getInstance("abc");
        final CharSet abc2 = CharSet.getInstance("abc");
        final CharSet atoc = CharSet.getInstance("a-c");
        final CharSet atoc2 = CharSet.getInstance("a-c");
        final CharSet notatoc = CharSet.getInstance("^a-c");
        final CharSet notatoc2 = CharSet.getInstance("^a-c");
        assertNotEquals(null, abc);
        assertEquals(abc, abc);
        assertEquals(abc, abc2);
        assertNotEquals(abc, atoc);
        assertNotEquals(abc, notatoc);
        assertNotEquals(atoc, abc);
        assertEquals(atoc, atoc);
        assertEquals(atoc, atoc2);
        assertNotEquals(atoc, notatoc);
        assertNotEquals(notatoc, abc);
        assertNotEquals(notatoc, atoc);
        assertEquals(notatoc, notatoc);
        assertEquals(notatoc, notatoc2);
    }
}
