package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest1 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.AFTER;
        Separators separators0 = new Separators("", ')', separators_Spacing0, ')', separators_Spacing0, "", 'M', separators_Spacing0, "");
        Separators separators1 = separators0.withArrayValueSeparator('T');
        assertEquals(')', separators0.getObjectEntrySeparator());
        assertEquals(')', separators0.getObjectFieldValueSeparator());
        assertEquals("", separators1.getArrayEmptySeparator());
        assertEquals('T', separators1.getArrayValueSeparator());
        assertEquals("", separators1.getObjectEmptySeparator());
        assertEquals("", separators1.getRootSeparator());
    }
}
