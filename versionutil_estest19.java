package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class VersionUtil_ESTestTest19 extends VersionUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        Class<Object> class0 = Object.class;
        Version version0 = VersionUtil.packageVersionFor(class0);
        assertTrue(version0.isUknownVersion());
    }
}
