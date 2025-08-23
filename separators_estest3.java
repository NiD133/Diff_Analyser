package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest3 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.AFTER;
        Separators.Spacing separators_Spacing1 = Separators.Spacing.BEFORE;
        Separators separators0 = new Separators("", ')', separators_Spacing0, ')', separators_Spacing1, "", 'M', separators_Spacing0, "");
        Separators separators1 = separators0.withObjectEntrySpacing(separators_Spacing0);
        assertEquals("", separators1.getObjectEmptySeparator());
        assertEquals(')', separators0.getObjectEntrySeparator());
        assertEquals(Separators.Spacing.AFTER, separators0.getObjectFieldValueSpacing());
        assertEquals("", separators1.getRootSeparator());
        assertEquals(Separators.Spacing.AFTER, separators0.getArrayValueSpacing());
        assertEquals(')', separators0.getObjectFieldValueSeparator());
        assertEquals("", separators1.getArrayEmptySeparator());
        assertEquals('M', separators0.getArrayValueSeparator());
        assertNotSame(separators1, separators0);
    }
}
