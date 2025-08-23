package org.apache.commons.jxpath.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ClassLoaderUtil_ESTestTest4 extends ClassLoaderUtil_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test3() throws Throwable {
        Class<String> class0 = ClassLoaderUtil.getClass("long", true);
        assertFalse(class0.isAnnotation());
    }
}
