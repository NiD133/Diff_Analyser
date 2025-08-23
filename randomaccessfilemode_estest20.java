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

public class RandomAccessFileMode_ESTestTest20 extends RandomAccessFileMode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        MockFile mockFile0 = new MockFile("chunkSize <= 0");
        Path path0 = mockFile0.toPath();
        RandomAccessFileMode randomAccessFileMode0 = RandomAccessFileMode.READ_ONLY;
        try {
            randomAccessFileMode0.apply(path0, (IOFunction<RandomAccessFile, RandomAccessFileMode>) null);
            fail("Expecting exception: FileNotFoundException");
        } catch (FileNotFoundException e) {
            //
            // File does not exist, and RandomAccessFile is not open in write mode
            //
            verifyException("org.evosuite.runtime.mock.java.io.MockRandomAccessFile", e);
        }
    }
}
