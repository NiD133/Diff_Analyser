package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

public class JavaVersionTestTest2 {

    @Test
    public void testJava6() {
        // http://www.oracle.com/technetwork/java/javase/version-6-141920.html
        assertThat(JavaVersion.parseMajorJavaVersion("1.6.0")).isEqualTo(6);
    }
}
