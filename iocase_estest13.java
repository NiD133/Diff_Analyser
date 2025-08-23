package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IOCase_ESTestTest13 extends IOCase_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        IOCase iOCase0 = IOCase.INSENSITIVE;
        // Undeclared exception!
        iOCase0.checkIndexOf("H{o(4Kq#L?M", (-70537784), "H{o(4Kq#L?M");
    }
}
