package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class VersionUtil_ESTestTest17 extends VersionUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        // Undeclared exception!
        try {
            VersionUtil.throwInternalReturnAny();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Internal error: this code path should never get executed
            //
            verifyException("com.fasterxml.jackson.core.util.VersionUtil", e);
        }
    }
}
