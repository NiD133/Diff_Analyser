package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertNullPointerException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

public class CharRangeTestTest13 extends AbstractLangTest {

    @Test
    void testEquals_Object() {
        final CharRange rangea = CharRange.is('a');
        final CharRange rangeae = CharRange.isIn('a', 'e');
        final CharRange rangenotbf = CharRange.isIn('b', 'f');
        assertNotEquals(null, rangea);
        assertEquals(rangea, rangea);
        assertEquals(rangea, CharRange.is('a'));
        assertEquals(rangeae, rangeae);
        assertEquals(rangeae, CharRange.isIn('a', 'e'));
        assertEquals(rangenotbf, rangenotbf);
        assertEquals(rangenotbf, CharRange.isIn('b', 'f'));
        assertNotEquals(rangea, rangeae);
        assertNotEquals(rangea, rangenotbf);
        assertNotEquals(rangeae, rangea);
        assertNotEquals(rangeae, rangenotbf);
        assertNotEquals(rangenotbf, rangea);
        assertNotEquals(rangenotbf, rangeae);
    }
}