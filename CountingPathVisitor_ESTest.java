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

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link CountingPathVisitor}.
 */
@DisplayName("CountingPathVisitor Test")
class CountingPathVisitorTest {

    private BasicFileAttributes fileAttributes;
    private BasicFileAttributes dirAttributes;
    private final Path testFile = Paths.get("test.txt");
    private final Path testDir = Paths.get("test-dir");

    @BeforeEach
    void setUp() {
        fileAttributes = mockFileAttributes(1024L, false);
        dirAttributes = mockFileAttributes(0L, true);
    }

    private BasicFileAttributes mockFileAttributes(long size, boolean isDirectory) {
        BasicFileAttributes attrs = mock(BasicFileAttributes.class);
        doReturn(size).when(attrs).size();
        doReturn(isDirectory).when(attrs).isDirectory();
        return attrs;
    }

    @Nested
    @DisplayName("Visitor Operations")
    class VisitorOperationTest {

        @Test
        @DisplayName("visitFile should update counters and return CONTINUE when file is accepted")
        void visitFile_whenFileIsAccepted_updatesCountersAndReturnsContinue() throws IOException {
            // Arrange
            CountingPathVisitor visitor = new CountingPathVisitor(Counters.longPathCounters(), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

            // Act
            FileVisitResult result = visitor.visitFile(testFile, fileAttributes);

            // Assert
            assertEquals(FileVisitResult.CONTINUE, result);
            assertEquals(1, visitor.getPathCounters().getFileCounter().get());
            assertEquals(1024, visitor.getPathCounters().getByteCounter().get());
        }

        @Test
        @DisplayName("visitFile should not update counters and return CONTINUE when file is rejected")
        void visitFile_whenFileIsRejected_doesNotUpdateCountersAndReturnsContinue() throws IOException {
            // Arrange
            CountingPathVisitor visitor = new CountingPathVisitor(Counters.longPathCounters(), FalseFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

            // Act
            FileVisitResult result = visitor.visitFile(testFile, fileAttributes);

            // Assert
            assertEquals(FileVisitResult.CONTINUE, result);
            assertEquals(0, visitor.getPathCounters().getFileCounter().get());
            assertEquals(0, visitor.getPathCounters().getByteCounter().get());
        }

        @Test
        @DisplayName("preVisitDirectory should update counter and return CONTINUE when directory is accepted")
        void preVisitDirectory_whenDirectoryIsAccepted_updatesCounterAndReturnsContinue() throws IOException {
            // Arrange
            CountingPathVisitor visitor = new CountingPathVisitor(Counters.longPathCounters(), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

            // Act
            FileVisitResult result = visitor.preVisitDirectory(testDir, dirAttributes);

            // Assert
            assertEquals(FileVisitResult.CONTINUE, result);
            assertEquals(1, visitor.getPathCounters().getDirectoryCounter().get());
        }

        @Test
        @DisplayName("preVisitDirectory should return SKIP_SUBTREE when directory is rejected")
        void preVisitDirectory_whenDirectoryIsRejected_returnsSkipSubtree() throws IOException {
            // Arrange
            CountingPathVisitor visitor = new CountingPathVisitor(Counters.longPathCounters(), TrueFileFilter.INSTANCE, FalseFileFilter.INSTANCE);

            // Act
            FileVisitResult result = visitor.preVisitDirectory(testDir, dirAttributes);

            // Assert
            assertEquals(FileVisitResult.SKIP_SUBTREE, result);
            assertEquals(0, visitor.getPathCounters().getDirectoryCounter().get());
        }

        @Test
        @DisplayName("postVisitDirectory should return CONTINUE even with an exception")
        void postVisitDirectory_withException_returnsContinue() throws IOException {
            // Arrange
            CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
            IOException ioException = new IOException("Test Exception");

            // Act
            FileVisitResult result = visitor.postVisitDirectory(testDir, ioException);

            // Assert
            assertEquals(FileVisitResult.CONTINUE, result);
        }
    }

    @Nested
    @DisplayName("Counter Updates and State")
    class CounterUpdatesTest {

        @Test
        @DisplayName("updateFileCounters should increment file count and byte size")
        void updateFileCounters_incrementsFileCountAndSize() {
            // Arrange
            CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();

            // Act
            visitor.updateFileCounters(testFile, fileAttributes);

            // Assert
            Counters.PathCounters counters = visitor.getPathCounters();
            assertEquals(1, counters.getFileCounter().get());
            assertEquals(0, counters.getDirectoryCounter().get());
            assertEquals(1024, counters.getByteCounter().get());
        }

        @Test
        @DisplayName("updateDirCounter should increment directory count")
        void updateDirCounter_incrementsDirectoryCount() {
            // Arrange
            CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();

            // Act
            visitor.updateDirCounter(testDir, null);

            // Assert
            Counters.PathCounters counters = visitor.getPathCounters();
            assertEquals(0, counters.getFileCounter().get());
            assertEquals(1, counters.getDirectoryCounter().get());
            assertEquals(0, counters.getByteCounter().get());
        }

        @Test
        @DisplayName("toString should reflect the current counter values")
        void toString_reflectsCurrentCounts() {
            // Arrange
            CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
            assertEquals("0 files, 0 directories, 0 bytes", visitor.toString(), "Initial state should be all zeros.");

            // Act
            visitor.updateFileCounters(testFile, fileAttributes);
            visitor.updateDirCounter(testDir, null);

            // Assert
            assertEquals("1 files, 1 directories, 1024 bytes", visitor.toString(), "toString should reflect updated counts.");
        }
    }

    @Nested
    @DisplayName("Constructors and Factories")
    class ConstructorAndFactoryTest {

        @Test
        @DisplayName("withLongCounters factory should create a visitor with long counters")
        void withLongCounters_createsVisitorWithLongCounters() {
            // Arrange & Act
            CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
            
            // Assert
            assertNotNull(visitor);
            assertNotNull(visitor.getPathCounters());
            assertEquals(0L, visitor.getPathCounters().getByteCounter().get());
        }

        @Test
        @DisplayName("withBigIntegerCounters factory should create a visitor with BigInteger counters")
        void withBigIntegerCounters_createsVisitorWithBigIntegerCounters() {
            // Arrange & Act
            AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters();

            // Assert
            assertNotNull(visitor);
            assertNotNull(visitor.getPathCounters());
            assertEquals(0L, visitor.getPathCounters().getByteCounter().get().longValue());
        }

        @Test
        @DisplayName("constructor with null PathCounters should use default counters")
        void constructor_withNullPathCounters_usesDefaultCounters() {
            // Arrange & Act
            CountingPathVisitor visitor = new CountingPathVisitor(null);

            // Assert
            assertNotNull(visitor.getPathCounters());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Contract")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("equals should be reflexive")
        void equals_isReflexive() {
            CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
            assertEquals(visitor, visitor);
        }

        @Test
        @DisplayName("equals should return false for null")
        void equals_isFalseForNull() {
            CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
            assertNotEquals(null, visitor);
        }

        @Test
        @DisplayName("equals should return false for different class")
        void equals_isFalseForDifferentClass() {
            CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
            assertNotEquals(visitor, "a string");
        }

        @Test
        @DisplayName("equals should return true for visitors with identical state")
        void equals_isTrueForIdenticalVisitors() {
            CountingPathVisitor visitor1 = CountingPathVisitor.withLongCounters();
            CountingPathVisitor visitor2 = CountingPathVisitor.withLongCounters();
            assertEquals(visitor1, visitor2);
        }

        @Test
        @DisplayName("equals should return false for visitors with different counter types")
        void equals_isFalseForVisitorsWithDifferentCounterTypes() {
            CountingPathVisitor longVisitor = CountingPathVisitor.withLongCounters();
            CountingPathVisitor bigIntVisitor = CountingPathVisitor.withBigIntegerCounters();
            assertNotEquals(longVisitor, bigIntVisitor);
        }

        @Test
        @DisplayName("hashCode should be consistent for equal objects")
        void hashCode_isConsistentForEqualObjects() {
            CountingPathVisitor visitor1 = CountingPathVisitor.withLongCounters();
            CountingPathVisitor visitor2 = CountingPathVisitor.withLongCounters();
            assertEquals(visitor1, visitor2);
            assertEquals(visitor1.hashCode(), visitor2.hashCode());
        }
    }

    @Nested
    @DisplayName("Exception Handling")
    class ExceptionHandlingTest {

        @Test
        @DisplayName("constructor with null fileFilter should throw NullPointerException")
        void constructor_withNullFileFilter_throwsNullPointerException() {
            // Arrange
            PathCounters counters = Counters.longPathCounters();

            // Act & Assert
            NullPointerException e = assertThrows(NullPointerException.class, () ->
                new CountingPathVisitor(counters, null, TrueFileFilter.INSTANCE)
            );
            assertEquals("fileFilter", e.getMessage());
        }

        @Test
        @DisplayName("visitFile with null attributes should throw NullPointerException")
        void visitFile_withNullAttributes_throwsNullPointerException() {
            // Arrange
            CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();

            // Act & Assert
            assertThrows(NullPointerException.class, () ->
                visitor.visitFile(testFile, null)
            );
        }

        @Test
        @DisplayName("updateFileCounters with null path should throw NullPointerException")
        void updateFileCounters_withNullPath_throwsNullPointerException() {
            // Arrange
            CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();

            // Act & Assert
            assertThrows(NullPointerException.class, () ->
                visitor.updateFileCounters(null, fileAttributes)
            );
        }
    }
}