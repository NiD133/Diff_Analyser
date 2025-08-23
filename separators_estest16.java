package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest16 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.BOTH;
        Separators separators0 = new Separators((String) null, 'A', separators_Spacing0, 'A', separators_Spacing0, (String) null, 'A', separators_Spacing0, (String) null);
        String string0 = separators0.getArrayEmptySeparator();
        assertEquals('A', separators0.getObjectEntrySeparator());
        assertEquals('A', separators0.getObjectFieldValueSeparator());
        assertNull(string0);
        assertEquals('A', separators0.getArrayValueSeparator());
    }
}
