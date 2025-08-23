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

public class CleaningPathVisitor_ESTestTest15 extends CleaningPathVisitor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        Counters.PathCounters counters_PathCounters0 = CountingPathVisitor.defaultPathCounters();
        String[] stringArray0 = new String[1];
        stringArray0[0] = ",(-_DTfh #j%MqF^";
        CleaningPathVisitor cleaningPathVisitor0 = new CleaningPathVisitor(counters_PathCounters0, stringArray0);
        MockFile mockFile0 = new MockFile(",(-_DTfh #j%MqF^");
        Path path0 = mockFile0.toPath();
        // Undeclared exception!
        try {
            cleaningPathVisitor0.visitFile(path0, (BasicFileAttributes) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.io.file.CountingPathVisitor", e);
        }
    }
}
