package org.apache.commons.io.file;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CountingPathVisitorTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private static BasicFileAttributes attrsFor(final Path path) throws IOException {
        return Files.readAttributes(path, BasicFileAttributes.class);
    }

    private static PathFilter acceptAll() {
        return (path, attributes) -> true;
    }

    private static PathFilter rejectAll() {
        return (path, attributes) -> false;
    }

    @Test
    public void builder_fluentApi_returnsSameInstance() {
        CountingPathVisitor.Builder b = new CountingPathVisitor.Builder();

        // Exercise fluent setters.
        CountingPathVisitor.Builder b1 = b
                .setPathCounters(CountingPathVisitor.defaultPathCounters())
                .setFileFilter(acceptAll())
                .setDirectoryFilter(rejectAll())
                .setDirectoryPostTransformer(UnaryOperator.identity());

        assertSame("Builder should be fluent and return itself", b, b1);
    }

    @Test
    public void factoryMethods_returnNonNull() {
        assertNotNull(CountingPathVisitor.withLongCounters());
        assertNotNull(CountingPathVisitor.withBigIntegerCounters());
        assertNotNull(CountingPathVisitor.defaultDirectoryTransformer());
        assertNotNull(CountingPathVisitor.defaultFileFilter());
        assertNotNull(CountingPathVisitor.defaultPathCounters());
    }

    @Test
    public void visitFile_countsAcceptedFilesAndBytes() throws Exception {
        // Arrange: create a real file with 3 bytes.
        File f = tmp.newFile("file.txt");
        Files.write(f.toPath(), "abc".getBytes(StandardCharsets.UTF_8));
        Path file = f.toPath();
        BasicFileAttributes attrs = attrsFor(file);

        // A visitor that accepts everything.
        CountingPathVisitor visitor = new CountingPathVisitor.Builder()
                .setFileFilter(acceptAll())
                .setDirectoryFilter(acceptAll())
                .get();

        // Act
        FileVisitResult result = visitor.visitFile(file, attrs);

        // Assert
        assertEquals(FileVisitResult.CONTINUE, result);
        // String form is stable across counter implementations.
        assertEquals("1 files, 0 directories, 3 bytes", visitor.toString());
    }

    @Test
    public void visitFile_doesNotCountWhenFilteredOut() throws Exception {
        // Arrange: create a real file.
        File f = tmp.newFile("ignored.bin");
        Files.write(f.toPath(), new byte[] { 1, 2, 3, 4 });
        Path file = f.toPath();
        BasicFileAttributes attrs = attrsFor(file);

        // A visitor that rejects all files.
        CountingPathVisitor visitor = new CountingPathVisitor.Builder()
                .setFileFilter(rejectAll())
                .setDirectoryFilter(acceptAll())
                .get();

        // Act
        FileVisitResult result = visitor.visitFile(file, attrs);

        // Assert
        assertEquals(FileVisitResult.CONTINUE, result);
        assertEquals("0 files, 0 directories, 0 bytes", visitor.toString());
    }

    @Test
    public void preAndPostVisitDirectory_countsDirectory_and_appliesTransformer() throws Exception {
        // Arrange: create a real directory.
        File dir = tmp.newFolder("dir");
        Path dirPath = dir.toPath();
        BasicFileAttributes dirAttrs = attrsFor(dirPath);

        AtomicBoolean transformerCalled = new AtomicBoolean(false);
        UnaryOperator<Path> spyTransformer = p -> {
            transformerCalled.set(true);
            return p; // identity
        };

        CountingPathVisitor visitor = new CountingPathVisitor.Builder()
                .setDirectoryFilter(acceptAll())
                .setFileFilter(acceptAll())
                .setDirectoryPostTransformer(spyTransformer)
                .get();

        // Act: simulate directory traversal.
        FileVisitResult pre = visitor.preVisitDirectory(dirPath, dirAttrs);
        FileVisitResult post = visitor.postVisitDirectory(dirPath, null);

        // Assert
        assertEquals(FileVisitResult.CONTINUE, pre);
        assertEquals(FileVisitResult.CONTINUE, post);
        assertTrue("Directory transformer should be invoked in postVisitDirectory", transformerCalled.get());
        assertEquals("0 files, 1 directories, 0 bytes", visitor.toString());
    }

    @Test(expected = NullPointerException.class)
    public void visitFile_throwsNpeOnNullAttributes() throws Exception {
        Path file = tmp.newFile("npe.txt").toPath();
        CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();

        // Expect NPE when attributes are null.
        visitor.visitFile(file, null);
    }

    @Test
    public void equals_and_hashCode_obviousProperties() {
        CountingPathVisitor visitor = CountingPathVisitor.withBigIntegerCounters();

        // Reflexive
        assertTrue(visitor.equals(visitor));
        // Not equal to null
        assertFalse(visitor.equals(null));
        // Not equal to different type
        assertFalse(visitor.equals("not-a-visitor"));

        // hashCode should be callable without error
        visitor.hashCode();
    }

    @Test(expected = NullPointerException.class)
    public void ctor_throwsOnNullFileFilter() {
        new CountingPathVisitor(
                CountingPathVisitor.defaultPathCounters(),
                null,
                acceptAll()
        );
    }

    @Test(expected = NullPointerException.class)
    public void ctor_throwsOnNullDirectoryFilter() {
        new CountingPathVisitor(
                CountingPathVisitor.defaultPathCounters(),
                acceptAll(),
                null
        );
    }
}