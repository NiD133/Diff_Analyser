package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest32 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        Separators separators0 = Separators.createDefaultInstance();
        Separators separators1 = separators0.withObjectFieldValueSeparator('|');
        assertEquals('|', separators1.getObjectFieldValueSeparator());
        assertEquals(',', separators0.getArrayValueSeparator());
        assertEquals(Separators.Spacing.NONE, separators1.getObjectEntrySpacing());
        assertEquals(Separators.Spacing.NONE, separators1.getArrayValueSpacing());
        assertEquals(Separators.Spacing.BOTH, separators1.getObjectFieldValueSpacing());
        assertEquals(',', separators0.getObjectEntrySeparator());
    }
}
