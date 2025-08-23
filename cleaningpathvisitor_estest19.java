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

public class CleaningPathVisitor_ESTestTest19 extends CleaningPathVisitor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        CountingPathVisitor.Builder countingPathVisitor_Builder0 = new CountingPathVisitor.Builder();
        Counters.PathCounters counters_PathCounters0 = countingPathVisitor_Builder0.getPathCounters();
        String[] stringArray0 = new String[6];
        stringArray0[0] = "";
        stringArray0[1] = "";
        stringArray0[2] = "";
        stringArray0[3] = "";
        stringArray0[4] = "";
        stringArray0[5] = "";
        CleaningPathVisitor cleaningPathVisitor0 = new CleaningPathVisitor(counters_PathCounters0, stringArray0);
        cleaningPathVisitor0.hashCode();
    }
}
