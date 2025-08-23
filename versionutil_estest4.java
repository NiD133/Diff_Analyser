package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class VersionUtil_ESTestTest4 extends VersionUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        int int0 = VersionUtil.parseVersionPart("3$Qtt~IU2x^1~fgh4]+");
        assertEquals(3, int0);
    }
}
