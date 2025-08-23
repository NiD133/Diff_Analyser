package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest34 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        Separators separators0 = Separators.createDefaultInstance();
        Separators separators1 = separators0.withRootSeparator((String) null);
        String string0 = separators1.getRootSeparator();
        assertEquals(',', separators0.getObjectEntrySeparator());
        assertNull(string0);
        assertEquals(Separators.Spacing.NONE, separators1.getArrayValueSpacing());
        assertEquals(':', separators0.getObjectFieldValueSeparator());
        assertEquals(Separators.Spacing.BOTH, separators1.getObjectFieldValueSpacing());
        assertEquals(',', separators0.getArrayValueSeparator());
        assertEquals(Separators.Spacing.NONE, separators1.getObjectEntrySpacing());
    }
}
