package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.truth.Truth.assertThat;

import com.google.common.annotations.GwtCompatible;
import junit.framework.TestCase;

@GwtCompatible // All CaseFormat methods are GwtCompatible
public class CaseFormatTestTest30 extends TestCase {

  /**
   * The contract of a Guava Converter is to return null when given a null input. This test
   * verifies this behavior for CaseFormat converters.
   */
  public void testConverter_givenNullInput_returnsNull() {
    // The null-handling behavior is defined in the base Converter class and is the same for all
    // format combinations. We use one representative example to verify this contract.
    Converter<String, String> converter = LOWER_HYPHEN.converterTo(UPPER_CAMEL);

    // Test that a forward conversion of null results in null.
    assertThat(converter.convert(null)).isNull();

    // Test that a reverse conversion of null also results in null.
    assertThat(converter.reverse().convert(null)).isNull();
  }
}