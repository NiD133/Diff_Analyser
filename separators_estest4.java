package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest4 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        Separators separators0 = new Separators('z', 'b', 'z');
        Separators separators1 = separators0.withRootSeparator("");
        Separators.Spacing separators_Spacing0 = Separators.Spacing.AFTER;
        Separators separators2 = separators1.withObjectEntrySpacing(separators_Spacing0);
        assertEquals(Separators.Spacing.AFTER, separators2.getObjectEntrySpacing());
        assertEquals(" ", separators2.getArrayEmptySeparator());
        assertEquals(" ", separators2.getObjectEmptySeparator());
        assertEquals(Separators.Spacing.BOTH, separators2.getObjectFieldValueSpacing());
        assertEquals(Separators.Spacing.NONE, separators2.getArrayValueSpacing());
        assertEquals('z', separators2.getObjectFieldValueSeparator());
        assertEquals("", separators2.getRootSeparator());
        assertEquals('z', separators2.getArrayValueSeparator());
        assertEquals('b', separators2.getObjectEntrySeparator());
    }
}
