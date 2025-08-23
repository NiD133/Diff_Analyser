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

public class ParallelScatterZipCreator_ESTestTest12 extends ParallelScatterZipCreator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        ForkJoinPool forkJoinPool0 = ForkJoinPool.commonPool();
        URI uRI0 = MockURI.aFileURI;
        MockFile mockFile0 = new MockFile(uRI0);
        mockFile0.renameTo(mockFile0);
        Path path0 = mockFile0.toPath();
        DefaultBackingStoreSupplier defaultBackingStoreSupplier0 = new DefaultBackingStoreSupplier(path0);
        ParallelScatterZipCreator parallelScatterZipCreator0 = new ParallelScatterZipCreator(forkJoinPool0, defaultBackingStoreSupplier0, 0);
        ZipArchiveOutputStream zipArchiveOutputStream0 = null;
        try {
            zipArchiveOutputStream0 = new ZipArchiveOutputStream(mockFile0, 0);
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            //
            // Could not initialize class org.apache.commons.compress.archivers.zip.ZipEncodingHelper
            //
            verifyException("org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream", e);
        }
    }
}
