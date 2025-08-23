package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import java.lang.reflect.Field;
import java.util.Locale;
import org.junit.Test;

public class FieldNamingPolicyTestTest2 {

    @Test
    public void testUpperCaseFirstLetter() {
        // Map from original -> expected
        String[][] argumentPairs = { { "a", "A" }, { "ab", "Ab" }, { "AB", "AB" }, { "_a", "_A" }, { "_ab", "_Ab" }, { "__", "__" }, { "_1", "_1" }, // Not a letter, but has uppercase variant (should not be uppercased)
        // See https://github.com/google/gson/issues/1965
        { "\u2170", "\u2170" }, { "_\u2170", "_\u2170" }, { "\u2170a", "\u2170A" } };
        for (String[] pair : argumentPairs) {
            assertThat(FieldNamingPolicy.upperCaseFirstLetter(pair[0])).isEqualTo(pair[1]);
        }
    }
}
