package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.System;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.evosuite.runtime.testdata.FileSystemHandling;
import org.junit.runner.RunWith;

public class ArArchiveOutputStream_ESTestTest3 extends ArArchiveOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        MockPrintStream mockPrintStream0 = new MockPrintStream("HU)HAv");
        byte[] byteArray0 = new byte[7];
        File file0 = MockFile.createTempFile("HU)HAv", "HU)HAv", (File) null);
        ArArchiveOutputStream arArchiveOutputStream0 = new ArArchiveOutputStream(mockPrintStream0);
        ArArchiveEntry arArchiveEntry0 = arArchiveOutputStream0.createArchiveEntry(file0, "HU)HAv");
        arArchiveOutputStream0.putArchiveEntry(arArchiveEntry0);
        arArchiveOutputStream0.write(byteArray0, (int) (byte) 127, 536);
        try {
            arArchiveOutputStream0.putArchiveEntry(arArchiveEntry0);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Length does not match entry (0 != 536
            //
            verifyException("org.apache.commons.compress.archivers.ar.ArArchiveOutputStream", e);
        }
    }
}
