package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ByteArrayBuilder_ESTestTest71 extends ByteArrayBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test70() throws Throwable {
        ByteArrayBuilder byteArrayBuilder0 = new ByteArrayBuilder(131050);
        ByteArrayBuilder byteArrayBuilder1 = ByteArrayBuilder.fromInitial(byteArrayBuilder0.NO_BYTES, (-219));
        int int0 = byteArrayBuilder1.size();
        assertEquals((-219), int0);
    }
}
