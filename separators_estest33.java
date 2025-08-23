package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest33 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.NONE;
        Separators separators0 = new Separators("", 'J', separators_Spacing0, 'J', separators_Spacing0, "", 'J', separators_Spacing0, "2U#AD9");
        Separators separators1 = separators0.withRootSeparator(" ");
        Separators separators2 = separators1.withRootSeparator(" ");
        assertEquals('J', separators0.getArrayValueSeparator());
        assertNotSame(separators2, separators0);
        assertEquals("2U#AD9", separators2.getArrayEmptySeparator());
        assertSame(separators2, separators1);
        assertEquals('J', separators0.getObjectEntrySeparator());
        assertEquals("", separators2.getObjectEmptySeparator());
        assertEquals('J', separators0.getObjectFieldValueSeparator());
    }
}
