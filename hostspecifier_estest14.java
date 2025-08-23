package com.google.common.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.text.ParseException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class HostSpecifier_ESTestTest14 extends HostSpecifier_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        try {
            HostSpecifier.from("jR:");
            fail("Expecting exception: ParseException");
        } catch (ParseException e) {
            //
            // Invalid host specifier: jR:
            //
            verifyException("com.google.common.net.HostSpecifier", e);
        }
    }
}
