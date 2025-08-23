package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.SequenceInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.junit.runner.RunWith;

public class SerializationUtils_ESTestTest8 extends SerializationUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        PipedOutputStream pipedOutputStream0 = new PipedOutputStream();
        Class<Object> class0 = Object.class;
        // Undeclared exception!
        try {
            SerializationUtils.serialize((Serializable) class0, (OutputStream) pipedOutputStream0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // java.io.IOException: Pipe not connected
            //
            verifyException("org.apache.commons.lang3.SerializationUtils", e);
        }
    }
}
