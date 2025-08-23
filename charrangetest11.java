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

public class CharRangeTestTest11 extends AbstractLangTest {

    @Test
    void testContains_Charrange() {
        final CharRange a = CharRange.is('a');
        final CharRange b = CharRange.is('b');
        final CharRange c = CharRange.is('c');
        final CharRange c2 = CharRange.is('c');
        final CharRange d = CharRange.is('d');
        final CharRange e = CharRange.is('e');
        final CharRange cd = CharRange.isIn('c', 'd');
        final CharRange bd = CharRange.isIn('b', 'd');
        final CharRange bc = CharRange.isIn('b', 'c');
        final CharRange ab = CharRange.isIn('a', 'b');
        final CharRange de = CharRange.isIn('d', 'e');
        final CharRange ef = CharRange.isIn('e', 'f');
        final CharRange ae = CharRange.isIn('a', 'e');
        // normal/normal
        assertFalse(c.contains(b));
        assertTrue(c.contains(c));
        assertTrue(c.contains(c2));
        assertFalse(c.contains(d));
        assertFalse(c.contains(cd));
        assertFalse(c.contains(bd));
        assertFalse(c.contains(bc));
        assertFalse(c.contains(ab));
        assertFalse(c.contains(de));
        assertTrue(cd.contains(c));
        assertTrue(bd.contains(c));
        assertTrue(bc.contains(c));
        assertFalse(ab.contains(c));
        assertFalse(de.contains(c));
        assertTrue(ae.contains(b));
        assertTrue(ae.contains(ab));
        assertTrue(ae.contains(bc));
        assertTrue(ae.contains(cd));
        assertTrue(ae.contains(de));
        final CharRange notb = CharRange.isNot('b');
        final CharRange notc = CharRange.isNot('c');
        final CharRange notd = CharRange.isNot('d');
        final CharRange notab = CharRange.isNotIn('a', 'b');
        final CharRange notbc = CharRange.isNotIn('b', 'c');
        final CharRange notbd = CharRange.isNotIn('b', 'd');
        final CharRange notcd = CharRange.isNotIn('c', 'd');
        final CharRange notde = CharRange.isNotIn('d', 'e');
        final CharRange notae = CharRange.isNotIn('a', 'e');
        final CharRange all = CharRange.isIn((char) 0, Character.MAX_VALUE);
        final CharRange allbutfirst = CharRange.isIn((char) 1, Character.MAX_VALUE);
        // normal/negated
        assertFalse(c.contains(notc));
        assertFalse(c.contains(notbd));
        assertTrue(all.contains(notc));
        assertTrue(all.contains(notbd));
        assertFalse(allbutfirst.contains(notc));
        assertFalse(allbutfirst.contains(notbd));
        // negated/normal
        assertTrue(notc.contains(a));
        assertTrue(notc.contains(b));
        assertFalse(notc.contains(c));
        assertTrue(notc.contains(d));
        assertTrue(notc.contains(e));
        assertTrue(notc.contains(ab));
        assertFalse(notc.contains(bc));
        assertFalse(notc.contains(bd));
        assertFalse(notc.contains(cd));
        assertTrue(notc.contains(de));
        assertFalse(notc.contains(ae));
        assertFalse(notc.contains(all));
        assertFalse(notc.contains(allbutfirst));
        assertTrue(notbd.contains(a));
        assertFalse(notbd.contains(b));
        assertFalse(notbd.contains(c));
        assertFalse(notbd.contains(d));
        assertTrue(notbd.contains(e));
        assertTrue(notcd.contains(ab));
        assertFalse(notcd.contains(bc));
        assertFalse(notcd.contains(bd));
        assertFalse(notcd.contains(cd));
        assertFalse(notcd.contains(de));
        assertFalse(notcd.contains(ae));
        assertTrue(notcd.contains(ef));
        assertFalse(notcd.contains(all));
        assertFalse(notcd.contains(allbutfirst));
        // negated/negated
        assertFalse(notc.contains(notb));
        assertTrue(notc.contains(notc));
        assertFalse(notc.contains(notd));
        assertFalse(notc.contains(notab));
        assertTrue(notc.contains(notbc));
        assertTrue(notc.contains(notbd));
        assertTrue(notc.contains(notcd));
        assertFalse(notc.contains(notde));
        assertFalse(notbd.contains(notb));
        assertFalse(notbd.contains(notc));
        assertFalse(notbd.contains(notd));
        assertFalse(notbd.contains(notab));
        assertFalse(notbd.contains(notbc));
        assertTrue(notbd.contains(notbd));
        assertFalse(notbd.contains(notcd));
        assertFalse(notbd.contains(notde));
        assertTrue(notbd.contains(notae));
    }
}
