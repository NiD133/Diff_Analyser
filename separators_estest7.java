package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest7 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.AFTER;
        Separators separators0 = new Separators("U?B$vB\"1@5", 'V', separators_Spacing0, 'V', separators_Spacing0, '7', separators_Spacing0);
        Separators separators1 = separators0.withObjectFieldValueSeparator('O');
        assertEquals('V', separators1.getObjectEntrySeparator());
        assertEquals('V', separators0.getObjectEntrySeparator());
        assertEquals('O', separators1.getObjectFieldValueSeparator());
        assertEquals('7', separators0.getArrayValueSeparator());
    }
}
