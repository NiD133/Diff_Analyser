package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest43 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.AFTER;
        Separators separators0 = new Separators("IS:B2Ea", 'V', separators_Spacing0, '9', separators_Spacing0, '9', separators_Spacing0);
        char char0 = separators0.getObjectEntrySeparator();
        assertEquals('V', separators0.getObjectFieldValueSeparator());
        assertEquals(" ", separators0.getObjectEmptySeparator());
        assertEquals('9', char0);
        assertEquals(" ", separators0.getArrayEmptySeparator());
        assertEquals('9', separators0.getArrayValueSeparator());
        assertEquals("IS:B2Ea", separators0.getRootSeparator());
    }
}
