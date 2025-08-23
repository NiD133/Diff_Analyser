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

public class SerializedCache_ESTestTest28 extends SerializedCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        SerializedCache serializedCache0 = new SerializedCache((Cache) null);
        // Undeclared exception!
        try {
            serializedCache0.putObject((Object) null, (Object) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.ibatis.cache.decorators.SerializedCache", e);
        }
    }
}
