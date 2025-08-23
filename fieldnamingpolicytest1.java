package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import java.lang.reflect.Field;
import java.util.Locale;
import org.junit.Test;

public class FieldNamingPolicyTestTest1 {

    @Test
    public void testSeparateCamelCase() {
        // Map from original -> expected
        String[][] argumentPairs = { { "a", "a" }, { "ab", "ab" }, { "Ab", "Ab" }, { "aB", "a_B" }, { "AB", "A_B" }, { "A_B", "A__B" }, { "firstSecondThird", "first_Second_Third" }, { "__", "__" }, { "_123", "_123" } };
        for (String[] pair : argumentPairs) {
            assertThat(FieldNamingPolicy.separateCamelCase(pair[0], '_')).isEqualTo(pair[1]);
        }
    }
}
