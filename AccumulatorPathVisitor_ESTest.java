package org.apache.commons.io.file;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.file.Counters.PathCounters;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.function.IOBiFunction;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Readable, behavior-focused tests for AccumulatorPathVisitor.
 *
 * These tests avoid mocking where practical and use small, real file system
 * structures to assert typical usage and edge cases.
 */
public class AccumulatorPathVisitorTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    // Helper: creates a subdirectory and a couple files.
    private TestTree createTestTree() throws IOException {
        File root = tmp.newFolder("root");
        File sub = new File(root, "sub");
        assertTrue(sub.mkdir());

        Path fileA = new File(root, "a.txt").toPath();
        Path fileB = new File(sub, "b.txt").toPath();

        Files.write(fileA, "A".getBytes());
        Files.write(fileB, "B".getBytes());

        return new TestTree(root.toPath(), sub.toPath(), fileA, fileB);
    }

    private static final class TestTree {
        final Path root;
        final Path subDir;
        final Path fileA;
        final Path fileB;

        TestTree(Path root, Path subDir, Path fileA, Path fileB) {
            this.root = root;
            this.subDir = subDir;
            this.fileA = fileA;
            this.fileB = fileB;
        }
    }

    @Test
    public void createsVisitorWithFilters() {
        AccumulatorPathVisitor v = AccumulatorPathVisitor.withLongCounters(
            HiddenFileFilter.VISIBLE, HiddenFileFilter.VISIBLE
        );
        assertNotNull(v);
    }

    @Test
    public void builderReturnsVisitor() {
        AccumulatorPathVisitor.Builder builder = AccumulatorPathVisitor.builder();
        AccumulatorPathVisitor v = builder.get();
        assertNotNull(v);
    }

    @Test
    public void updateFileCountersAddsToFileList() throws IOException {
        TestTree t = createTestTree();
        AccumulatorPathVisitor v = AccumulatorPathVisitor.withLongCounters();

        BasicFileAttributes aAttrs = Files.readAttributes(t.fileA, BasicFileAttributes.class);
        BasicFileAttributes bAttrs = Files.readAttributes(t.fileB, BasicFileAttributes.class);

        v.updateFileCounters(t.fileA, aAttrs);
        v.updateFileCounters(t.fileB, bAttrs);

        List<Path> files = v.getFileList();
        assertEquals(2, files.size());
        // Assert by filename to avoid platform-specific path equality subtleties
        assertTrue(files.stream().anyMatch(p -> p.getFileName().toString().equals("a.txt")));
        assertTrue(files.stream().anyMatch(p -> p.getFileName().toString().equals("b.txt")));
    }

    @Test
    public void updateDirCounterAddsToDirList() throws IOException {
        TestTree t = createTestTree();
        AccumulatorPathVisitor v = AccumulatorPathVisitor.withLongCounters();

        // Simulate a directory update; the implementation records directories as they are visited.
        v.updateDirCounter(t.subDir, new IOException("simulated"));

        List<Path> dirs = v.getDirList();
        assertEquals(1, dirs.size());
        assertEquals("sub", dirs.get(0).getFileName().toString());
    }

    @Test
    public void getFileListReturnsDefensiveCopy() throws IOException {
        TestTree t = createTestTree();
        AccumulatorPathVisitor v = AccumulatorPathVisitor.withLongCounters();

        BasicFileAttributes aAttrs = Files.readAttributes(t.fileA, BasicFileAttributes.class);
        v.updateFileCounters(t.fileA, aAttrs);

        List<Path> first = v.getFileList();
        assertEquals(1, first.size());

        // Mutate returned list
        first.clear();

        // Re-fetch; should be unaffected
        List<Path> second = v.getFileList();
        assertEquals(1, second.size());
    }

    @Test
    public void getDirListReturnsDefensiveCopy() throws IOException {
        TestTree t = createTestTree();
        AccumulatorPathVisitor v = AccumulatorPathVisitor.withLongCounters();

        v.updateDirCounter(t.subDir, new IOException("simulated"));

        List<Path> first = v.getDirList();
        assertEquals(1, first.size());

        first.clear();

        List<Path> second = v.getDirList();
        assertEquals(1, second.size());
    }

    @Test
    public void relativizeFilesAgainstRoot_sorted() throws IOException {
        TestTree t = createTestTree();
        AccumulatorPathVisitor v = AccumulatorPathVisitor.withLongCounters();

        v.updateFileCounters(t.fileA, Files.readAttributes(t.fileA, BasicFileAttributes.class));
        v.updateFileCounters(t.fileB, Files.readAttributes(t.fileB, BasicFileAttributes.class));

        List<Path> rel = v.relativizeFiles(t.root, true, null); // default sorting
        assertEquals(2, rel.size());
        assertEquals("a.txt", rel.get(0).toString());
        assertEquals("sub" + File.separator + "b.txt", rel.get(1).toString());
    }

    @Test
    public void relativizeDirectoriesAgainstRoot_sortedWithComparator() throws IOException {
        TestTree t = createTestTree();
        AccumulatorPathVisitor v = AccumulatorPathVisitor.withLongCounters();

        v.updateDirCounter(t.subDir, new IOException("simulated"));

        List<Path> rel = v.relativizeDirectories(t.root, true, Comparator.naturalOrder());
        assertEquals(1, rel.size());
        assertEquals("sub", rel.get(0).toString());
    }

    @Test(expected = NullPointerException.class)
    public void relativizeFiles_nullParent_throwsNPE() throws IOException {
        TestTree t = createTestTree();
        AccumulatorPathVisitor v = AccumulatorPathVisitor.withLongCounters();

        v.updateFileCounters(t.fileA, Files.readAttributes(t.fileA, BasicFileAttributes.class));
        v.relativizeFiles(null, true, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void relativizeFiles_unrelatedParent_throwsIAE() throws IOException {
        TestTree t = createTestTree();
        Path otherRoot = tmp.newFolder("otherRoot").toPath();

        AccumulatorPathVisitor v = AccumulatorPathVisitor.withLongCounters();
        v.updateFileCounters(t.fileA, Files.readAttributes(t.fileA, BasicFileAttributes.class));

        // t.fileA is not relative to otherRoot -> Path.relativize should fail.
        v.relativizeFiles(otherRoot, true, null);
    }

    @Test(expected = NullPointerException.class)
    public void updateFileCounters_nullAttributes_throwsNPE() throws IOException {
        TestTree t = createTestTree();
        AccumulatorPathVisitor v = AccumulatorPathVisitor.withLongCounters();
        v.updateFileCounters(t.fileA, null);
    }

    @Test(expected = NullPointerException.class)
    public void updateDirCounter_nullPath_throwsNPE() {
        AccumulatorPathVisitor v = AccumulatorPathVisitor.withLongCounters();
        v.updateDirCounter(null, new IOException("x"));
    }

    @Test
    public void equalsAndHashCode_onEmptyVisitors() {
        AccumulatorPathVisitor a = AccumulatorPathVisitor.withLongCounters();
        AccumulatorPathVisitor b = AccumulatorPathVisitor.withBigIntegerCounters();

        assertTrue("Two fresh visitors should be equal", a.equals(b));
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equalsDiffersAfterStateChange() throws IOException {
        TestTree t = createTestTree();
        AccumulatorPathVisitor a = AccumulatorPathVisitor.withLongCounters();
        AccumulatorPathVisitor b = AccumulatorPathVisitor.withLongCounters();

        assertTrue(a.equals(b));

        a.updateDirCounter(t.subDir, new IOException("simulated"));

        assertFalse(a.equals(b));
    }

    @Test
    public void deprecatedConstructors_stillWorkForBasicCreation() {
        PathCounters counters = Counters.noopPathCounters();
        AccumulatorPathVisitor v1 = new AccumulatorPathVisitor(counters);
        assertNotNull(v1);

        AccumulatorPathVisitor v2 = new AccumulatorPathVisitor(counters, HiddenFileFilter.VISIBLE, HiddenFileFilter.VISIBLE);
        assertNotNull(v2);
    }

    @Test(expected = NullPointerException.class)
    public void deprecatedConstructor_nullVisitFileFailed_throwsNPE() {
        PathCounters counters = Counters.noopPathCounters();
        PathFilter filter = HiddenFileFilter.VISIBLE;
        IOBiFunction<Path, IOException, FileVisitResult> visitFileFailed = null;

        new AccumulatorPathVisitor(counters, filter, filter, visitFileFailed);
    }
}