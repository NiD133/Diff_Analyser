package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PushbackInputStream;
import java.io.SequenceInputStream;
import java.util.Enumeration;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class CodecEncoding_ESTestTest16 extends CodecEncoding_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        BHSDCodec bHSDCodec0 = Codec.MDELTA5;
        PopulationCodec populationCodec0 = new PopulationCodec(bHSDCodec0, bHSDCodec0, bHSDCodec0);
        int[] intArray0 = CodecEncoding.getSpecifier(populationCodec0, bHSDCodec0);
        assertEquals(2, intArray0.length);
    }
}
