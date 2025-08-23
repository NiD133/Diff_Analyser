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

public class SerializedCache_ESTestTest26 extends SerializedCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        PerpetualCache perpetualCache0 = new PerpetualCache("");
        SerializedCache serializedCache0 = new SerializedCache(perpetualCache0);
        SerializedCache serializedCache1 = new SerializedCache(serializedCache0);
        serializedCache1.putObject((Object) null, (Object) null);
        Object object0 = serializedCache0.getObject((Object) null);
        assertNotNull(object0);
    }
}
