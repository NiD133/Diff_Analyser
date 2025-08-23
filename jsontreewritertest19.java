package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.Strictness;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Test for {@link JsonTreeWriter} focusing on strictness handling.
 */
public class JsonTreeWriterTest {

  @Test
  public void writeNonFiniteNumbers_withLegacyStrictness_throwsException() throws IOException {
    // Arrange
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setStrictness(Strictness.LEGACY_STRICT);
    // The writer must be inside an array or object to write a value.
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
    // In legacy strict mode, writing non-finite numbers is forbidden.
    for (Number number : nonFiniteNumbers) {
      IllegalArgumentException e =
          assertThrows(IllegalArgumentException.class, () -> writer.value(number));
      assertThat(e)
          .hasMessageThat()
          .isEqualTo("Numeric values must be finite, but was " + number);
    }
  }
}