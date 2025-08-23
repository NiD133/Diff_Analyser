package com.google.common.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.text.ParseException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class HostSpecifier_ESTestTest1 extends HostSpecifier_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        boolean boolean0 = HostSpecifier.isValid("0.0.0.0");
        assertTrue(boolean0);
    }
}
