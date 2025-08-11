package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.io.IOCase;

/**
 * Test suite for IOCase enum functionality.
 * Tests case-sensitive and case-insensitive string operations for file system operations.
 */
public class IOCaseTest {

    // ========== Enum Constants and Factory Methods ==========
    
    @Test
    public void testEnumValues_ShouldReturnThreeConstants() {
        IOCase[] values = IOCase.values();
        
        assertEquals("Should have exactly 3 IOCase constants", 3, values.length);
    }
    
    @Test
    public void testValueOf_WithSensitive_ShouldReturnSensitiveCase() {
        IOCase result = IOCase.valueOf("SENSITIVE");
        
        assertEquals(IOCase.SENSITIVE, result);
    }
    
    @Test
    public void testValueOf_WithInsensitive_ShouldReturnInsensitiveCase() {
        IOCase result = IOCase.valueOf("INSENSITIVE");
        
        assertEquals(IOCase.INSENSITIVE, result);
        assertEquals("Insensitive", result.getName());
    }
    
    @Test
    public void testForName_WithValidName_ShouldReturnCorrectCase() {
        IOCase sensitive = IOCase.forName("Sensitive");
        IOCase insensitive = IOCase.forName("Insensitive");
        
        assertEquals(IOCase.SENSITIVE, sensitive);
        assertEquals(IOCase.INSENSITIVE, insensitive);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testForName_WithInvalidName_ShouldThrowException() {
        IOCase.forName("InvalidCaseName");
    }

    // ========== Case Sensitivity Properties ==========
    
    @Test
    public void testIsCaseSensitive_ForSensitiveCase_ShouldReturnTrue() {
        assertTrue("SENSITIVE should be case sensitive", IOCase.SENSITIVE.isCaseSensitive());
    }
    
    @Test
    public void testIsCaseSensitive_ForInsensitiveCase_ShouldReturnFalse() {
        assertFalse("INSENSITIVE should not be case sensitive", IOCase.INSENSITIVE.isCaseSensitive());
    }
    
    @Test
    public void testStaticIsCaseSensitive_WithNullInput_ShouldReturnFalse() {
        assertFalse("Null IOCase should default to case insensitive", IOCase.isCaseSensitive(null));
    }
    
    @Test
    public void testStaticIsCaseSensitive_WithInsensitiveCase_ShouldReturnFalse() {
        assertFalse("INSENSITIVE case should return false", IOCase.isCaseSensitive(IOCase.INSENSITIVE));
    }

    // ========== Value Method (Null Safety) ==========
    
    @Test
    public void testValue_WithNullValue_ShouldReturnDefault() {
        IOCase result = IOCase.value(null, IOCase.INSENSITIVE);
        
        assertEquals("Should return default when value is null", IOCase.INSENSITIVE, result);
    }
    
    @Test
    public void testValue_WithNonNullValue_ShouldReturnValue() {
        IOCase result = IOCase.value(IOCase.SENSITIVE, IOCase.INSENSITIVE);
        
        assertEquals("Should return first parameter when not null", IOCase.SENSITIVE, result);
    }

    // ========== String Equality Checks ==========
    
    @Test
    public void testCheckEquals_CaseSensitive_WithIdenticalStrings_ShouldReturnTrue() {
        assertTrue("Identical strings should be equal", 
                  IOCase.SENSITIVE.checkEquals("test", "test"));
    }
    
    @Test
    public void testCheckEquals_CaseSensitive_WithDifferentCase_ShouldReturnFalse() {
        assertFalse("Different case should not be equal in sensitive mode", 
                   IOCase.SENSITIVE.checkEquals("Test", "test"));
    }
    
    @Test
    public void testCheckEquals_CaseInsensitive_WithDifferentCase_ShouldReturnTrue() {
        assertTrue("Different case should be equal in insensitive mode", 
                  IOCase.INSENSITIVE.checkEquals("System", "SYSTEM"));
    }
    
    @Test
    public void testCheckEquals_WithNullInput_ShouldReturnFalse() {
        assertFalse("Null input should return false", 
                   IOCase.SENSITIVE.checkEquals(null, "test"));
    }
    
    @Test
    public void testCheckEquals_WithEmptyStrings_ShouldReturnTrue() {
        assertTrue("Empty strings should be equal", 
                  IOCase.SYSTEM.checkEquals("", ""));
    }

    // ========== String Comparison ==========
    
    @Test
    public void testCheckCompareTo_WithIdenticalStrings_ShouldReturnZero() {
        int result = IOCase.INSENSITIVE.checkCompareTo("test", "test");
        
        assertEquals("Identical strings should compare to 0", 0, result);
    }
    
    @Test
    public void testCheckCompareTo_WithDifferentStrings_ShouldReturnNonZero() {
        int result = IOCase.INSENSITIVE.checkCompareTo("LINUX", "\"#S6?U_R7?'mwf");
        
        assertEquals("Different strings should have specific comparison result", 74, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testCheckCompareTo_WithNullInput_ShouldThrowException() {
        IOCase.SENSITIVE.checkCompareTo(null, null);
    }

    // ========== String Prefix/Suffix Checks ==========
    
    @Test
    public void testCheckStartsWith_WithMatchingPrefix_ShouldReturnTrue() {
        assertTrue("String should start with matching prefix", 
                  IOCase.INSENSITIVE.checkStartsWith("\"#s6?", "\"#s6?"));
    }
    
    @Test
    public void testCheckStartsWith_WithNonMatchingPrefix_ShouldReturnFalse() {
        assertFalse("String should not start with non-matching prefix", 
                   IOCase.SYSTEM.checkStartsWith("vHnm-dNXF4", "2"));
    }
    
    @Test
    public void testCheckStartsWith_WithNullInput_ShouldReturnFalse() {
        assertFalse("Null input should return false", 
                   IOCase.INSENSITIVE.checkStartsWith("I", null));
        assertFalse("Both null should return false", 
                   IOCase.SYSTEM.checkStartsWith(null, null));
    }
    
    @Test
    public void testCheckEndsWith_WithMatchingSuffix_ShouldReturnTrue() {
        assertTrue("String should end with matching suffix", 
                  IOCase.SENSITIVE.checkEndsWith("|,?])9N.", "|,?])9N."));
    }
    
    @Test
    public void testCheckEndsWith_WithNonMatchingSuffix_ShouldReturnFalse() {
        assertFalse("String should not end with non-matching suffix", 
                   IOCase.INSENSITIVE.checkEndsWith("Sensitive", "gX@Wm5dEh2O"));
    }
    
    @Test
    public void testCheckEndsWith_WithNullInput_ShouldReturnFalse() {
        assertFalse("Null input should return false", 
                   IOCase.INSENSITIVE.checkEndsWith("test", null));
        assertFalse("Both null should return false", 
                   IOCase.SYSTEM.checkEndsWith(null, null));
    }

    // ========== String Search Operations ==========
    
    @Test
    public void testCheckIndexOf_WithFoundSubstring_ShouldReturnCorrectIndex() {
        int result = IOCase.INSENSITIVE.checkIndexOf("1", 0, "1");
        
        assertEquals("Should find substring at correct index", 0, result);
    }
    
    @Test
    public void testCheckIndexOf_WithNotFoundSubstring_ShouldReturnMinusOne() {
        int result = IOCase.SYSTEM.checkIndexOf("5La,\"KK)#Ep.w:5veX", 0, "1");
        
        assertEquals("Should return -1 when substring not found", -1, result);
    }
    
    @Test
    public void testCheckIndexOf_CaseInsensitive_WithDifferentCase_ShouldFindMatch() {
        int result = IOCase.INSENSITIVE.checkIndexOf("XH{o(jPCKq3L?>", -31, "p");
        
        assertEquals("Should find case-insensitive match", 6, result);
    }
    
    @Test
    public void testCheckIndexOf_WithNullInput_ShouldReturnMinusOne() {
        int result = IOCase.SENSITIVE.checkIndexOf("test", -231, null);
        
        assertEquals("Null search string should return -1", -1, result);
    }
    
    @Test
    public void testCheckIndexOf_WithInvalidStartIndex_ShouldHandleGracefully() {
        int result = IOCase.SYSTEM.checkIndexOf("test", 2351, "");
        
        assertEquals("Invalid start index should return -1", -1, result);
    }

    // ========== Region Matching ==========
    
    @Test
    public void testCheckRegionMatches_WithMatchingRegion_ShouldReturnTrue() {
        assertTrue("Matching regions should return true", 
                  IOCase.SENSITIVE.checkRegionMatches("NUL", 0, "NUL"));
    }
    
    @Test
    public void testCheckRegionMatches_WithNonMatchingRegion_ShouldReturnFalse() {
        assertFalse("Non-matching regions should return false", 
                   IOCase.SENSITIVE.checkRegionMatches("Sensitive", 258, "Insensitive"));
    }
    
    @Test
    public void testCheckRegionMatches_WithNegativeIndex_ShouldReturnFalse() {
        assertFalse("Negative index should return false", 
                   IOCase.INSENSITIVE.checkRegionMatches("hM'", -537, "hM'"));
    }
    
    @Test
    public void testCheckRegionMatches_WithNullInput_ShouldReturnFalse() {
        assertFalse("Null input should return false", 
                   IOCase.SYSTEM.checkRegionMatches("System", -1121, null));
        assertFalse("Both null should return false", 
                   IOCase.SYSTEM.checkRegionMatches(null, 493, null));
    }

    // ========== String Representation ==========
    
    @Test
    public void testToString_ShouldReturnName() {
        assertEquals("toString should return the name", "Insensitive", IOCase.INSENSITIVE.toString());
    }
    
    @Test
    public void testGetName_ShouldReturnCorrectName() {
        assertEquals("getName should return correct name", "Insensitive", IOCase.INSENSITIVE.getName());
    }

    // ========== System-Dependent Behavior ==========
    
    @Test
    public void testSystemCase_ShouldHaveSystemDependentBehavior() {
        // This test verifies SYSTEM case works but doesn't assert specific behavior
        // since it depends on the operating system
        IOCase systemCase = IOCase.SYSTEM;
        IOCase result = IOCase.value(systemCase, systemCase);
        
        assertEquals("SYSTEM case should work with value method", IOCase.SYSTEM, result);
        
        // Test that SYSTEM case can perform comparisons
        int comparison = systemCase.checkCompareTo("test", "test");
        assertEquals("SYSTEM case should handle string comparison", 0, comparison);
    }
}