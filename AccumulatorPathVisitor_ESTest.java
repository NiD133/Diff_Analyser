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

import org.apache.commons.io.filefilter.CanReadFileFilter;
import org.apache.commons.io.filefilter.CanWriteFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Test;
import org.mockito.internal.util.io.IOUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link AccumulatorPathVisitor}.
 */
public class AccumulatorPathVisitorTest {

    @Test
    public void withBigIntegerCounters_shouldCreateVisitorWithGivenFilters() {
        // Arrange
        PathFilter fileFilter = CanReadFileFilter.CAN_READ;
        PathFilter dirFilter = CanWriteFileFilter.CAN_WRITE;

        // Act
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters(fileFilter, dirFilter);

        // Assert
        assertNotNull(visitor);
    }

    @Test
    public void getLists_shouldBeEmpty_onNewVisitor() {
        // Arrange
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();

        // Assert
        assertTrue("File list should be empty for a new visitor", visitor.getFileList().isEmpty());
        assertTrue("Directory list should be empty for a new visitor", visitor.getDirList().isEmpty());
    }

    @Test
    public void getFileList_shouldReturnListOfVisitedFiles() throws IOException {
        // Arrange
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters();
        Path file = Paths.get("file.txt");
        BasicFileAttributes attrs = mock(BasicFileAttributes.class);
        when(attrs.size()).thenReturn(10L);

        // Act
        visitor.visitFile(file, attrs);
        List<Path> fileList = visitor.getFileList();

        // Assert
        assertEquals(1, fileList.size());
        assertEquals(file, fileList.get(0));
    }

    @Test
    public void getDirList_shouldReturnListOfVisitedDirectories() throws IOException {
        // Arrange
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        Path dir = Paths.get("some/dir");

        // Act
        visitor.postVisitDirectory(dir, null);
        List<Path> dirList = visitor.getDirList();

        // Assert
        assertEquals(1, dirList.size());
        assertEquals(dir, dirList.get(0));
    }

    @Test
    public void relativizeFiles_shouldReturnEmptyList_whenNoFilesVisited() {
        // Arrange
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        Path parentDir = Paths.get("some/parent");

        // Act
        List<Path> relativizedFiles = visitor.relativizeFiles(parentDir, false, null);

        // Assert
        assertTrue(relativizedFiles.isEmpty());
    }

    @Test
    public void relativizeDirectories_shouldReturnEmptyList_whenNoDirectoriesVisited() {
        // Arrange
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters();
        Path parentDir = Paths.get("some/parent");

        // Act
        List<Path> relativizedDirs = visitor.relativizeDirectories(parentDir, false, null);

        // Assert
        assertTrue(relativizedDirs.isEmpty());
    }

    @Test
    public void relativizeFiles_shouldReturnRelativizedPath_whenFileIsVisited() throws IOException {
        // Arrange
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        Path parentDir = Paths.get("test/data");
        Path file = parentDir.resolve("file.txt");
        BasicFileAttributes attrs = mock(BasicFileAttributes.class);
        when(attrs.size()).thenReturn(123L);

        visitor.visitFile(file, attrs);

        // Act
        List<Path> relativizedFiles = visitor.relativizeFiles(parentDir, true, Comparator.naturalOrder());

        // Assert
        assertEquals(1, relativizedFiles.size());
        assertEquals(Paths.get("file.txt"), relativizedFiles.get(0));
    }

    @Test
    public void relativizeDirectories_shouldReturnRelativizedPath_whenDirectoryIsVisited() throws IOException {
        // Arrange
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        Path parentDir = Paths.get("test/data");
        Path subDir = parentDir.resolve("subdir");

        visitor.postVisitDirectory(subDir, null);

        // Act
        List<Path> relativizedDirs = visitor.relativizeDirectories(parentDir, false, null);

        // Assert
        assertEquals(1, relativizedDirs.size());
        assertEquals(Paths.get("subdir"), relativizedDirs.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void relativizeFiles_shouldThrowException_whenParentPathIsNotAnAncestor() throws IOException {
        // Arrange
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters();
        Path visitedFile = Paths.get("root/sub/file.txt");
        Path nonAncestorParent = Paths.get("other/root");
        BasicFileAttributes attrs = mock(BasicFileAttributes.class);
        visitor.visitFile(visitedFile, attrs);

        // Act
        visitor.relativizeFiles(nonAncestorParent, false, null); // This should throw
    }

    @Test(expected = IllegalArgumentException.class)
    public void relativizeDirectories_shouldThrowException_whenParentPathIsNotAnAncestor() throws IOException {
        // Arrange
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters();
        Path visitedDir = Paths.get("root/sub/dir");
        Path nonAncestorParent = Paths.get("other/root");
        visitor.postVisitDirectory(visitedDir, null);

        // Act
        visitor.relativizeDirectories(nonAncestorParent, false, null); // This should throw
    }

    @Test(expected = NullPointerException.class)
    public void relativizeFiles_shouldThrowException_forNullParentPath() {
        // Arrange
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();

        // Act
        visitor.relativizeFiles(null, false, null); // Should throw
    }

    @Test(expected = NullPointerException.class)
    public void relativizeDirectories_shouldThrowException_forNullParentPath() {
        // Arrange
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();

        // Act
        visitor.relativizeDirectories(null, false, null); // Should throw
    }

    @Test(expected = NullPointerException.class)
    public void visitFile_shouldThrowException_forNullAttributes() throws IOException {
        // Arrange
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters();
        Path file = Paths.get("a.txt");

        // Act
        visitor.visitFile(file, null); // Should throw inside updateFileCounters
    }

    @Test(expected = NullPointerException.class)
    public void postVisitDirectory_shouldThrowException_forNullPath() throws IOException {
        // Arrange
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters();

        // Act
        visitor.postVisitDirectory(null, new IOException()); // Should throw inside updateDirCounter
    }

    // --- Tests for deprecated constructors ---

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("deprecation")
    public void constructor_shouldThrowException_forNullFileFilter() {
        // Arrange
        Counters.PathCounters counters = Counters.longPathCounters();
        PathFilter dirFilter = TrueFileFilter.INSTANCE; // A non-null filter

        // Act
        new AccumulatorPathVisitor(counters, null, dirFilter); // Should throw
    }

    // --- Tests for equals() and hashCode() ---

    @Test
    public void equals_shouldReturnTrue_forTwoIdenticalEmptyVisitors() {
        // Arrange
        AccumulatorPathVisitor visitor1 = new AccumulatorPathVisitor();
        AccumulatorPathVisitor visitor2 = new AccumulatorPathVisitor();

        // Assert
        assertTrue(visitor1.equals(visitor2));
        assertTrue(visitor2.equals(visitor1));
        assertEquals(visitor1.hashCode(), visitor2.hashCode());
    }

    @Test
    public void equals_shouldReturnTrue_forSameInstance() {
        // Arrange
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();

        // Assert
        assertTrue(visitor.equals(visitor));
    }

    @Test
    public void equals_shouldReturnFalse_whenComparedToNull() {
        // Arrange
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();

        // Assert
        assertFalse(visitor.equals(null));
    }

    @Test
    public void equals_shouldReturnFalse_whenComparedToDifferentClass() {
        // Arrange
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        Object other = new Object();

        // Assert
        assertFalse(visitor.equals(other));
    }

    @Test
    public void equals_shouldReturnFalse_whenFileListsDiffer() throws IOException {
        // Arrange
        AccumulatorPathVisitor visitor1 = new AccumulatorPathVisitor();
        AccumulatorPathVisitor visitor2 = new AccumulatorPathVisitor();
        
        // Modify visitor2 by visiting a file
        Path file = Paths.get("file.txt");
        BasicFileAttributes attrs = mock(BasicFileAttributes.class);
        visitor2.visitFile(file, attrs);

        // Assert
        assertFalse(visitor1.equals(visitor2));
    }

    @Test
    public void equals_shouldReturnFalse_whenDirectoryListsDiffer() throws IOException {
        // Arrange
        AccumulatorPathVisitor visitor1 = new AccumulatorPathVisitor();
        AccumulatorPathVisitor visitor2 = new AccumulatorPathVisitor();

        // Modify visitor2 by visiting a directory
        Path dir = Paths.get("some/dir");
        visitor2.postVisitDirectory(dir, null);

        // Assert
        assertFalse(visitor1.equals(visitor2));
    }

    @Test
    public void equals_shouldReturnTrue_forEmptyVisitorsWithDifferentCounterTypes() {
        // Arrange
        AccumulatorPathVisitor longCounterVisitor = AccumulatorPathVisitor.withLongCounters();
        AccumulatorPathVisitor bigIntCounterVisitor = AccumulatorPathVisitor.withBigIntegerCounters();

        // Assert
        // The underlying counters are both zero, so they should be equal.
        assertTrue(longCounterVisitor.equals(bigIntCounterVisitor));
        assertEquals(longCounterVisitor.hashCode(), bigIntCounterVisitor.hashCode());
    }
}