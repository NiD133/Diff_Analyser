package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class VersionUtil_ESTestTest9 extends VersionUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        Class<JsonFactory> class0 = JsonFactory.class;
        Version version0 = VersionUtil.versionFor(class0);
        assertEquals(0, version0.getPatchLevel());
    }
}
