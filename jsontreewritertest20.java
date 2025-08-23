package com.google.gson.internal.bind;

import static org.junit.Assert.assertThrows;

import com.google.gson.Strictness;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter} focusing on strictness handling.
 */
public class JsonTreeWriterStrictnessTest {

  @Test
  public void write_withLegacyStrict_throwsForNonFiniteNumbers() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setStrictness(Strictness.LEGACY_STRICT);
    // The writer must be in an array or object context to write a value.
    writer.beginArray();

    List<Number> nonFiniteNumbers =
        Arrays.asList(
            Float.NaN,
            Float.NEGATIVE_INFINITY,
            Float.POSITIVE_INFINITY,
            Double.NaN,
            Double.NEGATIVE_INFINITY,
            Double.POSITIVE_INFINITY);

    // Act & Assert
    // Verify that writing each non-finite number is illegal in legacy strict mode.
    for (Number number : nonFiniteNumbers) {
      assertThrows(
          "Should throw for non-finite value: " + number,
          IllegalArgumentException.class,
          () -> writer.value(number));
    }
  }
}