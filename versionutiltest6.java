package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import static org.junit.jupiter.api.Assertions.*;

public class VersionUtilTestTest6 {

    @Test
    void parseVersionWithNullAndEmptyString() {
        Version version = VersionUtil.parseVersion(null, "/nUmRN)3", "");
        assertFalse(version.isSnapshot());
    }
}
