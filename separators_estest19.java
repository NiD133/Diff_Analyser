package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest19 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.NONE;
        Separators separators0 = new Separators((String) null, '6', separators_Spacing0, 'E', separators_Spacing0, (String) null, '3', separators_Spacing0, (String) null);
        Separators separators1 = separators0.withArrayEmptySeparator((String) null);
        assertEquals('E', separators1.getObjectEntrySeparator());
        assertSame(separators1, separators0);
        assertEquals('3', separators1.getArrayValueSeparator());
        assertEquals('6', separators1.getObjectFieldValueSeparator());
    }
}
