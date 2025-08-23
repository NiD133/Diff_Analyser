package com.google.common.net;

import org.junit.Test;
import java.text.ParseException;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link HostSpecifier}.
 */
public class HostSpecifierTest {

    /**
     * Verifies that two equal HostSpecifier instances produce the same hash code,
     * as required by the Object.hashCode() contract.
     */
    @Test
    public void hashCode_forEqualInstances_returnsSameValue() throws ParseException {
        // Arrange: Create two separate HostSpecifier instances from the same valid IP address string.
        // These two objects should be considered equal.
        HostSpecifier specifier1 = HostSpecifier.from("127.0.0.1");
        HostSpecifier specifier2 = HostSpecifier.from("127.0.0.1");

        // Act & Assert:
        // First, confirm that the instances are indeed equal to each other.
        assertEquals("Instances created from the same string should be equal", specifier1, specifier2);

        // Then, assert that their hash codes are also equal, fulfilling the contract.
        assertEquals("Equal instances must have equal hash codes", specifier1.hashCode(), specifier2.hashCode());
    }
}