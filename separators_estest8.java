package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest8 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.BOTH;
        Separators separators0 = new Separators("", 'a', separators_Spacing0, 'O', separators_Spacing0, "OA)D?vv7:1U", 'a', separators_Spacing0, "OA)D?vv7:1U");
        String string0 = separators0.getRootSeparator();
        assertEquals('a', separators0.getArrayValueSeparator());
        assertEquals("OA)D?vv7:1U", separators0.getObjectEmptySeparator());
        assertEquals('O', separators0.getObjectEntrySeparator());
        assertEquals("OA)D?vv7:1U", separators0.getArrayEmptySeparator());
        assertEquals("", string0);
        assertEquals('a', separators0.getObjectFieldValueSeparator());
    }
}
