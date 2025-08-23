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

public class SerializationUtils_ESTestTest7 extends SerializationUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Locale locale0 = Locale.JAPANESE;
        byte[] byteArray0 = SerializationUtils.serialize((Serializable) locale0);
        ByteArrayInputStream byteArrayInputStream0 = new ByteArrayInputStream(byteArray0);
        Object object0 = SerializationUtils.deserialize((InputStream) byteArrayInputStream0);
        assertSame(object0, locale0);
    }
}
