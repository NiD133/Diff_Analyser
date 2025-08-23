package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest10 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.NONE;
        Separators separators0 = new Separators("", 'j', separators_Spacing0, 'j', separators_Spacing0, "nCF", 'W', separators_Spacing0, "nCF");
        char char0 = separators0.getObjectFieldValueSeparator();
        assertEquals("nCF", separators0.getObjectEmptySeparator());
        assertEquals('W', separators0.getArrayValueSeparator());
        assertEquals('j', char0);
        assertEquals("", separators0.getRootSeparator());
        assertEquals("nCF", separators0.getArrayEmptySeparator());
        assertEquals('j', separators0.getObjectEntrySeparator());
    }
}
