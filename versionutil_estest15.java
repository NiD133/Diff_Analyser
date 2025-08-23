package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class VersionUtil_ESTestTest15 extends VersionUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        Version version0 = VersionUtil.parseVersion((String) null, (String) null, (String) null);
        assertEquals("", version0.getArtifactId());
    }
}
