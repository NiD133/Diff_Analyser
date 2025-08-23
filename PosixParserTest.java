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
 * Test suite for the PosixParser class.
 *
 * Note: These tests are currently disabled as the PosixParser does not support these features.
 *       Consider using DefaultParser for full support.
 *
 * TODO: Refactor to use JUnit parameterized tests for better coverage and maintainability.
 */
class PosixParserTest extends AbstractParserTestCase {

    /**
     * Sets up the test environment before each test.
     * Initializes the parser to be an instance of PosixParser.
     */
    @Override
    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setUp() {
        super.setUp();
        parser = new PosixParser();
    }

    /**
     * Test case for ambiguous long options without an equal sign using a single dash.
     * Currently disabled as this feature is not supported by PosixParser.
     */
    @Override
    @Test
    @Disabled("Not supported by the PosixParser")
    void testAmbiguousLongWithoutEqualSingleDash() throws Exception {
        // Test logic here (if supported)
    }

    /**
     * Test case for another scenario of ambiguous long options without an equal sign using a single dash.
     * Currently disabled as this feature is not supported by PosixParser.
     */
    @Override
    @Test
    @Disabled("Not supported by the PosixParser")
    void testAmbiguousLongWithoutEqualSingleDash2() throws Exception {
        // Test logic here (if supported)
    }

    /**
     * Test case for ambiguous partial long options.
     * Currently disabled as this feature is not supported by PosixParser.
     */
    @Override
    @Test
    @Disabled("Not supported by the PosixParser")
    void testAmbiguousPartialLongOption4() throws Exception {
        // Test logic here (if supported)
    }

    /**
     * Test case for double dash options.
     * Currently disabled as this feature is not supported by PosixParser.
     */
    @Override
    @Test
    @Disabled("Not supported by the PosixParser")
    void testDoubleDash2() throws Exception {
        // Test logic here (if supported)
    }

    /**
     * Test case for long options with an equal sign using a single dash.
     * Currently disabled as this feature is not supported by PosixParser.
     */
    @Override
    @Test
    @Disabled("Not supported by the PosixParser")
    void testLongWithEqualSingleDash() throws Exception {
        // Test logic here (if supported)
    }

    /**
     * Test case for long options without an equal sign using a single dash.
     * Currently disabled as this feature is not supported by PosixParser.
     */
    @Override
    @Test
    @Disabled("Not supported by the PosixParser")
    void testLongWithoutEqualSingleDash() throws Exception {
        // Test logic here (if supported)
    }

    /**
     * Test case for long options with unexpected arguments.
     * Currently disabled as this feature is not supported by PosixParser.
     */
    @Override
    @Test
    @Disabled("Not supported by the PosixParser")
    void testLongWithUnexpectedArgument1() throws Exception {
        // Test logic here (if supported)
    }

    /**
     * Test case for negative options.
     * Currently disabled as this feature is not supported by PosixParser.
     */
    @Override
    @Test
    @Disabled("Not supported by the PosixParser (CLI-184)")
    void testNegativeOption() throws Exception {
        // Test logic here (if supported)
    }

    /**
     * Test case for short options with an equal sign.
     * Currently disabled as this feature is not supported by PosixParser.
     */
    @Override
    @Test
    @Disabled("Not supported by the PosixParser")
    void testShortWithEqual() throws Exception {
        // Test logic here (if supported)
    }

    /**
     * Test case for unambiguous partial long options.
     * Currently disabled as this feature is not supported by PosixParser.
     */
    @Override
    @Test
    @Disabled("Not supported by the PosixParser")
    void testUnambiguousPartialLongOption4() throws Exception {
        // Test logic here (if supported)
    }
}