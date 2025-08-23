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

public class CaseFormatTestTest29 extends TestCase {

    public void testConverterToBackward() {
        assertThat(UPPER_UNDERSCORE.converterTo(UPPER_CAMEL).reverse().convert("FooBar")).isEqualTo("FOO_BAR");
        assertThat(UPPER_UNDERSCORE.converterTo(LOWER_CAMEL).reverse().convert("fooBar")).isEqualTo("FOO_BAR");
        assertThat(UPPER_CAMEL.converterTo(UPPER_UNDERSCORE).reverse().convert("FOO_BAR")).isEqualTo("FooBar");
        assertThat(LOWER_CAMEL.converterTo(UPPER_UNDERSCORE).reverse().convert("FOO_BAR")).isEqualTo("fooBar");
    }
}
