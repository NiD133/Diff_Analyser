package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.testing.NullPointerTester;
import com.google.common.testing.SerializableTester;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

public class CaseFormatTestTest26 extends TestCase {

    public void testUpperUnderscoreToUpperCamel() {
        assertThat(UPPER_UNDERSCORE.to(UPPER_CAMEL, "FOO")).isEqualTo("Foo");
        assertThat(UPPER_UNDERSCORE.to(UPPER_CAMEL, "FOO_BAR")).isEqualTo("FooBar");
        assertThat(UPPER_UNDERSCORE.to(UPPER_CAMEL, "H_T_T_P")).isEqualTo("HTTP");
    }
}
