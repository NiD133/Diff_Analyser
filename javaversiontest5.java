package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

public class JavaVersionTestTest5 {

    @Test
    public void testJava9() {
        // Legacy style
        // Oracle JDK 9
        assertThat(JavaVersion.parseMajorJavaVersion("9.0.4")).isEqualTo(9);
        // Debian as reported in https://github.com/google/gson/issues/1310
        assertThat(JavaVersion.parseMajorJavaVersion("9-Debian")).isEqualTo(9);
        // New style
        assertThat(JavaVersion.parseMajorJavaVersion("9-ea+19")).isEqualTo(9);
        assertThat(JavaVersion.parseMajorJavaVersion("9+100")).isEqualTo(9);
        assertThat(JavaVersion.parseMajorJavaVersion("9.0.1+20")).isEqualTo(9);
        assertThat(JavaVersion.parseMajorJavaVersion("9.1.1+20")).isEqualTo(9);
    }
}
