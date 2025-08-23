package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest24 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.NONE;
        Separators separators0 = new Separators("", 'J', separators_Spacing0, 'J', separators_Spacing0, "", 'J', separators_Spacing0, "2U#AD9");
        Separators separators1 = separators0.withObjectEmptySeparator("");
        assertSame(separators1, separators0);
        assertEquals('J', separators1.getObjectFieldValueSeparator());
        assertEquals("", separators1.getRootSeparator());
        assertEquals('J', separators1.getObjectEntrySeparator());
        assertEquals("2U#AD9", separators1.getArrayEmptySeparator());
        assertEquals('J', separators1.getArrayValueSeparator());
    }
}
