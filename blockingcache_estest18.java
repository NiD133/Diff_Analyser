package org.apache.ibatis.cache.decorators;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.concurrent.CountDownLatch;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BlockingCache_ESTestTest18 extends BlockingCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        PerpetualCache perpetualCache0 = new PerpetualCache("Detected an attempt at releasing unacquired lock. This should never happen.");
        SoftCache softCache0 = new SoftCache(perpetualCache0);
        BlockingCache blockingCache0 = new BlockingCache(softCache0);
        perpetualCache0.putObject("Detected an attempt at releasing unacquired lock. This should never happen.", "Detected an attempt at releasing unacquired lock. This should never happen.");
        // Undeclared exception!
        try {
            blockingCache0.getObject("Detected an attempt at releasing unacquired lock. This should never happen.");
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // class java.lang.String cannot be cast to class java.lang.ref.SoftReference (java.lang.String and java.lang.ref.SoftReference are in module java.base of loader 'bootstrap')
            //
            verifyException("org.apache.ibatis.cache.decorators.SoftCache", e);
        }
    }
}
