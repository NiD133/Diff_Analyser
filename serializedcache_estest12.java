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

public class SerializedCache_ESTestTest12 extends SerializedCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        PerpetualCache perpetualCache0 = new PerpetualCache("org.apache.ibatis.exceptions.PersistenceException");
        LruCache lruCache0 = new LruCache(perpetualCache0);
        ScheduledCache scheduledCache0 = new ScheduledCache(lruCache0);
        WeakCache weakCache0 = new WeakCache(scheduledCache0);
        SerializedCache serializedCache0 = new SerializedCache(weakCache0);
        boolean boolean0 = serializedCache0.equals(perpetualCache0);
        assertFalse(boolean0);
    }
}
