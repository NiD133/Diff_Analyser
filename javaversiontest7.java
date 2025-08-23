package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

public class JavaVersionTestTest7 {

    @Test
    public void testUnknownVersionFormat() {
        // unknown format
        assertThat(JavaVersion.parseMajorJavaVersion("Java9")).isEqualTo(6);
    }
}
