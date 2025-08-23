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

public class RandomAccessFileMode_ESTestTest23 extends RandomAccessFileMode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        RandomAccessFileMode randomAccessFileMode0 = RandomAccessFileMode.READ_ONLY;
        MockFile mockFile0 = new MockFile("U:QK#Sv^NT_2h");
        Path path0 = mockFile0.toPath();
        IOConsumer<RandomAccessFile> iOConsumer0 = IOConsumer.noop();
        try {
            randomAccessFileMode0.accept(path0, iOConsumer0);
            fail("Expecting exception: FileNotFoundException");
        } catch (FileNotFoundException e) {
            //
            // File does not exist, and RandomAccessFile is not open in write mode
            //
            verifyException("org.evosuite.runtime.mock.java.io.MockRandomAccessFile", e);
        }
    }
}
