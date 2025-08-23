package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest28 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        Separators separators0 = Separators.createDefaultInstance();
        Separators separators1 = separators0.withObjectEntrySeparator('e');
        assertEquals(',', separators1.getArrayValueSeparator());
        assertEquals(',', separators0.getArrayValueSeparator());
        assertEquals('e', separators1.getObjectEntrySeparator());
        assertEquals(Separators.Spacing.NONE, separators1.getObjectEntrySpacing());
        assertEquals(Separators.Spacing.BOTH, separators1.getObjectFieldValueSpacing());
        assertEquals(Separators.Spacing.NONE, separators1.getArrayValueSpacing());
        assertEquals(':', separators0.getObjectFieldValueSeparator());
    }
}