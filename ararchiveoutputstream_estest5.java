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

public class ArArchiveOutputStream_ESTestTest5 extends ArArchiveOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        MockPrintStream mockPrintStream0 = new MockPrintStream("HU)HAv");
        byte[] byteArray0 = new byte[7];
        File file0 = MockFile.createTempFile("HU)HAv", "HU)HAv", (File) null);
        ArArchiveOutputStream arArchiveOutputStream0 = new ArArchiveOutputStream(mockPrintStream0);
        ArArchiveEntry arArchiveEntry0 = arArchiveOutputStream0.createArchiveEntry(file0, "#c.");
        arArchiveOutputStream0.putArchiveEntry(arArchiveEntry0);
        arArchiveOutputStream0.write(byteArray0, (int) (byte) 37, (int) (byte) (-67));
        arArchiveOutputStream0.closeArchiveEntry();
        assertEquals(1, arArchiveOutputStream0.getCount());
    }
}
