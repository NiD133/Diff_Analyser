package org.apache.commons.io.file;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.commons.io.file.Counters.PathCounters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CleaningPathVisitorTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    /**
     * visitFile should delete a regular file when it is not in the skip list.
     */
    @Test
    public void deletesRegularFileWhenNotSkipped() throws IOException {
        File f = tmp.newFile("deleteme.txt");
        Path file = f.toPath();
        BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);

        PathCounters counters = CountingPathVisitor.defaultPathCounters();
        CleaningPathVisitor visitor = new CleaningPathVisitor(counters, (String[]) null);

        FileVisitResult result = visitor.visitFile(file, attrs);

        assertEquals(FileVisitResult.CONTINUE, result);
        assertFalse("File should be deleted", Files.exists(file));
    }

    /**
     * visitFile should not delete files whose path string is listed in the skip array.
     */
    @Test
    public void doesNotDeleteSkippedFile() throws IOException {
        File f = tmp.newFile("keepme.txt");
        Path file = f.toPath();
        BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);

        PathCounters counters = CountingPathVisitor.defaultPathCounters();
        // Use the file's full path string to match the implementation, which stores String[].
        CleaningPathVisitor visitor = new CleaningPathVisitor(counters, file.toString());

        FileVisitResult result = visitor.visitFile(file, attrs);

        assertEquals(FileVisitResult.CONTINUE, result);
        assertTrue("Skipped file should remain", Files.exists(file));
    }

    /**
     * preVisitDirectory should return SKIP_SUBTREE when the directory is in the skip list.
     */
    @Test
    public void preVisitDirectorySkipsSubtreeWhenDirectoryIsSkipped() throws IOException {
        File dir = tmp.newFolder("skipdir");
        Path dirPath = dir.toPath();
        BasicFileAttributes attrs = Files.readAttributes(dirPath, BasicFileAttributes.class);

        PathCounters counters = CountingPathVisitor.defaultPathCounters();
        CleaningPathVisitor visitor = new CleaningPathVisitor(counters, dirPath.toString());

        FileVisitResult result = visitor.preVisitDirectory(dirPath, attrs);

        assertEquals(FileVisitResult.SKIP_SUBTREE, result);
    }

    /**
     * preVisitDirectory should return CONTINUE when the directory is not skipped.
     */
    @Test
    public void preVisitDirectoryContinuesWhenNotSkipped() throws IOException {
        File dir = tmp.newFolder("normaldir");
        Path dirPath = dir.toPath();
        BasicFileAttributes attrs = Files.readAttributes(dirPath, BasicFileAttributes.class);

        PathCounters counters = CountingPathVisitor.defaultPathCounters();
        CleaningPathVisitor visitor = new CleaningPathVisitor(counters, (String[]) null);

        FileVisitResult result = visitor.preVisitDirectory(dirPath, attrs);

        assertEquals(FileVisitResult.CONTINUE, result);
    }

    /**
     * visitFile should require non-null BasicFileAttributes (delegates to CountingPathVisitor).
     */
    @Test(expected = NullPointerException.class)
    public void visitFileRequiresAttributes() throws IOException {
        File f = tmp.newFile("any.txt");
        Path file = f.toPath();

        PathCounters counters = CountingPathVisitor.defaultPathCounters();
        CleaningPathVisitor visitor = new CleaningPathVisitor(counters, (String[]) null);

        visitor.visitFile(file, null);
    }

    /**
     * Constructor should accept a null skip array and behave like an empty skip list.
     * Here we verify equals/hashCode consistency for two instances created with null and empty skip arrays.
     */
    @Test
    public void constructorHandlesNullSkipSameAsEmpty() {
        PathCounters counters = CountingPathVisitor.defaultPathCounters();

        CleaningPathVisitor vNull = new CleaningPathVisitor(counters, (String[]) null);
        CleaningPathVisitor vEmpty = new CleaningPathVisitor(counters, new String[0]);

        assertEquals(vNull, vEmpty);
        assertEquals(vNull.hashCode(), vEmpty.hashCode());
    }

    /**
     * equals/hashCode: two visitors with the same counters and skip list are equal.
     */
    @Test
    public void equalsAndHashCodeSameConfiguration() {
        PathCounters counters = CountingPathVisitor.defaultPathCounters();
        String[] skip = new String[] { "a", "b", "c" };

        CleaningPathVisitor v1 = new CleaningPathVisitor(counters, skip);
        CleaningPathVisitor v2 = new CleaningPathVisitor(counters, skip.clone());

        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
    }

    /**
     * equals: different skip lists or different delete options should make visitors not equal.
     */
    @Test
    public void equalsFalseForDifferentConfig() {
        PathCounters counters = CountingPathVisitor.defaultPathCounters();

        CleaningPathVisitor base = new CleaningPathVisitor(counters, (String[]) null);

        CleaningPathVisitor differentSkip = new CleaningPathVisitor(counters, "something");
        assertNotEquals(base, differentSkip);

        CleaningPathVisitor differentDeleteOption =
                new CleaningPathVisitor(counters, new DeleteOption[] { StandardDeleteOption.OVERRIDE_READ_ONLY }, (String[]) null);
        assertNotEquals(base, differentDeleteOption);
    }

    /**
     * equals should return false when comparing to a different type, and true for identity.
     */
    @Test
    public void equalsTypeAndIdentityBehavior() {
        PathCounters counters = CountingPathVisitor.defaultPathCounters();
        CleaningPathVisitor visitor = new CleaningPathVisitor(counters, (String[]) null);

        assertTrue(visitor.equals(visitor));
        assertFalse(visitor.equals("not a visitor"));
    }

    /**
     * Factory methods should return non-null CountingPathVisitor instances.
     */
    @Test
    public void factoryMethodsReturnVisitors() {
        assertNotNull(CleaningPathVisitor.withBigIntegerCounters());
        assertNotNull(CleaningPathVisitor.withLongCounters());
    }
}