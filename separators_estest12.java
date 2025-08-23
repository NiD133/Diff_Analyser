package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest12 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Separators separators0 = new Separators('f', 'f', 'f');
        char char0 = separators0.getObjectEntrySeparator();
        assertEquals('f', separators0.getArrayValueSeparator());
        assertEquals('f', char0);
        assertEquals(Separators.Spacing.NONE, separators0.getObjectEntrySpacing());
        assertEquals(Separators.Spacing.BOTH, separators0.getObjectFieldValueSpacing());
        assertEquals(Separators.Spacing.NONE, separators0.getArrayValueSpacing());
        assertEquals('f', separators0.getObjectFieldValueSeparator());
    }
}
