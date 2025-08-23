package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest26 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        Separators separators0 = Separators.createDefaultInstance();
        Separators.Spacing separators_Spacing0 = Separators.Spacing.NONE;
        Separators separators1 = separators0.withObjectEntrySpacing(separators_Spacing0);
        assertEquals(',', separators1.getObjectEntrySeparator());
        assertEquals(',', separators1.getArrayValueSeparator());
        assertEquals(Separators.Spacing.NONE, separators1.getArrayValueSpacing());
        assertEquals(':', separators1.getObjectFieldValueSeparator());
        assertEquals(Separators.Spacing.BOTH, separators1.getObjectFieldValueSpacing());
        assertSame(separators1, separators0);
    }
}
