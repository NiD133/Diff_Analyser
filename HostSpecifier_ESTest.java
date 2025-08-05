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
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class HostSpecifier_ESTest extends HostSpecifier_ESTest_scaffolding {

    // ========== isValid() method tests ==========
    
    @Test(timeout = 4000)
    public void testIsValid_ValidIPv4Address_ReturnsTrue() throws Throwable {
        boolean result = HostSpecifier.isValid("0.0.0.0");
        assertTrue("Valid IPv4 address should be accepted", result);
    }

    @Test(timeout = 4000)
    public void testIsValid_NullInput_ThrowsNullPointerException() throws Throwable {
        try { 
            HostSpecifier.isValid(null);
            fail("Expected NullPointerException for null input");
        } catch(NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsValid_IPv6Address_ThrowsAssertionError() throws Throwable {
        try { 
            HostSpecifier.isValid("::");
            fail("Expected AssertionError for IPv6 address");
        } catch(AssertionError e) {
            // IPv6 addresses are not supported in this implementation
        }
    }

    @Test(timeout = 4000)
    public void testIsValid_InvalidHostSpecifier_ReturnsFalse() throws Throwable {
        boolean result = HostSpecifier.isValid("E$U|;qI:3");
        assertFalse("Invalid host specifier should be rejected", result);
    }

    // ========== fromValid() method tests ==========
    
    @Test(timeout = 4000)
    public void testFromValid_NullInput_ThrowsNullPointerException() throws Throwable {
        try { 
            HostSpecifier.fromValid(null);
            fail("Expected NullPointerException for null input");
        } catch(NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testFromValid_IPv6Address_ThrowsAssertionError() throws Throwable {
        try { 
            HostSpecifier.fromValid("::");
            fail("Expected AssertionError for IPv6 address");
        } catch(AssertionError e) {
            // IPv6 addresses are not supported in this implementation
        }
    }

    @Test(timeout = 4000)
    public void testFromValid_DomainWithoutPublicSuffix_ThrowsIllegalArgumentException() throws Throwable {
        try { 
            HostSpecifier.fromValid("com.google.common.net.HostSpecifier");
            fail("Expected IllegalArgumentException for domain without recognized public suffix");
        } catch(IllegalArgumentException e) {
            verifyException("com.google.common.net.HostSpecifier", e);
        }
    }

    @Test(timeout = 4000)
    public void testFromValid_InvalidFormat_ThrowsIllegalArgumentException() throws Throwable {
        try { 
            HostSpecifier.fromValid(":7");
            fail("Expected IllegalArgumentException for invalid format");
        } catch(IllegalArgumentException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testFromValid_ValidIPv4Address_CreatesHostSpecifier() throws Throwable {
        HostSpecifier hostSpecifier = HostSpecifier.fromValid("127.0.0.1");
        assertEquals("HostSpecifier should preserve IPv4 address", "127.0.0.1", hostSpecifier.toString());
    }

    // ========== from() method tests ==========
    
    @Test(timeout = 4000)
    public void testFrom_NullInput_ThrowsNullPointerException() throws Throwable {
        try { 
            HostSpecifier.from(null);
            fail("Expected NullPointerException for null input");
        } catch(NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testFrom_IPv6Address_ThrowsAssertionError() throws Throwable {
        try { 
            HostSpecifier.from("::");
            fail("Expected AssertionError for IPv6 address");
        } catch(AssertionError e) {
            // IPv6 addresses are not supported in this implementation
        }
    }

    @Test(timeout = 4000)
    public void testFrom_InvalidHostSpecifier_ThrowsParseException() throws Throwable {
        try { 
            HostSpecifier.from("jR:");
            fail("Expected ParseException for invalid host specifier");
        } catch(ParseException e) {
            verifyException("com.google.common.net.HostSpecifier", e);
        }
    }

    @Test(timeout = 4000)
    public void testFrom_ValidIPv4Address_CreatesHostSpecifier() throws Throwable {
        HostSpecifier hostSpecifier = HostSpecifier.from("127.0.0.1");
        assertEquals("HostSpecifier should preserve IPv4 address", "127.0.0.1", hostSpecifier.toString());
    }

    // ========== equals() method tests ==========
    
    @Test(timeout = 4000)
    public void testEquals_SameHostSpecifiers_ReturnsTrue() throws Throwable {
        HostSpecifier hostSpecifier1 = HostSpecifier.from("127.0.0.1");
        HostSpecifier hostSpecifier2 = HostSpecifier.from("127.0.0.1");
        
        boolean result = hostSpecifier1.equals(hostSpecifier2);
        assertTrue("HostSpecifiers with same address should be equal", result);
    }

    @Test(timeout = 4000)
    public void testEquals_SameInstance_ReturnsTrue() throws Throwable {
        HostSpecifier hostSpecifier = HostSpecifier.from("127.0.0.1");
        
        boolean result = hostSpecifier.equals(hostSpecifier);
        assertTrue("HostSpecifier should equal itself", result);
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentObjectType_ReturnsFalse() throws Throwable {
        HostSpecifier hostSpecifier = HostSpecifier.from("127.0.0.1");
        Object otherObject = new Object();
        
        boolean result = hostSpecifier.equals(otherObject);
        assertFalse("HostSpecifier should not equal different object type", result);
    }

    // ========== hashCode() and toString() tests ==========
    
    @Test(timeout = 4000)
    public void testHashCode_ValidHostSpecifier_ReturnsHashCode() throws Throwable {
        HostSpecifier hostSpecifier = HostSpecifier.from("127.0.0.1");
        
        // Just verify that hashCode() doesn't throw an exception
        hostSpecifier.hashCode();
    }

    @Test(timeout = 4000)
    public void testToString_ValidHostSpecifier_ReturnsStringRepresentation() throws Throwable {
        HostSpecifier hostSpecifier = HostSpecifier.from("127.0.0.1");
        
        String result = hostSpecifier.toString();
        assertEquals("toString should return the host address", "127.0.0.1", result);
    }
}