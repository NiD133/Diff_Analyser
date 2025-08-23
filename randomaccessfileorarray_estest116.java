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

public class RandomAccessFileOrArray_ESTestTest116 extends RandomAccessFileOrArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test115() throws Throwable {
        WindowRandomAccessSource windowRandomAccessSource0 = new WindowRandomAccessSource((RandomAccessSource) null, 418L, 418L);
        IndependentRandomAccessSource independentRandomAccessSource0 = new IndependentRandomAccessSource(windowRandomAccessSource0);
        GetBufferedRandomAccessSource getBufferedRandomAccessSource0 = new GetBufferedRandomAccessSource(independentRandomAccessSource0);
        RandomAccessFileOrArray randomAccessFileOrArray0 = new RandomAccessFileOrArray(getBufferedRandomAccessSource0);
        long long0 = randomAccessFileOrArray0.skip(418L);
        assertEquals(418L, randomAccessFileOrArray0.getFilePointer());
        assertEquals(418L, long0);
    }
}