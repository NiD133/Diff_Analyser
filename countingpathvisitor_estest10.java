package org.apache.commons.io.file;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.UnaryOperator;
import org.apache.commons.io.filefilter.CanWriteFileFilter;
import org.apache.commons.io.filefilter.EmptyFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.PathEqualsFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.function.IOBiFunction;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockIOException;
import org.junit.runner.RunWith;

public class CountingPathVisitor_ESTestTest10 extends CountingPathVisitor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Counters.PathCounters counters_PathCounters0 = CountingPathVisitor.defaultPathCounters();
        DeleteOption[] deleteOptionArray0 = new DeleteOption[3];
        StandardDeleteOption standardDeleteOption0 = StandardDeleteOption.OVERRIDE_READ_ONLY;
        deleteOptionArray0[2] = (DeleteOption) standardDeleteOption0;
        String[] stringArray0 = new String[0];
        DeletingPathVisitor deletingPathVisitor0 = new DeletingPathVisitor(counters_PathCounters0, deleteOptionArray0, stringArray0);
        MockFile mockFile0 = new MockFile("");
        Path path0 = mockFile0.toPath();
        try {
            deletingPathVisitor0.visitFile(path0, (BasicFileAttributes) null);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // DOS or POSIX file operations not available for '', linkOptions [NOFOLLOW_LINKS]
            //
            verifyException("org.apache.commons.io.file.PathUtils", e);
        }
    }
}
