package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest5 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Years years0 = Years.THREE;
        Years years1 = years0.plus((Years) null);
        assertSame(years1, years0);
    }
}
