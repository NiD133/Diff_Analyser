package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOFunction;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.testdata.EvoSuiteFile;
import org.evosuite.runtime.testdata.FileSystemHandling;
import org.junit.runner.RunWith;

public class RandomAccessFileMode_ESTestTest33 extends RandomAccessFileMode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        OpenOption[] openOptionArray0 = new OpenOption[1];
        StandardOpenOption standardOpenOption0 = StandardOpenOption.WRITE;
        openOptionArray0[0] = (OpenOption) standardOpenOption0;
        RandomAccessFileMode randomAccessFileMode0 = RandomAccessFileMode.valueOf(openOptionArray0);
        MockFile mockFile0 = new MockFile("6UOySR)&C(+0\"]F]DS");
        IORandomAccessFile iORandomAccessFile0 = (IORandomAccessFile) randomAccessFileMode0.create((File) mockFile0);
        assertEquals("rw", iORandomAccessFile0.getMode());
    }
}
