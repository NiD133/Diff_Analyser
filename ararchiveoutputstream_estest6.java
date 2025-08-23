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

public class ArArchiveOutputStream_ESTestTest6 extends ArArchiveOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        MockFileOutputStream mockFileOutputStream0 = new MockFileOutputStream("*m{4/p6", false);
        ArArchiveOutputStream arArchiveOutputStream0 = new ArArchiveOutputStream(mockFileOutputStream0);
        ArArchiveEntry arArchiveEntry0 = new ArArchiveEntry("User ID", 8589934591L, 61440, 7, (-2628), 0);
        arArchiveOutputStream0.putArchiveEntry(arArchiveEntry0);
        assertEquals(68L, arArchiveOutputStream0.getBytesWritten());
    }
}
