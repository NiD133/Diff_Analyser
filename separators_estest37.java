package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest37 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test36() throws Throwable {
        Separators separators0 = new Separators('(', '(', '(');
        Separators.Spacing separators_Spacing0 = separators0.getArrayValueSpacing();
        String string0 = separators_Spacing0.spacesBefore();
        assertEquals("", string0);
        assertEquals(Separators.Spacing.BOTH, separators0.getObjectFieldValueSpacing());
        assertEquals('(', separators0.getObjectEntrySeparator());
        assertEquals(Separators.Spacing.NONE, separators0.getObjectEntrySpacing());
        assertEquals('(', separators0.getArrayValueSeparator());
        assertEquals('(', separators0.getObjectFieldValueSeparator());
    }
}
