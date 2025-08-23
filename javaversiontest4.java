package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

public class JavaVersionTestTest4 {

    @Test
    public void testJava8() {
        assertThat(JavaVersion.parseMajorJavaVersion("1.8")).isEqualTo(8);
        assertThat(JavaVersion.parseMajorJavaVersion("1.8.0")).isEqualTo(8);
        assertThat(JavaVersion.parseMajorJavaVersion("1.8.0_131")).isEqualTo(8);
        assertThat(JavaVersion.parseMajorJavaVersion("1.8.0_60-ea")).isEqualTo(8);
        assertThat(JavaVersion.parseMajorJavaVersion("1.8.0_111-internal")).isEqualTo(8);
        // openjdk8 per https://github.com/AdoptOpenJDK/openjdk-build/issues/93
        assertThat(JavaVersion.parseMajorJavaVersion("1.8.0-internal")).isEqualTo(8);
        assertThat(JavaVersion.parseMajorJavaVersion("1.8.0_131-adoptopenjdk")).isEqualTo(8);
    }
}
