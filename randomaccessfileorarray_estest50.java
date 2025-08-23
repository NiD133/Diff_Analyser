package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.io.GetBufferedRandomAccessSource;
import com.itextpdf.text.io.IndependentRandomAccessSource;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.WindowRandomAccessSource;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.net.URL;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.net.MockURL;
import org.evosuite.runtime.testdata.EvoSuiteFile;
import org.evosuite.runtime.testdata.FileSystemHandling;
import org.junit.runner.RunWith;

public class RandomAccessFileOrArray_ESTestTest50 extends RandomAccessFileOrArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test049() throws Throwable {
        byte[] byteArray0 = new byte[4];
        byteArray0[0] = (byte) (-16);
        byteArray0[1] = (byte) 6;
        RandomAccessFileOrArray randomAccessFileOrArray0 = new RandomAccessFileOrArray(byteArray0);
        char char0 = randomAccessFileOrArray0.readCharLE();
        assertEquals(2L, randomAccessFileOrArray0.getFilePointer());
        assertEquals('\u06F0', char0);
    }
}
