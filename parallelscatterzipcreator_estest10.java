package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.compress.parallel.ScatterGatherBackingStoreSupplier;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.net.MockURI;
import org.junit.runner.RunWith;

public class ParallelScatterZipCreator_ESTestTest10 extends ParallelScatterZipCreator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        ForkJoinPool forkJoinPool0 = new ForkJoinPool();
        MockFile mockFile0 = new MockFile("");
        Path path0 = mockFile0.toPath();
        DefaultBackingStoreSupplier defaultBackingStoreSupplier0 = new DefaultBackingStoreSupplier(path0);
        ParallelScatterZipCreator parallelScatterZipCreator0 = null;
        try {
            parallelScatterZipCreator0 = new ParallelScatterZipCreator(forkJoinPool0, defaultBackingStoreSupplier0, 1394);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Compression level is expected between -1~9
            //
            verifyException("org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator", e);
        }
    }
}
