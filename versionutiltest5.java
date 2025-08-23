package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import static org.junit.jupiter.api.Assertions.*;

public class VersionUtilTestTest5 {

    @Test
    void parseVersionWithEmptyStringAndEmptyString() {
        Version version = VersionUtil.parseVersion("", "", "\"g2AT");
        assertTrue(version.isUnknownVersion());
    }
}
