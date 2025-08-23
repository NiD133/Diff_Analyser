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

public class CleaningPathVisitor_ESTestTest10 extends CleaningPathVisitor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        CountingPathVisitor countingPathVisitor0 = CleaningPathVisitor.withBigIntegerCounters();
        Counters.PathCounters counters_PathCounters0 = CountingPathVisitor.defaultPathCounters();
        DeleteOption[] deleteOptionArray0 = new DeleteOption[4];
        StandardDeleteOption standardDeleteOption0 = StandardDeleteOption.OVERRIDE_READ_ONLY;
        deleteOptionArray0[1] = (DeleteOption) standardDeleteOption0;
        String[] stringArray0 = new String[0];
        CleaningPathVisitor cleaningPathVisitor0 = new CleaningPathVisitor(counters_PathCounters0, deleteOptionArray0, stringArray0);
        boolean boolean0 = countingPathVisitor0.equals(cleaningPathVisitor0);
        assertFalse(boolean0);
    }
}
