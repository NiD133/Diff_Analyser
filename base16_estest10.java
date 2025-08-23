package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Base16_ESTestTest10 extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Base16 base16_0 = new Base16();
        byte[] byteArray0 = new byte[5];
        BaseNCodec.Context baseNCodec_Context0 = new BaseNCodec.Context();
        base16_0.encode(byteArray0, 2147483639, 484, baseNCodec_Context0);
        assertEquals(76, BaseNCodec.MIME_CHUNK_SIZE);
    }
}
