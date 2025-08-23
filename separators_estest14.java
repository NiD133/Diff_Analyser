package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest14 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.AFTER;
        Separators separators0 = new Separators("U?B$vB\"1@5", 'V', separators_Spacing0, 'V', separators_Spacing0, '7', separators_Spacing0);
        char char0 = separators0.getArrayValueSeparator();
        assertEquals('7', char0);
        assertEquals('V', separators0.getObjectFieldValueSeparator());
        assertEquals('V', separators0.getObjectEntrySeparator());
    }
}
