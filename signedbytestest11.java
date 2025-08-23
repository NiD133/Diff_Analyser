package com.google.common.primitives;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.annotations.GwtCompatible;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link SignedBytes#sortDescending(byte[])}.
 */
@GwtCompatible
@RunWith(JUnit4.class)
public class SignedBytesSortDescendingTest {

  @Test
  public void sortDescending_onEmptyArray_doesNothing() {
    // Arrange
    byte[] actual = {};
    byte[] expected = {};

    // Act
    SignedBytes.sortDescending(actual);

    // Assert
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void sortDescending_onSingleElementArray_doesNothing() {
    // Arrange
    byte[] actual = {42};
    byte[] expected = {42};

    // Act
    SignedBytes.sortDescending(actual);

    // Assert
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void sortDescending_onTwoElementArray_sortsCorrectly() {
    // Arrange
    byte[] actual = {1, 2};
    byte[] expected = {2, 1};

    // Act
    SignedBytes.sortDescending(actual);

    // Assert
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void sortDescending_onArrayWithDuplicates_sortsCorrectly() {
    // Arrange
    byte[] actual = {1, 3, 1};
    byte[] expected = {3, 1, 1};

    // Act
    SignedBytes.sortDescending(actual);

    // Assert
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void sortDescending_onArrayWithMixedSigns_sortsCorrectly() {
    // Arrange
    byte[] actual = {-1, 1, -2, 2};
    byte[] expected = {2, 1, -1, -2};

    // Act
    SignedBytes.sortDescending(actual);

    // Assert
    assertThat(actual).isEqualTo(expected);
  }
}