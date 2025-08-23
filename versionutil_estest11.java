package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class VersionUtil_ESTestTest11 extends VersionUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        Version version0 = VersionUtil.parseVersion("com.fasterxml.jackson.core.util.VersionUtil", "com.fasterxml.jackson.core.util.VersionUtil", "com.fasterxml.jackson.core.util.VersionUtil");
        assertTrue(version0.isSnapshot());
        assertEquals(0, version0.getMinorVersion());
    }
}
