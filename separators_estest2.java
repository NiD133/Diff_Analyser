package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest2 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Separators separators0 = new Separators();
        Separators separators1 = separators0.withArrayValueSeparator('(');
        assertEquals(':', separators0.getObjectFieldValueSeparator());
        assertEquals('(', separators1.getArrayValueSeparator());
        assertEquals(',', separators1.getObjectEntrySeparator());
        assertEquals(Separators.Spacing.NONE, separators1.getObjectEntrySpacing());
        assertEquals(',', separators0.getObjectEntrySeparator());
        assertEquals(Separators.Spacing.BOTH, separators1.getObjectFieldValueSpacing());
        assertEquals(Separators.Spacing.NONE, separators1.getArrayValueSpacing());
    }
}
