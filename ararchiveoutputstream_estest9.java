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

public class ArArchiveOutputStream_ESTestTest9 extends ArArchiveOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        MockPrintStream mockPrintStream0 = new MockPrintStream("H U)HAv");
        MockFile mockFile0 = new MockFile("[e6N+eFPO;>oPtNaGo");
        ArArchiveOutputStream arArchiveOutputStream0 = new ArArchiveOutputStream(mockPrintStream0);
        ArArchiveEntry arArchiveEntry0 = arArchiveOutputStream0.createArchiveEntry((File) mockFile0, "cT%:jX_Do~zn~<qN");
        assertEquals(0L, arArchiveEntry0.getSize());
    }
}
