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

public class CaseFormatTestTest16 extends TestCase {

    public void testLowerCamelToUpperCamel() {
        assertThat(LOWER_CAMEL.to(UPPER_CAMEL, "foo")).isEqualTo("Foo");
        assertThat(LOWER_CAMEL.to(UPPER_CAMEL, "fooBar")).isEqualTo("FooBar");
        assertThat(LOWER_CAMEL.to(UPPER_CAMEL, "hTTP")).isEqualTo("HTTP");
    }
}
