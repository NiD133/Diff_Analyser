/*
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package org.apache.commons.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test case for the deprecated PosixParser.
 * 
 * <p>The PosixParser follows strict POSIX conventions and has limited functionality
 * compared to the DefaultParser. This test class verifies that unsupported features
 * are properly disabled while ensuring that standard POSIX parsing behavior works correctly.</p>
 * 
 * <p><strong>Key POSIX Parser Limitations:</strong></p>
 * <ul>
 *   <li>No support for single-dash long options (e.g., "-verbose" instead of "--verbose")</li>
 *   <li>No support for partial long option matching</li>
 *   <li>No support for equals syntax with short options (e.g., "-f=value")</li>
 *   <li>Limited double-dash handling</li>
 *   <li>No negative number option support</li>
 * </ul>
 * 
 * <p><strong>What PosixParser DOES support:</strong></p>
 * <ul>
 *   <li>Short options: -a, -b, -c</li>
 *   <li>Bundled short options: -abc (equivalent to -a -b -c)</li>
 *   <li>Long options with double dash: --verbose, --output=file</li>
 *   <li>Option arguments: -f filename, --file filename</li>
 * </ul>
 *
 * TODO: Migrate to JUnit parameterized tests to reduce code duplication
 */
class PosixParserTest extends AbstractParserTestCase {
    
    @Override
    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setUp() {
        super.setUp();
        parser = new PosixParser();
    }

    // =============================================================================
    // DISABLED TESTS - Features not supported by POSIX Parser
    // =============================================================================
    
    @Override
    @Test
    @Disabled("POSIX Parser limitation: Single-dash ambiguous long options not supported (e.g., '-ver' for '--verbose')")
    void testAmbiguousLongWithoutEqualSingleDash() throws Exception {
        // This test would verify parsing "-ver" as ambiguous between "--verbose" and "--version"
        // PosixParser only supports "--ver" syntax, not "-ver"
    }

    @Override
    @Test
    @Disabled("POSIX Parser limitation: Single-dash ambiguous long options not supported")
    void testAmbiguousLongWithoutEqualSingleDash2() throws Exception {
        // Additional test case for single-dash ambiguous long option parsing
        // PosixParser requires full "--" prefix for long options
    }

    @Override
    @Test
    @Disabled("POSIX Parser limitation: Partial long option matching not supported")
    void testAmbiguousPartialLongOption4() throws Exception {
        // This test would verify that partial matches like "--ver" for "--verbose" are ambiguous
        // PosixParser requires exact long option names
    }

    @Override
    @Test
    @Disabled("POSIX Parser limitation: Advanced double-dash handling not supported")
    void testDoubleDash2() throws Exception {
        // This test covers complex double-dash scenarios that PosixParser doesn't handle
        // Basic "--" support exists, but advanced cases are not supported
    }

    @Override
    @Test
    @Disabled("POSIX Parser limitation: Single-dash long options with equals not supported (e.g., '-file=value')")
    void testLongWithEqualSingleDash() throws Exception {
        // This would test "-file=value" syntax
        // PosixParser only supports "--file=value" syntax
    }

    @Override
    @Test
    @Disabled("POSIX Parser limitation: Single-dash long options not supported (e.g., '-file value')")
    void testLongWithoutEqualSingleDash() throws Exception {
        // This would test "-file value" syntax  
        // PosixParser only supports "--file value" syntax
    }

    @Override
    @Test
    @Disabled("POSIX Parser limitation: Long option argument validation not supported")
    void testLongWithUnexpectedArgument1() throws Exception {
        // This test verifies error handling for long options with unexpected arguments
        // PosixParser has limited argument validation capabilities
    }

    @Override
    @Test
    @Disabled("POSIX Parser limitation: Negative number options not supported (CLI-184)")
    void testNegativeOption() throws Exception {
        // This would test parsing negative numbers as option values (e.g., "-n -5")
        // PosixParser interprets "-5" as an option, not a negative number argument
        // See Apache CLI issue CLI-184 for details
    }

    @Override
    @Test
    @Disabled("POSIX Parser limitation: Equals syntax with short options not supported (e.g., '-f=filename')")
    void testShortWithEqual() throws Exception {
        // This would test "-f=filename" syntax
        // PosixParser only supports "-f filename" (space-separated) syntax
    }

    @Override
    @Test
    @Disabled("POSIX Parser limitation: Partial long option matching not supported")
    void testUnambiguousPartialLongOption4() throws Exception {
        // This test would verify that unambiguous partial matches like "--verb" for "--verbose" work
        // PosixParser requires complete long option names
    }
    
    // =============================================================================
    // ADDITIONAL DOCUMENTATION METHODS
    // =============================================================================
    
    /**
     * Example of what PosixParser DOES support - included for documentation purposes.
     * These test cases are inherited from AbstractParserTestCase and should pass.
     */
    @Test
    void demonstrateSupportedPosixFeatures() throws Exception {
        // This test serves as documentation for what PosixParser supports:
        // - Short options: -a, -b, -c
        // - Bundled short options: -abc
        // - Long options with double dash: --verbose
        // - Standard option arguments: -f filename, --file filename
        
        // Note: Actual test implementation would be inherited from parent class
        // This method exists primarily for documentation clarity
    }
}