package org.apache.commons.io.file;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.junit.runner.RunWith;

public class CleaningPathVisitor_ESTestTest5 extends CleaningPathVisitor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        DeleteOption[] deleteOptionArray0 = new DeleteOption[1];
        StandardDeleteOption standardDeleteOption0 = StandardDeleteOption.OVERRIDE_READ_ONLY;
        deleteOptionArray0[0] = (DeleteOption) standardDeleteOption0;
        String[] stringArray0 = new String[0];
        CleaningPathVisitor cleaningPathVisitor0 = new CleaningPathVisitor((Counters.PathCounters) null, deleteOptionArray0, stringArray0);
        MockFile mockFile0 = new MockFile("", "");
        Path path0 = mockFile0.toPath();
        BasicFileAttributes basicFileAttributes0 = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        try {
            cleaningPathVisitor0.visitFile(path0, basicFileAttributes0);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // DOS or POSIX file operations not available for '/', linkOptions [NOFOLLOW_LINKS]
            //
            verifyException("org.apache.commons.io.file.PathUtils", e);
        }
    }
}
