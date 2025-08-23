package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest30 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.NONE;
        Separators separators0 = new Separators("", 'J', separators_Spacing0, 'J', separators_Spacing0, "", 'J', separators_Spacing0, "2U#AD9");
        Separators.Spacing separators_Spacing1 = Separators.Spacing.AFTER;
        Separators separators1 = separators0.withObjectFieldValueSpacing(separators_Spacing1);
        assertEquals('J', separators0.getObjectFieldValueSeparator());
        assertEquals("2U#AD9", separators1.getArrayEmptySeparator());
        assertEquals("", separators1.getObjectEmptySeparator());
        assertEquals(Separators.Spacing.NONE, separators1.getArrayValueSpacing());
        assertEquals(Separators.Spacing.NONE, separators1.getObjectEntrySpacing());
        assertEquals("", separators1.getRootSeparator());
        assertEquals('J', separators0.getArrayValueSeparator());
        assertEquals(Separators.Spacing.AFTER, separators1.getObjectFieldValueSpacing());
        assertEquals('J', separators0.getObjectEntrySeparator());
    }
}
