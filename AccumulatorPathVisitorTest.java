package org.apache.commons.io.file;

import static org.apache.commons.io.file.CounterAssertions.assertCounts;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.io.ThreadUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.EmptyFileFilter;
import org.apache.commons.io.filefilter.PathVisitorFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests both AccumulatorPathVisitor and PathVisitorFileFilter.
 *
 * Notes:
 * - These tests focus on making the intention of each scenario explicit.
 * - Common setup and assertions are factored into helpers to reduce duplication.
 */
class AccumulatorPathVisitorTest {

    @TempDir
    Path tempDirPath;

    // -----------------------
    // Parameter sources
    // -----------------------

    static Stream<Arguments> testParameters() {
        return Stream.of(
            Arguments.of((Supplier<AccumulatorPathVisitor>) AccumulatorPathVisitor::withLongCounters),
            Arguments.of((Supplier<AccumulatorPathVisitor>) AccumulatorPathVisitor::withBigIntegerCounters),
            Arguments.of((Supplier<AccumulatorPathVisitor>) () ->
                AccumulatorPathVisitor.withBigIntegerCounters(TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE))
        );
    }

    static Stream<Arguments> testParametersIgnoreFailures() {
        return Stream.of(
            Arguments.of((Supplier<AccumulatorPathVisitor>) () -> new AccumulatorPathVisitor(
                Counters.bigIntegerPathCounters(),
                CountingPathVisitor.defaultDirectoryFilter(),
                CountingPathVisitor.defaultFileFilter()))
        );
    }

    // -----------------------
    // Helpers
    // -----------------------

    private static PathVisitorFileFilter visitorFilter(final AccumulatorPathVisitor visitor) {
        return new PathVisitorFileFilter(visitor);
    }

    private static Path resourcePath(final String relative) {
        return Paths.get("src/test/resources/org/apache/commons/io").resolve(relative);
    }

    private static void assertReflexiveAndStableEqualsAndHashCode(final Object o) {
        assertEquals(o, o);
        assertEquals(o.hashCode(), o.hashCode());
    }

    private List<Path> createTempFiles(final int count) throws IOException {
        final List<Path> files = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final Path f = Files.createTempFile(tempDirPath, "test", ".txt");
            assertTrue(Files.exists(f));
            files.add(f);
        }
        return files;
    }

    private Set<Path> createTempFilesSet(final int count) throws IOException {
        return new LinkedHashSet<>(createTempFiles(count));
    }

    // -----------------------
    // Tests
    // -----------------------

    /**
     * Verifies the 0-arg constructor (noop counters) still accumulates visited paths.
     * We walk an empty temp directory, so only the directory list should have one entry.
     */
    @Test
    void test0ArgConstructor() throws IOException {
        final AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        final PathVisitorFileFilter vf = visitorFilter(visitor);

        // Limit traversal to directories that are empty. The visitor is still invoked.
        Files.walkFileTree(tempDirPath, new AndFileFilter(vf, DirectoryFileFilter.INSTANCE, EmptyFileFilter.EMPTY));

        // Noop counters -> zero counts, but one visited directory (the temp root), no files.
        assertCounts(0, 0, 0, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size());
        assertTrue(visitor.getFileList().isEmpty());
        assertReflexiveAndStableEqualsAndHashCode(visitor);
    }

    /**
     * Walking an empty directory with various visitor configurations.
     */
    @ParameterizedTest
    @MethodSource("testParameters")
    void testEmptyFolder(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        final AccumulatorPathVisitor visitor = supplier.get();
        final PathVisitorFileFilter vf = visitorFilter(visitor);

        Files.walkFileTree(tempDirPath, new AndFileFilter(vf, DirectoryFileFilter.INSTANCE, EmptyFileFilter.EMPTY));

        assertCounts(1, 0, 0, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size());
        assertTrue(visitor.getFileList().isEmpty());
        assertReflexiveAndStableEqualsAndHashCode(visitor);
    }

    /**
     * Equals and hashCode behavior including mutation of internal counters.
     */
    @Test
    void testEqualsHashCode() {
        final AccumulatorPathVisitor v0 = AccumulatorPathVisitor.withLongCounters();
        final AccumulatorPathVisitor v1 = AccumulatorPathVisitor.withLongCounters();

        // Reflexive and symmetric equality before mutation
        assertEquals(v0, v0);
        assertEquals(v0, v1);
        assertEquals(v1, v0);
        assertEquals(v0.hashCode(), v0.hashCode());
        assertEquals(v0.hashCode(), v1.hashCode());
        assertEquals(v1.hashCode(), v0.hashCode());

        // Mutate one instance; they should now differ
        v0.getPathCounters().getByteCounter().increment();

        assertEquals(v0, v0);
        assertNotEquals(v0, v1);
        assertNotEquals(v1, v0);
        assertEquals(v0.hashCode(), v0.hashCode());
        assertNotEquals(v0.hashCode(), v1.hashCode());
        assertNotEquals(v1.hashCode(), v0.hashCode());
    }

    /**
     * A directory with one file of size 0.
     */
    @ParameterizedTest
    @MethodSource("testParameters")
    void testFolders1FileSize0(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        final AccumulatorPathVisitor visitor = supplier.get();
        Files.walkFileTree(resourcePath("dirs-1-file-size-0"), visitorFilter(visitor));

        assertCounts(1, 1, 0, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size());
        assertEquals(1, visitor.getFileList().size());
        assertReflexiveAndStableEqualsAndHashCode(visitor);
    }

    /**
     * A directory with one file of size 1.
     */
    @ParameterizedTest
    @MethodSource("testParameters")
    void testFolders1FileSize1(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        final AccumulatorPathVisitor visitor = supplier.get();
        Files.walkFileTree(resourcePath("dirs-1-file-size-1"), visitorFilter(visitor));

        assertCounts(1, 1, 1, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size());
        assertEquals(1, visitor.getFileList().size());
        assertReflexiveAndStableEqualsAndHashCode(visitor);
    }

    /**
     * A directory with two subdirectories, each containing one file of size 1.
     */
    @ParameterizedTest
    @MethodSource("testParameters")
    void testFolders2FileSize2(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        final AccumulatorPathVisitor visitor = supplier.get();
        Files.walkFileTree(resourcePath("dirs-2-file-size-2"), visitorFilter(visitor));

        assertCounts(3, 2, 2, visitor.getPathCounters());
        assertEquals(3, visitor.getDirList().size());
        assertEquals(2, visitor.getFileList().size());
        assertReflexiveAndStableEqualsAndHashCode(visitor);
    }

    /**
     * IO-755: Walk a directory while another thread deletes files concurrently.
     * This test aims to exercise robustness, not exact counts (which are non-deterministic here).
     */
    @ParameterizedTest
    @MethodSource("testParametersIgnoreFailures")
    void testFolderWhileDeletingAsync(final Supplier<AccumulatorPathVisitor> supplier) throws IOException, InterruptedException {
        final int count = 10_000;
        final List<Path> files = createTempFiles(count);

        final AccumulatorPathVisitor visitor = supplier.get();

        // Slow down visitFile to increase overlap with deletions.
        final PathVisitorFileFilter vf = new PathVisitorFileFilter(visitor) {
            @Override
            public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) throws IOException {
                try {
                    ThreadUtils.sleep(Duration.ofMillis(10));
                } catch (final InterruptedException ignore) {
                    // ignore
                }
                return super.visitFile(path, attributes);
            }
        };

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final AtomicBoolean deletionCompleted = new AtomicBoolean(false);

        try {
            executor.execute(() -> {
                for (final Path f : files) {
                    try {
                        Files.delete(f); // Best-effort, some deletions may fail if already visited/deleted
                    } catch (final IOException ignored) {
                        // ignore
                    }
                }
                deletionCompleted.set(true);
            });

            Files.walkFileTree(tempDirPath, vf);
        } finally {
            if (!deletionCompleted.get()) {
                ThreadUtils.sleep(Duration.ofMillis(1000));
            }
            if (!deletionCompleted.get()) {
                executor.awaitTermination(5, TimeUnit.SECONDS);
            }
            executor.shutdownNow();
        }

        // Basic sanity on equals/hashCode after traversal
        assertReflexiveAndStableEqualsAndHashCode(visitor);
    }

    /**
     * IO-755: Walk a directory and delete all files halfway through the visit from the visiting thread.
     * Here, after visiting half the files, we delete them all. The visitor won't see deleted files.
     */
    @ParameterizedTest
    @MethodSource("testParametersIgnoreFailures")
    void testFolderWhileDeletingSync(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        final int totalFiles = 100;
        final int halfway = totalFiles / 2;

        final Set<Path> files = createTempFilesSet(totalFiles);
        final AccumulatorPathVisitor visitor = supplier.get();
        final AtomicInteger visited = new AtomicInteger();

        final PathVisitorFileFilter vf = new PathVisitorFileFilter(visitor) {
            @Override
            public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) throws IOException {
                if (visited.incrementAndGet() == halfway) {
                    for (final Path f : files) {
                        Files.delete(f);
                    }
                }
                return super.visitFile(path, attributes);
            }
        };

        Files.walkFileTree(tempDirPath, vf);

        // Only the files visited before deletion remain counted.
        assertCounts(1, halfway - 1, 0, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size());
        assertEquals(halfway - 1, visitor.getFileList().size());
        assertReflexiveAndStableEqualsAndHashCode(visitor);
    }
}