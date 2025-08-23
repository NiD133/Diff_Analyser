package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class VersionUtil_ESTestTest2 extends VersionUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        int int0 = VersionUtil.parseVersionPart("9c*fdp6?ec}ur$$");
        assertEquals(9, int0);
    }
}
