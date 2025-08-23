package com.google.common.base;

import com.google.common.testing.SerializableTester;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Verifies that for every combination of source and target formats, the {@link Converter} returned
 * by {@link CaseFormat#converterTo(CaseFormat)} is serializable.
 */
@RunWith(Parameterized.class)
public class CaseFormatConverterSerializationTest {

  private final CaseFormat sourceFormat;
  private final CaseFormat targetFormat;

  public CaseFormatConverterSerializationTest(CaseFormat sourceFormat, CaseFormat targetFormat) {
    this.sourceFormat = sourceFormat;
    this.targetFormat = targetFormat;
  }

  @Parameters(name = "{0}_to_{1}")
  public static Collection<Object[]> data() {
    List<Object[]> testCases = new ArrayList<>();
    for (CaseFormat source : CaseFormat.values()) {
      for (CaseFormat target : CaseFormat.values()) {
        testCases.add(new Object[] {source, target});
      }
    }
    return testCases;
  }

  @Test
  public void converterIsSerializable() {
    Converter<String, String> converter = sourceFormat.converterTo(targetFormat);
    SerializableTester.reserializeAndAssert(converter);
  }
}