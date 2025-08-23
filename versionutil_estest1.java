package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class VersionUtil_ESTestTest1 extends VersionUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        int int0 = VersionUtil.parseVersionPart("0T*A{.*6/lD:1tm E_");
        assertEquals(0, int0);
    }
}
