package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

public class JavaVersionTestTest1 {

    @Test
    public void testGetMajorJavaVersion() {
        // Gson currently requires at least Java 8
        assertThat(JavaVersion.getMajorJavaVersion()).isAtLeast(8);
    }
}
