package com.fasterxml.jackson.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

// The original class name is kept for consistency.
public class UTF8WriterTestTest5 extends com.fasterxml.jackson.core.JUnit5TestBase {

    private IOContext _ioContext() {
        return testIOContext();
    }

    @Nested
    @DisplayName("when writing invalid surrogate pairs as integers")
    class WriteInvalidInts {

        @Test
        @DisplayName("should throw IOException for a lone low surrogate")
        void writeLoneLowSurrogateInt() {
            // Arrange
            final int LONE_LOW_SURROGATE = 0xDE03;
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            // Act & Assert
            IOException e = assertThrows(IOException.class, () -> {
                try (UTF8Writer writer = new UTF8Writer(_ioContext(), out)) {
                    writer.write(LONE_LOW_SURROGATE);
                }
            });

            verifyException(e, "Unmatched second part");
        }

        @Test
        @DisplayName("should throw IOException for a high surrogate followed by a non-surrogate")
        void writeHighSurrogateFollowedByNonSurrogateInt() {
            // Arrange
            final int HIGH_SURROGATE = 0xD83D;
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            // Act & Assert
            IOException e = assertThrows(IOException.class, () -> {
                try (UTF8Writer writer = new UTF8Writer(_ioContext(), out)) {
                    writer.write(HIGH_SURROGATE);
                    writer.write('a'); // Not a low surrogate
                }
            });

            verifyException(e, "Broken surrogate pair");
        }
    }

    @Nested
    @DisplayName("when writing invalid surrogate pairs as strings")
    class WriteInvalidStrings {

        @Test
        @DisplayName("should throw IOException for a string with a lone low surrogate")
        void writeLoneLowSurrogateString() {
            // Arrange
            final String LONE_LOW_SURROGATE_STR = "\uDE03";
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            // Act & Assert
            IOException e = assertThrows(IOException.class, () -> {
                try (UTF8Writer writer = new UTF8Writer(_ioContext(), out)) {
                    writer.write(LONE_LOW_SURROGATE_STR);
                }
            });

            verifyException(e, "Unmatched second part");
        }

        @Test
        @DisplayName("should throw IOException for a string with a high surrogate followed by a non-surrogate")
        void writeHighSurrogateFollowedByNonSurrogateString() {
            // Arrange
            final String BROKEN_SURROGATE_PAIR_STR = "\uD83Da";
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            // Act & Assert
            IOException e = assertThrows(IOException.class, () -> {
                try (UTF8Writer writer = new UTF8Writer(_ioContext(), out)) {
                    writer.write(BROKEN_SURROGATE_PAIR_STR);
                }
            });

            verifyException(e, "Broken surrogate pair");
        }
    }
}