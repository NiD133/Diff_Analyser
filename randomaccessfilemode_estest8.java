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

public class RandomAccessFileMode_ESTestTest8 extends RandomAccessFileMode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        RandomAccessFileMode randomAccessFileMode0 = RandomAccessFileMode.valueOfMode("r");
        assertEquals(RandomAccessFileMode.READ_ONLY, randomAccessFileMode0);
        EvoSuiteFile evoSuiteFile0 = new EvoSuiteFile("r");
        FileSystemHandling.appendStringToFile(evoSuiteFile0, "r");
        MockFile mockFile0 = new MockFile("r");
        Path path0 = mockFile0.toPath();
        randomAccessFileMode0.create(path0);
        assertEquals("r", randomAccessFileMode0.getMode());
    }
}
