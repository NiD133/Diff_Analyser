package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest35 extends Separators_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        Separators.Spacing separators_Spacing0 = Separators.Spacing.BOTH;
        String string0 = separators_Spacing0.spacesAfter();
        assertEquals(" ", string0);
    }
}
