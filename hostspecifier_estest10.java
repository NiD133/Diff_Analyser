package com.google.common.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.text.ParseException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class HostSpecifier_ESTestTest10 extends HostSpecifier_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        HostSpecifier hostSpecifier0 = HostSpecifier.fromValid("127.0.0.1");
        assertEquals("127.0.0.1", hostSpecifier0.toString());
    }
}
