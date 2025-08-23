package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IOCase_ESTestTest10 extends IOCase_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        IOCase iOCase0 = IOCase.INSENSITIVE;
        int int0 = iOCase0.checkIndexOf("\"93F*1y", (-794), "\"93F*1y");
        assertEquals(0, int0);
    }
}
