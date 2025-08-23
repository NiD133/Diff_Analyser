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

public class SerializedCache_ESTestTest23 extends SerializedCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        PerpetualCache perpetualCache0 = new PerpetualCache("");
        perpetualCache0.putObject("", "");
        SerializedCache serializedCache0 = new SerializedCache(perpetualCache0);
        // Undeclared exception!
        try {
            serializedCache0.getObject("");
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // class java.lang.String cannot be cast to class [B (java.lang.String and [B are in module java.base of loader 'bootstrap')
            //
            verifyException("org.apache.ibatis.cache.decorators.SerializedCache", e);
        }
    }
}
