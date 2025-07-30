package com.google.common.net;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.net.HostSpecifier;
import java.text.ParseException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class HostSpecifier_ESTest extends HostSpecifier_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testIsValid_withValidIPv4Address_returnsTrue() {
        boolean isValid = HostSpecifier.isValid("0.0.0.0");
        assertTrue(isValid);
    }

    @Test(timeout = 4000)
    public void testIsValid_withNull_throwsNullPointerException() {
        try {
            HostSpecifier.isValid(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsValid_withInvalidIPv6Address_throwsAssertionError() {
        try {
            HostSpecifier.isValid("::");
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            // Expected exception for invalid IPv6 address
        }
    }

    @Test(timeout = 4000)
    public void testFromValid_withNull_throwsNullPointerException() {
        try {
            HostSpecifier.fromValid(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testFromValid_withInvalidIPv6Address_throwsAssertionError() {
        try {
            HostSpecifier.fromValid("::");
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            // Expected exception for invalid IPv6 address
        }
    }

    @Test(timeout = 4000)
    public void testFrom_withNull_throwsNullPointerException() {
        try {
            HostSpecifier.from(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testFrom_withInvalidIPv6Address_throwsAssertionError() {
        try {
            HostSpecifier.from("::");
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            // Expected exception for invalid IPv6 address
        }
    }

    @Test(timeout = 4000)
    public void testFromValid_withInvalidDomainName_throwsIllegalArgumentException() {
        try {
            HostSpecifier.fromValid("com.google.common.net.HostSpecifier");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("com.google.common.net.HostSpecifier", e);
        }
    }

    @Test(timeout = 4000)
    public void testFromValid_withInvalidPort_throwsIllegalArgumentException() {
        try {
            HostSpecifier.fromValid(":7");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testFromValid_withValidIPv4Address_returnsHostSpecifier() {
        HostSpecifier hostSpecifier = HostSpecifier.fromValid("127.0.0.1");
        assertEquals("127.0.0.1", hostSpecifier.toString());
    }

    @Test(timeout = 4000)
    public void testEquals_withSameHostSpecifier_returnsTrue() {
        HostSpecifier hostSpecifier1 = HostSpecifier.from("127.0.0.1");
        HostSpecifier hostSpecifier2 = HostSpecifier.from("127.0.0.1");
        assertTrue(hostSpecifier1.equals(hostSpecifier2));
    }

    @Test(timeout = 4000)
    public void testEquals_withSameObject_returnsTrue() {
        HostSpecifier hostSpecifier = HostSpecifier.from("127.0.0.1");
        assertTrue(hostSpecifier.equals(hostSpecifier));
    }

    @Test(timeout = 4000)
    public void testEquals_withDifferentObject_returnsFalse() {
        HostSpecifier hostSpecifier = HostSpecifier.from("127.0.0.1");
        Object otherObject = new Object();
        assertFalse(hostSpecifier.equals(otherObject));
    }

    @Test(timeout = 4000)
    public void testFrom_withInvalidHostSpecifier_throwsParseException() {
        try {
            HostSpecifier.from("jR:");
            fail("Expecting exception: ParseException");
        } catch (ParseException e) {
            verifyException("com.google.common.net.HostSpecifier", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsValid_withInvalidCharacters_returnsFalse() {
        boolean isValid = HostSpecifier.isValid("E$U|;qI:3");
        assertFalse(isValid);
    }

    @Test(timeout = 4000)
    public void testHashCode_withValidHostSpecifier_doesNotThrow() {
        HostSpecifier hostSpecifier = HostSpecifier.from("127.0.0.1");
        hostSpecifier.hashCode();
    }

    @Test(timeout = 4000)
    public void testToString_withValidHostSpecifier_returnsCorrectString() {
        HostSpecifier hostSpecifier = HostSpecifier.from("127.0.0.1");
        assertEquals("127.0.0.1", hostSpecifier.toString());
    }
}