package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest38 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test37() throws Throwable {
        Separators separators0 = Separators.createDefaultInstance();
        separators0.getRootSeparator();
        assertEquals(Separators.Spacing.NONE, separators0.getObjectEntrySpacing());
        assertEquals(':', separators0.getObjectFieldValueSeparator());
        assertEquals(',', separators0.getObjectEntrySeparator());
        assertEquals(Separators.Spacing.NONE, separators0.getArrayValueSpacing());
        assertEquals(Separators.Spacing.BOTH, separators0.getObjectFieldValueSpacing());
        assertEquals(',', separators0.getArrayValueSeparator());
    }
}
