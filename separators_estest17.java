package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest17 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.AFTER;
        Separators separators0 = new Separators("oRb)u^~$[*+b+7&Gu", 'D', separators_Spacing0, 'y', separators_Spacing0, "oRb)u^~$[*+b+7&Gu", '1', separators_Spacing0, "com.fasterxml.jackson.core.util.Separators$Spacing");
        String string0 = separators0.getArrayEmptySeparator();
        assertEquals('1', separators0.getArrayValueSeparator());
        assertEquals('y', separators0.getObjectEntrySeparator());
        assertEquals("oRb)u^~$[*+b+7&Gu", separators0.getObjectEmptySeparator());
        assertEquals('D', separators0.getObjectFieldValueSeparator());
        assertEquals("com.fasterxml.jackson.core.util.Separators$Spacing", string0);
        assertEquals("oRb)u^~$[*+b+7&Gu", separators0.getRootSeparator());
    }
}
