package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.util.Separators;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class Separators_ESTest extends Separators_ESTest_scaffolding {

    private static final char DEFAULT_OBJECT_FIELD_VALUE_SEPARATOR = ':';
    private static final char DEFAULT_OBJECT_ENTRY_SEPARATOR = ',';
    private static final char DEFAULT_ARRAY_VALUE_SEPARATOR = ',';
    private static final Separators.Spacing DEFAULT_OBJECT_FIELD_VALUE_SPACING = Separators.Spacing.BOTH;
    private static final Separators.Spacing DEFAULT_ARRAY_VALUE_SPACING = Separators.Spacing.NONE;
    private static final Separators.Spacing DEFAULT_OBJECT_ENTRY_SPACING = Separators.Spacing.NONE;

    @Test(timeout = 4000)
    public void testWithArrayValueSeparator() throws Throwable {
        Separators separators = new Separators();
        Separators updatedSeparators = separators.withArrayValueSeparator('T');

        // Verify original separators
        assertEquals(DEFAULT_OBJECT_FIELD_VALUE_SEPARATOR, separators.getObjectFieldValueSeparator());
        assertEquals(DEFAULT_OBJECT_ENTRY_SEPARATOR, separators.getObjectEntrySeparator());
        assertEquals(DEFAULT_ARRAY_VALUE_SEPARATOR, separators.getArrayValueSeparator());

        // Verify updated separators
        assertEquals('T', updatedSeparators.getArrayValueSeparator());
    }

    @Test(timeout = 4000)
    public void testWithObjectEntrySpacing() throws Throwable {
        Separators separators = new Separators();
        Separators updatedSeparators = separators.withObjectEntrySpacing(Separators.Spacing.AFTER);

        // Verify original separators
        assertEquals(DEFAULT_OBJECT_ENTRY_SPACING, separators.getObjectEntrySpacing());

        // Verify updated separators
        assertEquals(Separators.Spacing.AFTER, updatedSeparators.getObjectEntrySpacing());
    }

    @Test(timeout = 4000)
    public void testWithRootSeparator() throws Throwable {
        Separators separators = new Separators();
        Separators updatedSeparators = separators.withRootSeparator(" ");

        // Verify original separators
        assertNull(separators.getRootSeparator());

        // Verify updated separators
        assertEquals(" ", updatedSeparators.getRootSeparator());
    }

    @Test(timeout = 4000)
    public void testWithObjectFieldValueSeparator() throws Throwable {
        Separators separators = new Separators();
        Separators updatedSeparators = separators.withObjectFieldValueSeparator('|');

        // Verify original separators
        assertEquals(DEFAULT_OBJECT_FIELD_VALUE_SEPARATOR, separators.getObjectFieldValueSeparator());

        // Verify updated separators
        assertEquals('|', updatedSeparators.getObjectFieldValueSeparator());
    }

    @Test(timeout = 4000)
    public void testWithArrayEmptySeparator() throws Throwable {
        Separators separators = new Separators();
        Separators updatedSeparators = separators.withArrayEmptySeparator("[]");

        // Verify original separators
        assertEquals(Separators.DEFAULT_ARRAY_EMPTY_SEPARATOR, separators.getArrayEmptySeparator());

        // Verify updated separators
        assertEquals("[]", updatedSeparators.getArrayEmptySeparator());
    }

    @Test(timeout = 4000)
    public void testWithObjectEmptySeparator() throws Throwable {
        Separators separators = new Separators();
        Separators updatedSeparators = separators.withObjectEmptySeparator("{}");

        // Verify original separators
        assertEquals(Separators.DEFAULT_OBJECT_EMPTY_SEPARATOR, separators.getObjectEmptySeparator());

        // Verify updated separators
        assertEquals("{}", updatedSeparators.getObjectEmptySeparator());
    }

    @Test(timeout = 4000)
    public void testDefaultSeparators() throws Throwable {
        Separators separators = Separators.createDefaultInstance();

        // Verify default separators
        assertEquals(DEFAULT_OBJECT_FIELD_VALUE_SEPARATOR, separators.getObjectFieldValueSeparator());
        assertEquals(DEFAULT_OBJECT_ENTRY_SEPARATOR, separators.getObjectEntrySeparator());
        assertEquals(DEFAULT_ARRAY_VALUE_SEPARATOR, separators.getArrayValueSeparator());
        assertEquals(DEFAULT_OBJECT_FIELD_VALUE_SPACING, separators.getObjectFieldValueSpacing());
        assertEquals(DEFAULT_ARRAY_VALUE_SPACING, separators.getArrayValueSpacing());
        assertEquals(DEFAULT_OBJECT_ENTRY_SPACING, separators.getObjectEntrySpacing());
    }
}