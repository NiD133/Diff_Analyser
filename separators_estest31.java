package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest31 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.NONE;
        Separators separators0 = new Separators("", 'J', separators_Spacing0, 'J', separators_Spacing0, "", 'J', separators_Spacing0, "2U#AD9");
        Separators separators1 = separators0.withObjectFieldValueSeparator('J');
        assertSame(separators1, separators0);
        assertEquals("", separators1.getRootSeparator());
        assertEquals("2U#AD9", separators1.getArrayEmptySeparator());
        assertEquals('J', separators1.getArrayValueSeparator());
        assertEquals("", separators1.getObjectEmptySeparator());
        assertEquals('J', separators1.getObjectEntrySeparator());
    }
}
