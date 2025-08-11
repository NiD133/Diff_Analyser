/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link CleaningPathVisitor}.
 */
public class CleaningPathVisitorTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private Path tempDirPath;
    private BasicFileAttributes tempDirAttributes;

    @Before
    public void setUp() throws IOException {
        tempDirPath = tempFolder.getRoot().toPath();
        tempDirAttributes = Files.readAttributes(tempDirPath, BasicFileAttributes.class);
    }

    // --- Constructor Tests ---

    @Test
    public void testConstructorWithNullSkipList() {
        // A null skip list is permissible and should not cause an exception.
        final CleaningPathVisitor visitor = new CleaningPathVisitor(Counters.noopPathCounters(), (String[]) null);
        assertNotNull(visitor);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorThrowsNpeForSkipListWithNulls() {
        // The constructor sorts the skip list, which throws an NPE if it contains null elements.
        new CleaningPathVisitor(Counters.noopPathCounters(), "a", null, "b");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorThrowsNpeForDeleteOptionsWithNulls() {
        // The constructor processes delete options, which throws an NPE if the array contains nulls.
        new CleaningPathVisitor(Counters.noopPathCounters(), new DeleteOption[]{StandardDeleteOption.OVERRIDE_READ_ONLY, null});
    }

    // --- preVisitDirectory Tests ---

    @Test
    public void testPreVisitDirectoryReturnsContinueForNonSkippedDir() throws IOException {
        final CleaningPathVisitor visitor = new CleaningPathVisitor(Counters.noopPathCounters(), "anotherDir");
        assertEquals(FileVisitResult.CONTINUE, visitor.preVisitDirectory(tempDirPath, tempDirAttributes));
    }

    @Test
    public void testPreVisitDirectoryReturnsSkipSubtreeForSkippedDir() throws IOException {
        final String dirToSkip = tempFolder.getRoot().getName();
        final CleaningPathVisitor visitor = new CleaningPathVisitor(Counters.noopPathCounters(), dirToSkip);
        assertEquals(FileVisitResult.SKIP_SUBTREE, visitor.preVisitDirectory(tempDirPath, tempDirAttributes));
    }

    // --- visitFile Tests ---

    @Test
    public void testVisitFileDeletesFileWhenNotSkipped() throws IOException {
        final Path fileToClean = tempFolder.newFile("fileToClean.txt").toPath();
        assertTrue("File should exist before visit", Files.exists(fileToClean));

        final CleaningPathVisitor visitor = new CleaningPathVisitor(Counters.longPathCounters());
        final BasicFileAttributes attributes = Files.readAttributes(fileToClean, BasicFileAttributes.class);

        visitor.visitFile(fileToClean, attributes);

        assertFalse("File should be deleted after visit", Files.exists(fileToClean));
        assertEquals("File counter should be incremented", 1, visitor.getPathCounters().getFileCounter().get());
        assertEquals("Byte counter should be updated", attributes.size(), visitor.getPathCounters().getByteCounter().get());
    }

    @Test
    public void testVisitFileDoesNotDeleteFileWhenSkipped() throws IOException {
        final String fileNameToSkip = "fileToSkip.txt";
        final Path fileToSkip = tempFolder.newFile(fileNameToSkip).toPath();
        assertTrue("File should exist before visit", Files.exists(fileToSkip));

        final CleaningPathVisitor visitor = new CleaningPathVisitor(Counters.longPathCounters(), fileNameToSkip);
        final BasicFileAttributes attributes = Files.readAttributes(fileToSkip, BasicFileAttributes.class);

        visitor.visitFile(fileToSkip, attributes);

        assertTrue("Skipped file should not be deleted", Files.exists(fileToSkip));
        // The counter is updated by the superclass even if the file is not deleted.
        assertEquals("File counter should be incremented", 1, visitor.getPathCounters().getFileCounter().get());
        assertEquals("Byte counter should be updated", attributes.size(), visitor.getPathCounters().getByteCounter().get());
    }

    @Test
    public void testVisitFileDeletesReadOnlyFileWithOverrideOption() throws IOException {
        final Path readOnlyFile = tempFolder.newFile("readOnly.txt").toPath();

        // Make the file read-only
        if (!readOnlyFile.toFile().setReadOnly()) {
            System.err.println("Warning: Could not set file to read-only. Skipping test.");
            return;
        }
        assertFalse("File should be read-only", Files.isWritable(readOnlyFile));

        final CleaningPathVisitor visitor = new CleaningPathVisitor(
            Counters.noopPathCounters(),
            new DeleteOption[]{StandardDeleteOption.OVERRIDE_READ_ONLY}
        );
        final BasicFileAttributes attributes = Files.readAttributes(readOnlyFile, BasicFileAttributes.class);

        visitor.visitFile(readOnlyFile, attributes);

        assertFalse("Read-only file should be deleted with override option", Files.exists(readOnlyFile));
    }

    @Test(expected = NullPointerException.class)
    public void testVisitFileThrowsNpeForNullAttributes() throws IOException {
        final CleaningPathVisitor visitor = new CleaningPathVisitor(Counters.noopPathCounters());
        final Path file = tempFolder.newFile("anyfile.txt").toPath();
        // Calling visitFile with null attributes should throw NPE from the superclass.
        visitor.visitFile(file, null);
    }

    // --- Static Factory Tests ---

    @Test
    public void testWithBigIntegerCounters() {
        final CountingPathVisitor visitor = CleaningPathVisitor.withBigIntegerCounters();
        assertNotNull(visitor);
        assertEquals(Counters.bigIntegerPathCounters().getClass(), visitor.getPathCounters().getClass());
    }

    @Test
    public void testWithLongCounters() {
        final CountingPathVisitor visitor = CleaningPathVisitor.withLongCounters();
        assertNotNull(visitor);
        assertEquals(Counters.longPathCounters().getClass(), visitor.getPathCounters().getClass());
    }

    // --- Equals and HashCode Tests ---

    @Test
    public void testEqualsAndHashCode() {
        final CleaningPathVisitor visitor1 = new CleaningPathVisitor(Counters.longPathCounters(), "skip1", "skip2");
        final CleaningPathVisitor visitor2 = new CleaningPathVisitor(Counters.longPathCounters(), "skip1", "skip2");
        final CleaningPathVisitor visitor3 = new CleaningPathVisitor(Counters.longPathCounters(), "skip1"); // Different skip list
        final CleaningPathVisitor visitor4 = new CleaningPathVisitor(Counters.bigIntegerPathCounters(), "skip1", "skip2"); // Different counter
        final CleaningPathVisitor visitor5 = new CleaningPathVisitor(
            Counters.longPathCounters(),
            new DeleteOption[]{StandardDeleteOption.OVERRIDE_READ_ONLY},
            "skip1", "skip2"
        ); // Different delete option

        // Reflexive
        assertEquals("A visitor must be equal to itself.", visitor1, visitor1);

        // Symmetric and consistent
        assertEquals("Identical visitors must be equal.", visitor1, visitor2);
        assertEquals("Hash code for identical visitors must be the same.", visitor1.hashCode(), visitor2.hashCode());

        // Not equal to different configurations
        assertNotEquals("Visitors with different skip lists should not be equal.", visitor1, visitor3);
        assertNotEquals("Visitors with different counters should not be equal.", visitor1, visitor4);
        assertNotEquals("Visitors with different delete options should not be equal.", visitor1, visitor5);

        // Not equal to null or other types
        assertNotEquals("A visitor should not be equal to null.", visitor1, null);
        assertNotEquals("A visitor should not be equal to an object of a different type.", visitor1, new Object());
    }
}