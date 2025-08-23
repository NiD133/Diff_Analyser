package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import java.lang.reflect.Field;
import java.util.Locale;
import org.junit.Test;

public class FieldNamingPolicyTestTest4 {

    /**
     * Lower casing policies should be unaffected by default Locale.
     */
    @Test
    public void testLowerCasingLocaleIndependent() throws Exception {
        class Dummy {

            @SuppressWarnings({ "unused", "ConstantField" })
            int I;
        }
        FieldNamingPolicy[] policies = { FieldNamingPolicy.LOWER_CASE_WITH_DASHES, FieldNamingPolicy.LOWER_CASE_WITH_DOTS, FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES };
        Field field = Dummy.class.getDeclaredField("I");
        String name = field.getName();
        String expected = name.toLowerCase(Locale.ROOT);
        Locale oldLocale = Locale.getDefault();
        // Set Turkish as Locale which has special case conversion rules
        Locale.setDefault(new Locale("tr"));
        try {
            // Verify that default Locale has different case conversion rules
            assertWithMessage("Test setup is broken").that(name.toLowerCase(Locale.getDefault())).doesNotMatch(expected);
            for (FieldNamingPolicy policy : policies) {
                // Should ignore default Locale
                assertWithMessage("Unexpected conversion for %s", policy).that(policy.translateName(field)).matches(expected);
            }
        } finally {
            Locale.setDefault(oldLocale);
        }
    }
}
