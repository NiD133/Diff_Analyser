package com.fasterxml.jackson.core.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, behavior-oriented tests for Separators.
 *
 * Goals:
 * - Make defaults and constructors obvious
 * - Show when withX methods return the same instance vs a new one
 * - Document spacing helpers on the Spacing enum
 * - Keep assertions focused and meaningful
 */
public class SeparatorsTest {

    @Test
    public void defaults_fromNoArgConstructor() {
        Separators s = new Separators();

        // Characters
        assertEquals(':', s.getObjectFieldValueSeparator());
        assertEquals(',', s.getObjectEntrySeparator());
        assertEquals(',', s.getArrayValueSeparator());

        // Spacing defaults
        assertEquals(Separators.Spacing.BOTH, s.getObjectFieldValueSpacing());
        assertEquals(Separators.Spacing.NONE, s.getObjectEntrySpacing());
        assertEquals(Separators.Spacing.NONE, s.getArrayValueSpacing());

        // Empty separators and root separator default to a single space
        assertEquals(" ", s.getRootSeparator());
        assertEquals(" ", s.getObjectEmptySeparator());
        assertEquals(" ", s.getArrayEmptySeparator());
    }

    @Test
    public void threeCharConstructor_setsCharsAndDefaultSpacings() {
        Separators s = new Separators('J', 'b', 'N');

        assertEquals(" ", s.getRootSeparator());

        assertEquals('J', s.getObjectFieldValueSeparator());
        assertEquals(Separators.Spacing.BOTH, s.getObjectFieldValueSpacing());

        assertEquals('b', s.getObjectEntrySeparator());
        assertEquals(Separators.Spacing.NONE, s.getObjectEntrySpacing());

        assertEquals('N', s.getArrayValueSeparator());
        assertEquals(Separators.Spacing.NONE, s.getArrayValueSpacing());

        assertEquals(" ", s.getObjectEmptySeparator());
        assertEquals(" ", s.getArrayEmptySeparator());
    }

    @Test
    public void createDefaultInstance_matchesNoArgConstructor() {
        Separators a = Separators.createDefaultInstance();
        Separators b = new Separators();

        assertEquals(a.getRootSeparator(), b.getRootSeparator());
        assertEquals(a.getObjectFieldValueSeparator(), b.getObjectFieldValueSeparator());
        assertEquals(a.getObjectFieldValueSpacing(), b.getObjectFieldValueSpacing());
        assertEquals(a.getObjectEntrySeparator(), b.getObjectEntrySeparator());
        assertEquals(a.getObjectEntrySpacing(), b.getObjectEntrySpacing());
        assertEquals(a.getObjectEmptySeparator(), b.getObjectEmptySeparator());
        assertEquals(a.getArrayValueSeparator(), b.getArrayValueSeparator());
        assertEquals(a.getArrayValueSpacing(), b.getArrayValueSpacing());
        assertEquals(a.getArrayEmptySeparator(), b.getArrayEmptySeparator());
    }

    @Test
    public void canonicalConstructor_usesAllArguments() {
        Separators s = new Separators(
                "",                          // root
                'X', Separators.Spacing.AFTER, // object field value
                ';', Separators.Spacing.BEFORE, // object entry
                "{}",                          // empty object
                '|', Separators.Spacing.BOTH,  // array value
                "[]"                           // empty array
        );

        assertEquals("", s.getRootSeparator());
        assertEquals('X', s.getObjectFieldValueSeparator());
        assertEquals(Separators.Spacing.AFTER, s.getObjectFieldValueSpacing());
        assertEquals(';', s.getObjectEntrySeparator());
        assertEquals(Separators.Spacing.BEFORE, s.getObjectEntrySpacing());
        assertEquals("{}", s.getObjectEmptySeparator());
        assertEquals('|', s.getArrayValueSeparator());
        assertEquals(Separators.Spacing.BOTH, s.getArrayValueSpacing());
        assertEquals("[]", s.getArrayEmptySeparator());
    }

    @Test
    public void deprecatedConstructor_fillsEmptySeparatorsWithDefaults() {
        Separators s = new Separators(
                "", 'V', Separators.Spacing.AFTER,
                '9', Separators.Spacing.AFTER,
                '9', Separators.Spacing.AFTER
        );

        assertEquals("", s.getRootSeparator());
        assertEquals('V', s.getObjectFieldValueSeparator());
        assertEquals('9', s.getObjectEntrySeparator());
        assertEquals('9', s.getArrayValueSeparator());

        // Empty separators use defaults when not provided
        assertEquals(" ", s.getObjectEmptySeparator());
        assertEquals(" ", s.getArrayEmptySeparator());
    }

    @Test
    public void withMethods_returnSameInstanceWhenNoChange() {
        Separators s = new Separators();

        assertSame(s, s.withObjectFieldValueSeparator(':'));
        assertSame(s, s.withObjectEntrySeparator(','));
        assertSame(s, s.withArrayValueSeparator(','));
        assertSame(s, s.withObjectFieldValueSpacing(Separators.Spacing.BOTH));
        assertSame(s, s.withObjectEntrySpacing(Separators.Spacing.NONE));
        assertSame(s, s.withArrayValueSpacing(Separators.Spacing.NONE));
        assertSame(s, s.withRootSeparator(" "));
        assertSame(s, s.withObjectEmptySeparator(" "));
        assertSame(s, s.withArrayEmptySeparator(" "));
    }

    @Test
    public void withMethods_createNewInstanceWhenValueChanges() {
        Separators base = new Separators();

        Separators s1 = base.withObjectFieldValueSeparator('|');
        assertNotSame(base, s1);
        assertEquals('|', s1.getObjectFieldValueSeparator());
        assertEquals(base.getObjectEntrySeparator(), s1.getObjectEntrySeparator());

        Separators s2 = base.withObjectEntrySeparator('e');
        assertNotSame(base, s2);
        assertEquals('e', s2.getObjectEntrySeparator());
        assertEquals(base.getArrayValueSeparator(), s2.getArrayValueSeparator());

        Separators s3 = base.withArrayValueSeparator('T');
        assertNotSame(base, s3);
        assertEquals('T', s3.getArrayValueSeparator());

        Separators s4 = base.withObjectFieldValueSpacing(Separators.Spacing.AFTER);
        assertNotSame(base, s4);
        assertEquals(Separators.Spacing.AFTER, s4.getObjectFieldValueSpacing());

        Separators s5 = base.withObjectEntrySpacing(Separators.Spacing.AFTER);
        assertNotSame(base, s5);
        assertEquals(Separators.Spacing.AFTER, s5.getObjectEntrySpacing());

        Separators s6 = base.withArrayValueSpacing(Separators.Spacing.AFTER);
        assertNotSame(base, s6);
        assertEquals(Separators.Spacing.AFTER, s6.getArrayValueSpacing());

        Separators s7 = base.withRootSeparator("");
        assertNotSame(base, s7);
        assertEquals("", s7.getRootSeparator());

        Separators s8 = base.withObjectEmptySeparator("");
        assertNotSame(base, s8);
        assertEquals("", s8.getObjectEmptySeparator());

        Separators s9 = base.withArrayEmptySeparator("");
        assertNotSame(base, s9);
        assertEquals("", s9.getArrayEmptySeparator());
    }

    @Test
    public void rootSeparator_canBeNull() {
        Separators s = new Separators().withRootSeparator(null);

        assertNull(s.getRootSeparator());

        // Other values should remain defaults
        assertEquals(':', s.getObjectFieldValueSeparator());
        assertEquals(',', s.getObjectEntrySeparator());
        assertEquals(',', s.getArrayValueSeparator());
    }

    @Test
    public void spacingEnum_helpers() {
        Separators.Spacing none = Separators.Spacing.NONE;
        Separators.Spacing before = Separators.Spacing.BEFORE;
        Separators.Spacing after = Separators.Spacing.AFTER;
        Separators.Spacing both = Separators.Spacing.BOTH;

        // NONE
        assertEquals("", none.spacesBefore());
        assertEquals("", none.spacesAfter());
        assertEquals("x", none.apply('x'));

        // BEFORE
        assertEquals(" ", before.spacesBefore());
        assertEquals("", before.spacesAfter());
        assertEquals(" ;", before.apply(';'));

        // AFTER
        assertEquals("", after.spacesBefore());
        assertEquals(" ", after.spacesAfter());
        assertEquals(", ", after.apply(','));

        // BOTH
        assertEquals(" ", both.spacesBefore());
        assertEquals(" ", both.spacesAfter());
        assertEquals(" p ", both.apply('p'));
    }

    @Test
    public void objectAndArrayEmptySeparators_defaultToSingleSpace() {
        Separators s = new Separators('z', 'b', 'z');

        assertEquals(" ", s.getObjectEmptySeparator());
        assertEquals(" ", s.getArrayEmptySeparator());
    }

    @Test
    public void chaining_updatesOnlyRequestedParts() {
        Separators s = new Separators()
                .withObjectEntrySeparator(';')
                .withArrayValueSeparator('|');

        assertEquals(';', s.getObjectEntrySeparator());
        assertEquals('|', s.getArrayValueSeparator());

        // Unchanged pieces remain default
        assertEquals(':', s.getObjectFieldValueSeparator());
        assertEquals(Separators.Spacing.BOTH, s.getObjectFieldValueSpacing());
        assertEquals(Separators.Spacing.NONE, s.getArrayValueSpacing());
    }
}