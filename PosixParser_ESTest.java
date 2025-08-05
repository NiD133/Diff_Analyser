package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

/**
 * Test suite for PosixParser functionality.
 * Tests the flatten() and burstToken() methods which are core to POSIX-style command line parsing.
 */
public class PosixParserTest {

    private PosixParser parser = new PosixParser();
    
    // ========== burstToken() Tests ==========
    
    @Test
    public void testBurstToken_WithEmptyString_ShouldNotThrow() {
        // Given: An empty token
        String emptyToken = "";
        
        // When & Then: Should handle empty string gracefully
        parser.burstToken(emptyToken, true);
    }
    
    @Test(expected = NullPointerException.class)
    public void testBurstToken_WithoutPriorFlattenCall_ShouldThrowNullPointerException() {
        // Given: A token without initializing parser state via flatten()
        String token = ";-";
        
        // When: Calling burstToken without proper initialization
        // Then: Should throw NullPointerException
        parser.burstToken(token, true);
    }
    
    @Test
    public void testBurstToken_AfterFlattenCall_ShouldWork() {
        // Given: Parser initialized with flatten call
        Options options = new Options();
        String[] args = new String[9]; // Array with nulls
        parser.flatten(options, args, false);
        
        // When: Calling burstToken after initialization
        // Then: Should not throw exception
        parser.burstToken(";-", true);
    }
    
    // ========== flatten() Tests - Null Handling ==========
    
    @Test(expected = NullPointerException.class)
    public void testFlatten_WithNullOptions_ShouldThrowNullPointerException() {
        // When: Calling flatten with null options
        // Then: Should throw NullPointerException
        parser.flatten(null, null, true);
    }
    
    // ========== flatten() Tests - Basic Functionality ==========
    
    @Test
    public void testFlatten_WithSimpleShortOption_ShouldProcessCorrectly() {
        // Given: Options with a required option and args containing that option
        Options options = new Options();
        options.addRequiredOption("j", "j", true, "j");
        String[] args = {null, "-j", null, null, null};
        
        // When: Flattening the arguments
        String[] result = parser.flatten(options, args, true);
        
        // Then: Should return flattened array with the option
        assertEquals("Should contain only the valid option", 1, result.length);
    }
    
    @Test
    public void testFlatten_WithUnknownShortOption_ShouldIncludeInResult() {
        // Given: Empty options and args with unknown option
        Options options = new Options();
        String[] args = {null, "-j", null, null, null};
        
        // When: Flattening with stopAtNonOption=true
        String[] result = parser.flatten(options, args, true);
        
        // Then: Should include unknown options in result
        assertEquals("Should include unknown option and remaining args", 4, result.length);
    }
    
    @Test
    public void testFlatten_WithSingleDash_ShouldIncludeInResult() {
        // Given: Args containing single dash (stdin indicator)
        Options options = new Options();
        String[] args = {null, null, null, null, "-"};
        
        // When: Flattening the arguments
        String[] result = parser.flatten(options, args, true);
        
        // Then: Should include the single dash in result
        assertEquals("Should include single dash", 1, result.length);
    }
    
    // ========== flatten() Tests - Long Options ==========
    
    @Test
    public void testFlatten_WithLongOptionWithValue_ShouldProcessCorrectly() {
        // Given: Options and args with long option containing equals sign
        Options options = new Options();
        options.addRequiredOption("j", "j", false, "j");
        String[] args = new String[11];
        args[5] = "--=<iy"; // Long option with value
        
        // When: Flattening twice (as in original test)
        String[] firstResult = parser.flatten(options, args, false);
        String[] secondResult = parser.flatten(options, firstResult, true);
        
        // Then: Should process long option correctly
        assertEquals("Should process long option with value", 3, secondResult.length);
    }
    
    @Test
    public void testFlatten_WithUnknownLongOption_ShouldIncludeInResult() {
        // Given: Args with unknown long option
        Options options = new Options();
        String[] args = new String[37];
        args[19] = "--K";
        
        // When: Flattening the arguments
        String[] result = parser.flatten(options, args, false);
        
        // Then: Should include unknown long option
        assertEquals("Should include unknown long option", 1, result.length);
    }
    
    @Test
    public void testFlatten_WithLongOptionWithEqualsAndValue_ShouldProcessCorrectly() {
        // Given: Args with long option containing equals and value
        Options options = new Options();
        String[] args = new String[9];
        args[5] = "--=<q;n";
        
        // When: Flattening the arguments
        String[] result = parser.flatten(options, args, false);
        
        // Then: Should process the option correctly
        assertEquals("Should process long option with equals and value", 1, result.length);
    }
    
    // ========== flatten() Tests - Ambiguous Options ==========
    
    @Test
    public void testFlatten_WithAmbiguousLongOption_ShouldThrowException() {
        // Given: Options with ambiguous long option prefixes
        Options options = new Options();
        options.addRequiredOption("j", "j", false, "j");
        options.addOption("j", "--WS", false, "--WS");
        String[] args = new String[12];
        args[10] = "--=<ibn"; // Ambiguous prefix
        
        // When & Then: Should throw exception for ambiguous option
        try {
            parser.flatten(options, args, false);
            fail("Should throw exception for ambiguous option");
        } catch (Exception e) {
            assertTrue("Should mention ambiguous option", 
                      e.getMessage().contains("Ambiguous option"));
        }
    }
    
    // ========== flatten() Tests - Complex Scenarios ==========
    
    @Test
    public void testFlatten_WithLargeArgumentArray_ShouldProcessCorrectly() {
        // Given: Large array of country codes (realistic data)
        Options options = new Options();
        String[] countryCodes = {"US", "CA", "GB", "FR", "DE"}; // Simplified for test
        
        // When: Flattening multiple times
        String[] firstResult = parser.flatten(options, countryCodes, true);
        String[] secondResult = parser.flatten(options, firstResult, true);
        
        // Then: Should process all entries
        assertEquals("Should process all country codes", countryCodes.length, secondResult.length);
    }
    
    @Test
    public void testFlatten_WithComplexShortOption_ShouldProcessCorrectly() {
        // Given: Args with complex short option pattern
        Options options = new Options();
        String[] args = new String[6];
        args[3] = "-/C"; // Complex short option
        
        // When: Flattening the arguments
        String[] result = parser.flatten(options, args, false);
        
        // Then: Should process the complex option
        assertEquals("Should process complex short option", 1, result.length);
    }
    
    // ========== Integration Tests ==========
    
    @Test
    public void testFlatten_WithLongOptionAndBurstToken_ShouldWorkTogether() {
        // Given: Options with long option and subsequent burstToken call
        Options options = new Options();
        options.addRequiredOption("j", "org.apache.commons.cli.PosixParser", true, "j");
        String[] args = new String[9];
        args[2] = "-org.apache.commons.cli.PosixParser";
        
        // When: Flattening and then bursting token
        parser.flatten(options, args, false);
        
        // Then: burstToken should work after flatten
        parser.burstToken(";-", true); // Should not throw exception
    }
    
    @Test
    public void testFlatten_MultipleCallsWithDifferentTokens_ShouldHandleCorrectly() {
        // Given: Options and multiple different token types
        Options options = new Options();
        options.addRequiredOption("j", "", true, "j");
        String[] countryCodes = {"US", "CA", "GB"}; // Simplified
        
        // When: Multiple flatten calls with different parameters
        parser.flatten(options, countryCodes, true);
        parser.burstToken("--", true);
        parser.burstToken("$--", true);
        
        // Then: Should handle all calls without exception
        // Test passes if no exceptions are thrown
    }
}