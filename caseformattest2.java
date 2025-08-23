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

public class CaseFormatTestTest2 extends TestCase {

    @J2ktIncompatible
    // NullPointerTester
    @GwtIncompatible
    public void testNullArguments() {
        NullPointerTester tester = new NullPointerTester();
        tester.testAllPublicStaticMethods(CaseFormat.class);
        for (CaseFormat format : CaseFormat.values()) {
            tester.testAllPublicInstanceMethods(format);
        }
    }
}
