package org.apache.ibatis.cache.decorators;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.SequenceInputStream;
import java.util.Enumeration;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import org.junit.runner.RunWith;

public class SerializedCache_ESTestTest15 extends SerializedCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        PerpetualCache perpetualCache0 = new PerpetualCache("org.apache.ibatis.cache.decorators.SerializedCache$CustomObjectInputStream");
        BlockingCache blockingCache0 = new BlockingCache(perpetualCache0);
        TransactionalCache transactionalCache0 = new TransactionalCache(blockingCache0);
        SerializedCache serializedCache0 = new SerializedCache(blockingCache0);
        // Undeclared exception!
        try {
            serializedCache0.removeObject(transactionalCache0);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Detected an attempt at releasing unacquired lock. This should never happen.
            //
            verifyException("org.apache.ibatis.cache.decorators.BlockingCache", e);
        }
    }
}
