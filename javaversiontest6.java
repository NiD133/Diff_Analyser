package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

public class JavaVersionTestTest6 {

    @Test
    public void testJava10() {
        // Oracle JDK 10.0.1
        assertThat(JavaVersion.parseMajorJavaVersion("10.0.1")).isEqualTo(10);
    }
}
