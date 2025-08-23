package com.google.common.escape;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.junit.Test;

/**
 * Readable, focused unit tests for ArrayBasedUnicodeEscaper.
 *
 * These tests exercise:
 * - Application of explicit replacement mappings
 * - Respect for the configured "safe" code point range
 * - Behavior for supplementary characters (surrogate pairs)
 * - Handling of an empty safe range (safeMax < safeMin)
 * - Fast-path behavior when no escaping is necessary (returns same String instance)
 * - Validation of constructor arguments
 */
public class ArrayBasedUnicodeEscaperTest {

  /**
   * Minimal concrete escaper used by the tests. It:
   * - Uses the provided map for explicit replacements
   * - Uses the configured safe range
   * - Escapes everything else using a simple "[U+XXXX]" format
   */
  private static final class TestEscaper extends ArrayBasedUnicodeEscaper {
    TestEscaper(Map<Character, String> replacementMap, int safeMin, int safeMax) {
      super(replacementMap, safeMin, safeMax, null);
    }

    TestEscaper(ArrayBasedEscaperMap escaperMap, int safeMin, int safeMax) {
      super(escaperMap, safeMin, safeMax, null);
    }

    @Override
    protected char[] escapeUnsafe(int cp) {
      String hex = Integer.toHexString(cp).toUpperCase(Locale.ROOT);
      // Left pad to at least 4 digits for readability (e.g., U+000A). Non-BMP may be longer.
      while (hex.length() < 4) {
        hex = "0" + hex;
      }
      return ("[U+" + hex + "]").toCharArray();
    }
  }

  private static Map<Character, String> htmlLikeMap() {
    Map<Character, String> m = new HashMap<>();
    m.put('&', "&amp;");
    m.put('<', "&lt;");
    return m;
  }

  private static String emojiGrinningFace() {
    return new String(Character.toChars(0x1F600)); // 😀
  }

  @Test
  public void appliesExplicitMappings_and_leavesSafeCharsAlone() {
    // Safe ASCII range
    TestEscaper escaper = new TestEscaper(htmlLikeMap(), 0x20, 0x7E);
    String input = "A&B <C>";
    String expected = "A&amp;B &lt;C>";
    assertEquals(expected, escaper.escape(input));
  }

  @Test
  public void escapesUnmappedCharacters_belowSafeRange_viaEscapeUnsafe() {
    TestEscaper escaper = new TestEscaper(new HashMap<>(), 0x20, 0x7E);
    String input = "control:\u0001 end";
    String expected = "control:[U+0001] end";
    assertEquals(expected, escaper.escape(input));
  }

  @Test
  public void escapesSupplementaryCodePoints_viaEscapeUnsafe() {
    TestEscaper escaper = new TestEscaper(htmlLikeMap(), 0x20, 0x7E);
    String input = "emoji: " + emojiGrinningFace() + " done";
    String expected = "emoji: [U+1F600] done";
    assertEquals(expected, escaper.escape(input));
  }

  @Test
  public void emptySafeRange_whenSafeMaxLessThanSafeMin_escapesEverythingUnmapped() {
    // safeMax < safeMin => no code points are safe
    TestEscaper escaper = new TestEscaper(new HashMap<>(), 10, 5);
    String input = "AZ";
    String expected = "[U+0041][U+005A]";
    assertEquals(expected, escaper.escape(input));
  }

  @Test
  public void returnsSameInstance_whenNoEscapingIsNeeded() {
    // Entire Unicode range is considered safe; no mappings mean no changes.
    TestEscaper escaper = new TestEscaper(new HashMap<>(), 0x0000, 0x10FFFF);
    String input = "NoChange";
    String output = escaper.escape(input);
    // Fast-path should return the original String instance
    assertSame(input, output);
  }

  @Test
  public void surrogatePairsAreNotFalselyTreatedAsSafe() {
    // Safe range covers BMP up to 0xFFFF, but surrogate pairs must still be escaped
    TestEscaper escaper = new TestEscaper(new HashMap<>(), 0x0000, 0xFFFF);
    String input = "X" + emojiGrinningFace() + "Y";
    String expected = "X[U+1F600]Y";
    assertEquals(expected, escaper.escape(input));
  }

  @Test
  public void nullReplacementMap_throwsNullPointerException() {
    try {
      new TestEscaper((Map<Character, String>) null, 0x0000, 0x10FFFF);
      fail("Expected NullPointerException");
    } catch (NullPointerException expected) {
      // expected
    }
  }
}