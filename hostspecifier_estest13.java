package com.google.common.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.text.ParseException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class HostSpecifier_ESTestTest13 extends HostSpecifier_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        HostSpecifier hostSpecifier0 = HostSpecifier.from("127.0.0.1");
        Object object0 = new Object();
        boolean boolean0 = hostSpecifier0.equals(object0);
        assertFalse(boolean0);
    }
}