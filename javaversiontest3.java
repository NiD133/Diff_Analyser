package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

public class JavaVersionTestTest3 {

    @Test
    public void testJava7() {
        // http://www.oracle.com/technetwork/java/javase/jdk7-naming-418744.html
        assertThat(JavaVersion.parseMajorJavaVersion("1.7.0")).isEqualTo(7);
    }
}
