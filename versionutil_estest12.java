package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class VersionUtil_ESTestTest12 extends VersionUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Version version0 = VersionUtil.parseVersion("yf;Hr?-6", "yf;Hr?-6", "yf;Hr?-6");
        assertEquals(6, version0.getPatchLevel());
        assertEquals(0, version0.getMajorVersion());
    }
}
