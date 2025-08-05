/*
 * This test suite has been refactored from an auto-generated version by EvoSuite.
 * The goal of the refactoring is to improve readability, maintainability, and to
 * clearly express the intended behavior of the DefaultSplitCharacter class.
 *
 * Irrelevant test cases that did not test the target class have been removed.
 * Test methods have been renamed to follow a behavior-driven style.
 * Variables have been given meaningful names, and the test structure
 * now follows the Arrange-Act-Assert pattern.
 */
package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link DefaultSplitCharacter}.
 */
public class DefaultSplitCharacterTest {

    // --- Tests for the isSplitCharacter() method ---

    @Test
    public void isSplitCharacter_withDefaultImplementation_returnsTrueForHyphen() {
        // Arrange
        SplitCharacter defaultSplitter = DefaultSplitCharacter.DEFAULT;
        char[] text = {'a', '-', 'b'};

        // Act
        boolean isSplitChar = defaultSplitter.isSplitCharacter(0, 1, 2, text, null);

        // Assert
        assertTrue("The default implementation should split on a hyphen.", isSplitChar);
    }

    @Test
    public void isSplitCharacter_withDefaultImplementation_returnsTrueForSpace() {
        // Arrange
        SplitCharacter defaultSplitter = DefaultSplitCharacter.DEFAULT;
        char[] text = {'a', ' ', 'b'};

        // Act
        boolean isSplitChar = defaultSplitter.isSplitCharacter(0, 1, 2, text, null);

        // Assert
        assertTrue("The default implementation should split on a space.", isSplitChar);
    }

    @Test
    public void isSplitCharacter_withDefaultImplementation_returnsTrueForControlCharacter() {
        // Arrange
        SplitCharacter defaultSplitter = DefaultSplitCharacter.DEFAULT;
        // The null character (\u0000) is a control character and should be a split point.
        char[] text = {'\u0000'};

        // Act
        boolean isSplitChar = defaultSplitter.isSplitCharacter(0, 0, 0, text, null);

        // Assert
        assertTrue("The default implementation should split on control characters.", isSplitChar);
    }

    @Test
    public void isSplitCharacter_withCustomImplementation_returnsTrueForCustomCharacter() {
        // Arrange
        // This custom splitter should only split on a semicolon ';'.
        SplitCharacter customSplitter = new DefaultSplitCharacter(';');
        char[] text = {'a', ';', 'b'};

        // Act
        boolean isSplitChar = customSplitter.isSplitCharacter(0, 1, 2, text, null);

        // Assert
        assertTrue("A custom splitter should recognize its specified split character.", isSplitChar);
    }

    @Test
    public void isSplitCharacter_withCustomImplementation_returnsFalseForDefaultCharacter() {
        // Arrange
        // This custom splitter should only split on a semicolon ';'.
        SplitCharacter customSplitter = new DefaultSplitCharacter(';');
        // A hyphen is a default split character, but should be ignored by this custom splitter.
        char[] text = {'a', '-', 'b'};

        // Act
        boolean isSplitChar = customSplitter.isSplitCharacter(0, 1, 2, text, null);

        // Assert
        assertFalse("A custom splitter should not split on default characters like hyphen.", isSplitChar);
    }

    // --- Tests for the getCurrentCharacter() method ---

    @Test
    public void getCurrentCharacter_returnsCorrectCharacterAtIndex() {
        // Arrange
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();
        char[] text = "hello".toCharArray();

        // Act
        char result = splitCharacter.getCurrentCharacter(1, text, null);

        // Assert
        assertEquals('e', result);
    }

    // --- Exception Handling Tests ---

    @Test(expected = NullPointerException.class)
    public void isSplitCharacter_withNullCharacterArray_throwsNullPointerException() {
        // Arrange
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();

        // Act
        splitCharacter.isSplitCharacter(0, 0, 0, null, null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void isSplitCharacter_withInvalidIndex_throwsArrayIndexOutOfBoundsException() {
        // Arrange
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();
        char[] emptyArray = new char[0];

        // Act
        splitCharacter.isSplitCharacter(0, -1, 0, emptyArray, null);
    }

    @Test(expected = NullPointerException.class)
    public void getCurrentCharacter_withNullCharacterArray_throwsNullPointerException() {
        // Arrange
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();

        // Act
        splitCharacter.getCurrentCharacter(0, null, null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getCurrentCharacter_withOutOfBoundsIndex_throwsArrayIndexOutOfBoundsException() {
        // Arrange
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();
        char[] text = {'a'};

        // Act
        splitCharacter.getCurrentCharacter(100, text, null);
    }
}