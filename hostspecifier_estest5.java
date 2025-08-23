package com.google.common.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.text.ParseException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class HostSpecifier_ESTestTest5 extends HostSpecifier_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        // Undeclared exception!
        try {
            HostSpecifier.fromValid("::");
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            //
            // java.net.UnknownHostException: Not IPv4: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
            //
        }
    }
}
