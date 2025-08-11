package com.google.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import org.junit.Test;

import java.io.StringReader;
import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ToNumberPolicyTest {

  private static final double EPS = 1e-10;

  private static JsonReader newReader(String json) {
    return new JsonReader(new StringReader(json));
  }

  private static JsonReader newLenientReader(String json) {
    JsonReader reader = newReader(json);
    reader.setStrictness(Strictness.LENIENT);
    return reader;
  }

  @Test
  public void nullReader_throwsNPE_forAllPolicies() {
    for (ToNumberPolicy policy : ToNumberPolicy.values()) {
      assertThrows(NullPointerException.class, () -> policy.readNumber(null));
    }
  }

  @Test
  public void longOrDouble_readsIntegerAsLong() throws Exception {
    JsonReader reader = newReader("42");
    Number result = ToNumberPolicy.LONG_OR_DOUBLE.readNumber(reader);

    assertTrue("Expected Long for integer JSON number", result instanceof Long);
    assertEquals(42L, result.longValue());
  }

  @Test
  public void longOrDouble_readsFractionAsDouble() throws Exception {
    JsonReader reader = newReader("3.14");
    Number result = ToNumberPolicy.LONG_OR_DOUBLE.readNumber(reader);

    assertTrue("Expected Double for fractional JSON number", result instanceof Double);
    assertEquals(3.14, result.doubleValue(), EPS);
  }

  @Test
  public void longOrDouble_strict_rejectsInfiniteDouble() {
    // 1e309 overflows to Infinity for double; STRICT mode should reject it
    JsonReader reader = newReader("1e309");

    assertThrows(MalformedJsonException.class,
        () -> ToNumberPolicy.LONG_OR_DOUBLE.readNumber(reader));
  }

  @Test
  public void longOrDouble_lenient_allowsInfiniteDouble() throws Exception {
    // In LENIENT mode, Infinity is allowed and returned as a Double
    JsonReader reader = newLenientReader("1e309");
    Number result = ToNumberPolicy.LONG_OR_DOUBLE.readNumber(reader);

    assertTrue(result instanceof Double);
    assertTrue("Expected infinite double value", Double.isInfinite(result.doubleValue()));
  }

  @Test
  public void doublePolicy_alwaysReturnsDouble() throws Exception {
    JsonReader reader1 = newReader("42");
    Number result1 = ToNumberPolicy.DOUBLE.readNumber(reader1);
    assertTrue(result1 instanceof Double);
    assertEquals(42.0, result1.doubleValue(), EPS);

    JsonReader reader2 = newReader("3.5");
    Number result2 = ToNumberPolicy.DOUBLE.readNumber(reader2);
    assertTrue(result2 instanceof Double);
    assertEquals(3.5, result2.doubleValue(), EPS);
  }

  @Test
  public void lazilyParsedNumber_parsesOnDemand() throws Exception {
    // LAZILY_PARSED_NUMBER should defer parsing until a numeric accessor is called
    JsonReader reader = newReader("123");
    Number result = ToNumberPolicy.LAZILY_PARSED_NUMBER.readNumber(reader);

    // We do not rely on the concrete class; instead verify numeric access works when needed
    assertEquals(123, result.intValue());
    assertEquals(123L, result.longValue());
    assertEquals(123.0, result.doubleValue(), EPS);
  }

  @Test
  public void bigDecimal_returnsExactRepresentation() throws Exception {
    // BigDecimal should preserve the exact textual representation, including scale
    JsonReader reader = newReader("3.1400");
    Number result = ToNumberPolicy.BIG_DECIMAL.readNumber(reader);

    assertTrue(result instanceof BigDecimal);
    assertEquals(new BigDecimal("3.1400"), result);
  }
}