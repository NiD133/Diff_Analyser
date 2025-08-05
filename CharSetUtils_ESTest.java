package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.CharSetUtils;
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
public class CharSetUtilsTest extends CharSetUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testSqueezeWithoutMatchingCharacters() {
        String[] charSet = {"0-9"};
        String result = CharSetUtils.squeeze("offset cannot be negative", charSet);
        assertEquals("offset cannot be negative", result);
    }

    @Test(timeout = 4000)
    public void testSqueezeWithMatchingCharacters() {
        String[] charSet = {"uk{Z^6e/S>lTbb#wl"};
        String result = CharSetUtils.squeeze(
            "Aborting to protect against StackOverflowError - output of one loop is the input of another",
            charSet
        );
        assertEquals("Aborting to protect against StackOverflowEror - output of one lop is the input of another", result);
    }

    @Test(timeout = 4000)
    public void testSqueezeEmptyString() {
        String[] charSet = new String[4];
        String result = CharSetUtils.squeeze("", charSet);
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testDeleteWithNullString() {
        String[] charSet = new String[6];
        String result = CharSetUtils.delete(null, charSet);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testSqueezeWithNonMatchingCharacters() {
        String[] charSet = {",']t[O'q)5c-["};
        String result = CharSetUtils.squeeze("...", charSet);
        assertEquals("...", result);
    }

    @Test(timeout = 4000)
    public void testSqueezeWithPartialMatchingCharacters() {
        String[] charSet = {"uk{Z^6e/S>lTbb#wl"};
        String result = CharSetUtils.squeeze("...", charSet);
        assertEquals(".", result);
    }

    @Test(timeout = 4000)
    public void testSqueezeWithMultipleMatches() {
        String[] charSet = {"Jk{Z^]6e/!S>zlTbb#wl"};
        String result = CharSetUtils.squeeze("offset cannot be negative", charSet);
        assertEquals("ofset canot be negative", result);
    }

    @Test(timeout = 4000)
    public void testSqueezeWithNoChangesNeeded() {
        String[] charSet = {"h|Bv_9mUP7'&Y"};
        String result = CharSetUtils.squeeze(
            "Aborting to protect against StackOverflowError - output of one loop is the input of another",
            charSet
        );
        assertEquals("Aborting to protect against StackOverflowError - output of one loop is the input of another", result);
    }

    @Test(timeout = 4000)
    public void testSqueezeWithEmptyCharSet() {
        String[] charSet = new String[2];
        String result = CharSetUtils.squeeze(
            "Aborting to protect against StackOverflowError - output of one loop is the input of another",
            charSet
        );
        assertEquals("Aborting to protect against StackOverflowError - output of one loop is the input of another", result);
    }

    @Test(timeout = 4000)
    public void testSqueezeNullString() {
        String[] charSet = new String[8];
        String result = CharSetUtils.squeeze(null, charSet);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testKeepWithMatchingCharacters() {
        String[] charSet = {"A-Z"};
        String result = CharSetUtils.keep("A-Z", charSet);
        assertEquals("AZ", result);
    }

    @Test(timeout = 4000)
    public void testKeepEmptyString() {
        String[] charSet = new String[4];
        String result = CharSetUtils.keep("", charSet);
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testKeepWithNullString() {
        String[] charSet = new String[5];
        String result = CharSetUtils.keep(null, charSet);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testKeepWithNoMatchingCharacters() {
        String[] charSet = new String[8];
        String result = CharSetUtils.keep("A-Z", charSet);
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testDeleteWithMatchingCharacters() {
        String[] charSet = {"A-Z"};
        String result = CharSetUtils.delete("A-Z", charSet);
        assertEquals("-", result);
    }

    @Test(timeout = 4000)
    public void testDeleteWithNoMatchingCharacters() {
        String[] charSet = new String[8];
        String result = CharSetUtils.delete("A-Z", charSet);
        assertEquals("A-Z", result);
    }

    @Test(timeout = 4000)
    public void testDeleteEmptyString() {
        String[] charSet = new String[0];
        String result = CharSetUtils.delete("", charSet);
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testCountWithMatchingCharacters() {
        String[] charSet = {"h|Bv_9mUP7'&Y"};
        int count = CharSetUtils.count("^MPkb@$Zu", charSet);
        assertEquals(1, count);
    }

    @Test(timeout = 4000)
    public void testCountEmptyString() {
        String[] charSet = new String[13];
        int count = CharSetUtils.count("", charSet);
        assertEquals(0, count);
    }

    @Test(timeout = 4000)
    public void testContainsAnyWithNoMatchingCharacters() {
        String[] charSet = {"Xii8KJ"};
        boolean contains = CharSetUtils.containsAny("A-Za-z", charSet);
        assertFalse(contains);
    }

    @Test(timeout = 4000)
    public void testContainsAnyWithMatchingCharacters() {
        String[] charSet = {"h|Bv_9mUP7'&Y"};
        boolean contains = CharSetUtils.containsAny("h|Bv_9mUP7'&Y", charSet);
        assertTrue(contains);
    }

    @Test(timeout = 4000)
    public void testContainsAnyWithEmptyCharSet() {
        String[] charSet = new String[2];
        boolean contains = CharSetUtils.containsAny("h|Bv_9mUP7'&Y", charSet);
        assertFalse(contains);
    }

    @Test(timeout = 4000)
    public void testContainsAnyEmptyString() {
        String[] charSet = new String[7];
        boolean contains = CharSetUtils.containsAny("", charSet);
        assertFalse(contains);
    }

    @Test(timeout = 4000)
    public void testCountWithNoMatchingCharacters() {
        String[] charSet = new String[2];
        int count = CharSetUtils.count("&", charSet);
        assertEquals(0, count);
    }

    @Test(timeout = 4000)
    public void testCharSetUtilsConstructor() {
        CharSetUtils charSetUtils = new CharSetUtils();
        assertNotNull(charSetUtils);
    }
}