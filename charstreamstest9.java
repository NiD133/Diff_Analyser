package com.google.common.io;

import static com.google.common.truth.Truth.assertThat;

import java.io.IOException;
import java.io.StringReader;
import java.nio.CharBuffer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link CharStreams#copy(Readable, Appendable)}.
 *
 * <p>This test focuses on the generic code path by wrapping the source {@code Readable} to prevent
 * type-specific optimizations.
 */
@RunWith(JUnit4.class)
public class CharStreamsCopyFromReadableTest extends IoTestCase {

  /**
   * Wraps a {@code Readable} in a new anonymous class to prevent type-specific optimizations in the
   * code under test. This ensures that the generic {@code copy(Readable, ...)} implementation is
   * invoked, rather than a specialized overload (e.g., for {@code StringReader}).
   */
  private static Readable asGenericReadable(Readable readable) {
    return new Readable() {
      @Override
      public int read(CharBuffer cb) throws IOException {
        return readable.read(cb);
      }
    };
  }

  @Test
  public void copy_fromGenericReadableToAppendable_copiesAsciiCharacters() throws IOException {
    // Arrange
    String sourceString = ASCII;
    Readable sourceReader = asGenericReadable(new StringReader(sourceString));
    StringBuilder destination = new StringBuilder();

    // Act
    long copiedCharCount = CharStreams.copy(sourceReader, destination);

    // Assert
    assertThat(destination.toString()).isEqualTo(sourceString);
    assertThat(copiedCharCount).isEqualTo(sourceString.length());
  }

  @Test
  public void copy_fromGenericReadableToAppendable_copiesI18nCharacters() throws IOException {
    // Arrange
    String sourceString = I18N;
    Readable sourceReader = asGenericReadable(new StringReader(sourceString));
    StringBuilder destination = new StringBuilder();

    // Act
    long copiedCharCount = CharStreams.copy(sourceReader, destination);

    // Assert
    assertThat(destination.toString()).isEqualTo(sourceString);
    assertThat(copiedCharCount).isEqualTo(sourceString.length());
  }
}