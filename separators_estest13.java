package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest13 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.NONE;
        Separators separators0 = new Separators((String) null, '6', separators_Spacing0, 'E', separators_Spacing0, (String) null, '3', separators_Spacing0, (String) null);
        String string0 = separators0.getObjectEmptySeparator();
        assertEquals('6', separators0.getObjectFieldValueSeparator());
        assertEquals('E', separators0.getObjectEntrySeparator());
        assertEquals('3', separators0.getArrayValueSeparator());
        assertNull(string0);
    }
}
