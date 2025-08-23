package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class VersionUtil_ESTestTest10 extends VersionUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Version version0 = VersionUtil.parseVersion("iQT[E/73*QBf", "iQT[E/73*QBf", "iQT[E/73*QBf");
        assertEquals(0, version0.getMajorVersion());
        assertEquals(73, version0.getMinorVersion());
        assertEquals(0, version0.getPatchLevel());
    }
}
