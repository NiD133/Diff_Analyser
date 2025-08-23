package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import static org.junit.jupiter.api.Assertions.*;

public class VersionUtilTestTest9 {

    @SuppressWarnings("deprecation")
    @Test
    void mavenVersionParsing() {
        assertEquals(new Version(1, 2, 3, "SNAPSHOT", "foo.bar", "foo-bar"), VersionUtil.mavenVersionFor(VersionUtilTest.class.getClassLoader(), "foo.bar", "foo-bar"));
    }
}
