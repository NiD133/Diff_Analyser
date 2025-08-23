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

public class SerializedCache_ESTestTest8 extends SerializedCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        PerpetualCache perpetualCache0 = new PerpetualCache((String) null);
        SerializedCache serializedCache0 = new SerializedCache(perpetualCache0);
        String string0 = serializedCache0.getId();
        assertNull(string0);
    }
}
