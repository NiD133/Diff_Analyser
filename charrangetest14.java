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

public class CharRangeTestTest14 extends AbstractLangTest {

    @Test
    void testHashCode() {
        final CharRange rangea = CharRange.is('a');
        final CharRange rangeae = CharRange.isIn('a', 'e');
        final CharRange rangenotbf = CharRange.isIn('b', 'f');
        assertEquals(rangea.hashCode(), rangea.hashCode());
        assertEquals(rangea.hashCode(), CharRange.is('a').hashCode());
        assertEquals(rangeae.hashCode(), rangeae.hashCode());
        assertEquals(rangeae.hashCode(), CharRange.isIn('a', 'e').hashCode());
        assertEquals(rangenotbf.hashCode(), rangenotbf.hashCode());
        assertEquals(rangenotbf.hashCode(), CharRange.isIn('b', 'f').hashCode());
        assertNotEquals(rangea.hashCode(), rangeae.hashCode());
        assertNotEquals(rangea.hashCode(), rangenotbf.hashCode());
        assertNotEquals(rangeae.hashCode(), rangea.hashCode());
        assertNotEquals(rangeae.hashCode(), rangenotbf.hashCode());
        assertNotEquals(rangenotbf.hashCode(), rangea.hashCode());
        assertNotEquals(rangenotbf.hashCode(), rangeae.hashCode());
    }
}
