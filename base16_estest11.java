package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Base16_ESTestTest11 extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        Base16 base16_0 = new Base16(false);
        assertEquals(64, BaseNCodec.PEM_CHUNK_SIZE);
    }
}
