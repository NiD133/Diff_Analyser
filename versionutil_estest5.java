package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class VersionUtil_ESTestTest5 extends VersionUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Version version0 = VersionUtil.parseVersion("8juvlng,dux%Z/Gd4", "8juvlng,dux%Z/Gd4", "8juvlng,dux%Z/Gd4");
        assertEquals(0, version0.getPatchLevel());
        assertEquals(0, version0.getMinorVersion());
        assertEquals(8, version0.getMajorVersion());
    }
}
