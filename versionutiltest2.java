package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import static org.junit.jupiter.api.Assertions.*;

public class VersionUtilTestTest2 {

    @Test
    void versionParsing() {
        assertEquals(new Version(1, 2, 15, "foo", "group", "artifact"), VersionUtil.parseVersion("1.2.15-foo", "group", "artifact"));
        Version v = VersionUtil.parseVersion("1.2.3-SNAPSHOT", "group", "artifact");
        assertEquals("group/artifact/1.2.3-SNAPSHOT", v.toFullString());
    }
}