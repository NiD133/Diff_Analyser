package com.google.common.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.text.ParseException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class HostSpecifier_ESTestTest8 extends HostSpecifier_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        // Undeclared exception!
        try {
            HostSpecifier.fromValid("com.google.common.net.HostSpecifier");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Domain name does not have a recognized public suffix: com.google.common.net.HostSpecifier
            //
            verifyException("com.google.common.net.HostSpecifier", e);
        }
    }
}
