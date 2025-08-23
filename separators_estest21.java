package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest21 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.AFTER;
        Separators separators0 = new Separators("oRb)u^~$[*+b+7&Gu", 'D', separators_Spacing0, 'y', separators_Spacing0, "oRb)u^~$[*+b+7&Gu", '1', separators_Spacing0, "com.fasterxml.jackson.core.util.Separators$Spacing");
        Separators separators1 = separators0.withArrayValueSpacing(separators_Spacing0);
        assertEquals("com.fasterxml.jackson.core.util.Separators$Spacing", separators1.getArrayEmptySeparator());
        assertEquals('y', separators1.getObjectEntrySeparator());
        assertEquals("oRb)u^~$[*+b+7&Gu", separators1.getRootSeparator());
        assertEquals('D', separators1.getObjectFieldValueSeparator());
        assertEquals('1', separators1.getArrayValueSeparator());
        assertEquals("oRb)u^~$[*+b+7&Gu", separators1.getObjectEmptySeparator());
        assertSame(separators1, separators0);
    }
}
