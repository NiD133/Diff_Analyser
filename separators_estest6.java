package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest6 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.BOTH;
        Separators separators0 = new Separators((String) null, 'A', separators_Spacing0, 'A', separators_Spacing0, (String) null, 'A', separators_Spacing0, (String) null);
        Separators separators1 = separators0.withObjectFieldValueSeparator('?');
        assertEquals('A', separators0.getArrayValueSeparator());
        assertEquals('A', separators1.getArrayValueSeparator());
        assertEquals('A', separators0.getObjectEntrySeparator());
        assertEquals('A', separators1.getObjectEntrySeparator());
        assertEquals('?', separators1.getObjectFieldValueSeparator());
    }
}
