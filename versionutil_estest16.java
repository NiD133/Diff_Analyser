package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class VersionUtil_ESTestTest16 extends VersionUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        ClassLoader classLoader0 = ClassLoader.getSystemClassLoader();
        Version version0 = VersionUtil.mavenVersionFor(classLoader0, "[][.~r)|i/l/", "Ve");
        assertEquals(0, version0.getMajorVersion());
    }
}
