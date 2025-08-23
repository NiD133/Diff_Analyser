package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest11 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        Separators separators0 = new Separators();
        char char0 = separators0.getObjectEntrySeparator();
        assertEquals(',', separators0.getArrayValueSeparator());
        assertEquals(Separators.Spacing.BOTH, separators0.getObjectFieldValueSpacing());
        assertEquals(Separators.Spacing.NONE, separators0.getArrayValueSpacing());
        assertEquals(':', separators0.getObjectFieldValueSeparator());
        assertEquals(Separators.Spacing.NONE, separators0.getObjectEntrySpacing());
        assertEquals(',', char0);
    }
}
