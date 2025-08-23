package org.apache.commons.jxpath.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ClassLoaderUtil_ESTestTest3 extends ClassLoaderUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test2() throws Throwable {
        try {
            ClassLoaderUtil.getClass("UQR~q#m;I=WyC", true);
            fail("Expecting exception: ClassNotFoundException");
        } catch (ClassNotFoundException e) {
        }
    }
}
