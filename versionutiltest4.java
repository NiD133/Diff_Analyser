package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import static org.junit.jupiter.api.Assertions.*;

public class VersionUtilTestTest4 {

    @Test
    void parseVersionReturningVersionWhereGetMajorVersionIsZero() {
        Version version = VersionUtil.parseVersion("#M&+m@569P", "#M&+m@569P", "com.fasterxml.jackson.core.util.VersionUtil");
        assertEquals(0, version.getMinorVersion());
        assertEquals(0, version.getPatchLevel());
        assertEquals(0, version.getMajorVersion());
        assertFalse(version.isSnapshot());
        assertFalse(version.isUnknownVersion());
    }
}
