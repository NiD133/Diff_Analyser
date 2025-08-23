package org.apache.commons.jxpath.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ClassLoaderUtil_ESTestTest2 extends ClassLoaderUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test1() throws Throwable {
        Class<Object> class0 = ClassLoaderUtil.getClass("short[]", false);
        assertTrue(class0.isArray());
    }
}
