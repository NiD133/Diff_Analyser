package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.commons.codec.CodecPolicy;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BCodec_ESTestTest7 extends BCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        BCodec bCodec0 = new BCodec();
        byte[] byteArray0 = new byte[6];
        byte[] byteArray1 = bCodec0.doDecoding(byteArray0);
        byte[] byteArray2 = bCodec0.doEncoding(byteArray1);
        assertNotSame(byteArray2, byteArray0);
    }
}
