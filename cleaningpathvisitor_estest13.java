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

public class CleaningPathVisitor_ESTestTest13 extends CleaningPathVisitor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        String[] stringArray0 = new String[0];
        CountingPathVisitor.Builder countingPathVisitor_Builder0 = new CountingPathVisitor.Builder();
        Counters.PathCounters counters_PathCounters0 = countingPathVisitor_Builder0.getPathCounters();
        CleaningPathVisitor cleaningPathVisitor0 = new CleaningPathVisitor(counters_PathCounters0, stringArray0);
        boolean boolean0 = cleaningPathVisitor0.equals(cleaningPathVisitor0);
        assertTrue(boolean0);
    }
}
