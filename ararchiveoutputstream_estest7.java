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

public class ArArchiveOutputStream_ESTestTest7 extends ArArchiveOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        MockPrintStream mockPrintStream0 = new MockPrintStream("HU)HAv");
        MockFile mockFile0 = new MockFile((File) null, "");
        ArArchiveOutputStream arArchiveOutputStream0 = new ArArchiveOutputStream(mockPrintStream0);
        Path path0 = mockFile0.toPath();
        LinkOption[] linkOptionArray0 = new LinkOption[9];
        LinkOption linkOption0 = LinkOption.NOFOLLOW_LINKS;
        linkOptionArray0[0] = linkOption0;
        linkOptionArray0[1] = linkOption0;
        linkOptionArray0[2] = linkOptionArray0[0];
        linkOptionArray0[3] = linkOptionArray0[0];
        linkOptionArray0[4] = linkOption0;
        linkOptionArray0[5] = linkOptionArray0[3];
        linkOptionArray0[6] = linkOption0;
        linkOptionArray0[7] = linkOption0;
        linkOptionArray0[8] = linkOptionArray0[3];
        ArArchiveEntry arArchiveEntry0 = arArchiveOutputStream0.createArchiveEntry(path0, "", linkOptionArray0);
        assertEquals(0, arArchiveEntry0.getUserId());
    }
}
