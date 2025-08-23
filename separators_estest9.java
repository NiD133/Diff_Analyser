package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest9 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        Separators separators0 = Separators.createDefaultInstance();
        Separators separators1 = separators0.withObjectFieldValueSeparator('8');
        char char0 = separators1.getObjectFieldValueSeparator();
        assertEquals(Separators.Spacing.NONE, separators1.getObjectEntrySpacing());
        assertEquals(',', separators0.getObjectEntrySeparator());
        assertEquals('8', char0);
        assertEquals(Separators.Spacing.BOTH, separators1.getObjectFieldValueSpacing());
        assertEquals(',', separators0.getArrayValueSeparator());
        assertEquals(Separators.Spacing.NONE, separators1.getArrayValueSpacing());
    }
}
