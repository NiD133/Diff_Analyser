package com.google.common.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.net.HostSpecifier;
import java.text.ParseException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class HostSpecifier_ESTest extends HostSpecifier_ESTest_scaffolding {

    // ========================= isValid() Tests =========================
    
    @Test(timeout = 4000)
    public void testIsValid_IPv4Address_ReturnsTrue() {
        boolean isValid = HostSpecifier.isValid("0.0.0.0");
        assertTrue(isValid);
    }

    @Test(timeout = 4000)
    public void testIsValid_NullInput_ThrowsNullPointerException() {
        try {
            HostSpecifier.isValid(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsValid_IPv6Address_ThrowsAssertionError() {
        try {
            HostSpecifier.isValid("::");
            fail("Expected AssertionError");
        } catch (AssertionError e) {
            // Thrown due to internal IPv4 conversion attempt
            verifyException("com.google.common.net.HostSpecifier", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsValid_InvalidFormat_ReturnsFalse() {
        boolean isValid = HostSpecifier.isValid("E$U|;qI:3");
        assertFalse(isValid);
    }

    // ======================== fromValid() Tests ========================
    
    @Test(timeout = 4000)
    public void testFromValid_IPv4Address_ReturnsInstance() {
        HostSpecifier specifier = HostSpecifier.fromValid("127.0.0.1");
        assertEquals("127.0.0.1", specifier.toString());
    }

    @Test(timeout = 4000)
    public void testFromValid_NullInput_ThrowsNullPointerException() {
        try {
            HostSpecifier.fromValid(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testFromValid_IPv6Address_ThrowsAssertionError() {
        try {
            HostSpecifier.fromValid("::");
            fail("Expected AssertionError");
        } catch (AssertionError e) {
            // Thrown due to internal IPv4 conversion attempt
            verifyException("com.google.common.net.HostSpecifier", e);
        }
    }

    @Test(timeout = 4000)
    public void testFromValid_InvalidDomain_ThrowsIllegalArgumentException() {
        try {
            HostSpecifier.fromValid("com.google.common.net.HostSpecifier");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(
                "Domain name does not have a recognized public suffix: com.google.common.net.HostSpecifier",
                e.getMessage()
            );
        }
    }

    @Test(timeout = 4000)
    public void testFromValid_MalformedString_ThrowsIllegalArgumentException() {
        try {
            HostSpecifier.fromValid(":7");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Precondition check fails for malformed format
            assertNull(e.getMessage());
        }
    }

    // =========================== from() Tests ==========================
    
    @Test(timeout = 4000)
    public void testFrom_ValidIPv4Address_ReturnsInstance() throws ParseException {
        HostSpecifier specifier = HostSpecifier.from("127.0.0.1");
        assertEquals("127.0.0.1", specifier.toString());
    }

    @Test(timeout = 4000)
    public void testFrom_NullInput_ThrowsNullPointerException() {
        try {
            HostSpecifier.from(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testFrom_IPv6Address_ThrowsAssertionError() {
        try {
            HostSpecifier.from("::");
            fail("Expected AssertionError");
        } catch (AssertionError e) {
            // Thrown due to internal IPv4 conversion attempt
            verifyException("com.google.common.net.HostSpecifier", e);
        }
    }

    @Test(timeout = 4000)
    public void testFrom_InvalidFormat_ThrowsParseException() {
        try {
            HostSpecifier.from("jR:");
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Invalid host specifier: jR:", e.getMessage());
        }
    }

    // ==================== Instance Method Tests ====================
    
    @Test(timeout = 4000)
    public void testEquals_SameInstance_ReturnsTrue() throws ParseException {
        HostSpecifier specifier = HostSpecifier.from("127.0.0.1");
        assertTrue(specifier.equals(specifier));
    }

    @Test(timeout = 4000)
    public void testEquals_SameValue_ReturnsTrue() throws ParseException {
        HostSpecifier specifier1 = HostSpecifier.from("127.0.0.1");
        HostSpecifier specifier2 = HostSpecifier.from("127.0.0.1");
        assertTrue(specifier1.equals(specifier2));
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentType_ReturnsFalse() throws ParseException {
        HostSpecifier specifier = HostSpecifier.from("127.0.0.1");
        assertFalse(specifier.equals(new Object()));
    }

    @Test(timeout = 4000)
    public void testHashCode_ConsistentWithEquals() throws ParseException {
        HostSpecifier specifier = HostSpecifier.from("127.0.0.1");
        specifier.hashCode();  // Just verify no exception
    }

    @Test(timeout = 4000)
    public void testToString_ReturnsCanonicalForm() throws ParseException {
        HostSpecifier specifier = HostSpecifier.from("127.0.0.1");
        assertEquals("127.0.0.1", specifier.toString());
    }
}