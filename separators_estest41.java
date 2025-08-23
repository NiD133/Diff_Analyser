package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest41 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.AFTER;
        Separators separators0 = new Separators("", ')', separators_Spacing0, ')', separators_Spacing0, "", 'M', separators_Spacing0, "");
        String string0 = separators0.getArrayEmptySeparator();
        assertEquals("", string0);
        assertEquals('M', separators0.getArrayValueSeparator());
        assertEquals(')', separators0.getObjectEntrySeparator());
        assertEquals("", separators0.getRootSeparator());
        assertEquals("", separators0.getObjectEmptySeparator());
        assertEquals(')', separators0.getObjectFieldValueSeparator());
    }
}
