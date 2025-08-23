package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest40 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.NONE;
        Separators separators0 = new Separators("", 'J', separators_Spacing0, 'J', separators_Spacing0, "", 'J', separators_Spacing0, "2U#AD9");
        separators0.getObjectEntrySpacing();
        assertEquals("", separators0.getObjectEmptySeparator());
        assertEquals("", separators0.getRootSeparator());
        assertEquals('J', separators0.getArrayValueSeparator());
        assertEquals("2U#AD9", separators0.getArrayEmptySeparator());
        assertEquals('J', separators0.getObjectFieldValueSeparator());
        assertEquals('J', separators0.getObjectEntrySeparator());
    }
}
