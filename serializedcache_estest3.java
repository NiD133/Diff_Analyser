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

public class SerializedCache_ESTestTest3 extends SerializedCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Enumeration<MockFileInputStream> enumeration0 = (Enumeration<MockFileInputStream>) mock(Enumeration.class, new ViolatedAssumptionAnswer());
        doReturn(false).when(enumeration0).hasMoreElements();
        SequenceInputStream sequenceInputStream0 = new SequenceInputStream(enumeration0);
        SerializedCache.CustomObjectInputStream serializedCache_CustomObjectInputStream0 = null;
        try {
            serializedCache_CustomObjectInputStream0 = new SerializedCache.CustomObjectInputStream(sequenceInputStream0);
            fail("Expecting exception: EOFException");
        } catch (Throwable e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.io.ObjectInputStream$PeekInputStream", e);
        }
    }
}
