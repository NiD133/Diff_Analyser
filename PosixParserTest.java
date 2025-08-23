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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the deprecated PosixParser using the common scenarios defined in {@link AbstractParserTestCase}.
 *
 * This class intentionally overrides and disables a subset of tests that cannot pass with PosixParser because:
 * - PosixParser does not recognize GNU-style long options introduced with a single dash (e.g. -version).
 * - It does not support the = form for option arguments (e.g. --output=file or -ofile treated as value).
 * - Certain edge cases like ambiguous partial long options and negative-number options are not supported.
 *
 * The remaining tests defined in the base class still run against PosixParser to verify behavior it does support.
 *
 * Note: PosixParser is deprecated since 1.3. Prefer DefaultParser for modern CLI parsing features.
 */
@SuppressWarnings("deprecation") // The subject under test (PosixParser) is deprecated by design for this suite.
class PosixParserTest extends AbstractParserTestCase {

    // Reusable reasons for @Disabled to avoid repetition and keep messages consistent.
    private static final String R_NOT_SUPPORTED = "not supported by the PosixParser";
    private static final String R_NOT_SUPPORTED_CLI_184 = "not supported by the PosixParser (CLI-184)";

    /**
     * Creates a fresh PosixParser for each test. The base class wires the "parser" field used by all tests.
     */
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        parser = new PosixParser();
    }

    /**
     * PosixParser cannot disambiguate long options introduced with a single dash without '='.
     */
    @Override
    @Test
    @DisplayName("Single-dash ambiguous long option without '=' is not supported")
    @Disabled(R_NOT_SUPPORTED)
    void testAmbiguousLongWithoutEqualSingleDash() throws Exception {
        // Intentionally disabled for PosixParser (see class-level Javadoc).
    }

    /**
     * Same as above but for a second ambiguity scenario.
     */
    @Override
    @Test
    @DisplayName("Single-dash ambiguous long option without '=' (variant 2) is not supported")
    @Disabled(R_NOT_SUPPORTED)
    void testAmbiguousLongWithoutEqualSingleDash2() throws Exception {
        // Intentionally disabled.
    }

    /**
     * Ambiguous partial long option resolution is not supported by PosixParser.
     */
    @Override
    @Test
    @DisplayName("Ambiguous partial long option (case 4) is not supported")
    @Disabled(R_NOT_SUPPORTED)
    void testAmbiguousPartialLongOption4() throws Exception {
        // Intentionally disabled.
    }

    /**
     * Certain double-dash handling scenarios differ from PosixParser behavior.
     */
    @Override
    @Test
    @DisplayName("Double dash handling (variant 2) is not supported")
    @Disabled(R_NOT_SUPPORTED)
    void testDoubleDash2() throws Exception {
        // Intentionally disabled.
    }

    /**
     * Long-options with '=' using a single dash are not recognized by PosixParser.
     */
    @Override
    @Test
    @DisplayName("Single-dash long option with '=' is not supported")
    @Disabled(R_NOT_SUPPORTED)
    void testLongWithEqualSingleDash() throws Exception {
        // Intentionally disabled.
    }

    /**
     * Long-options without '=' using a single dash are not recognized by PosixParser.
     */
    @Override
    @Test
    @DisplayName("Single-dash long option without '=' is not supported")
    @Disabled(R_NOT_SUPPORTED)
    void testLongWithoutEqualSingleDash() throws Exception {
        // Intentionally disabled.
    }

    /**
     * Supplying an unexpected argument to a long option is not handled the same by PosixParser.
     */
    @Override
    @Test
    @DisplayName("Long option with unexpected argument (case 1) is not supported")
    @Disabled(R_NOT_SUPPORTED)
    void testLongWithUnexpectedArgument1() throws Exception {
        // Intentionally disabled.
    }

    /**
     * Treating negative numbers as options (CLI-184) is not supported by PosixParser.
     */
    @Override
    @Test
    @DisplayName("Negative-number option handling (CLI-184) is not supported")
    @Disabled(R_NOT_SUPPORTED_CLI_184)
    void testNegativeOption() throws Exception {
        // Intentionally disabled.
    }

    /**
     * Short options using the '=' syntax are not supported by PosixParser.
     */
    @Override
    @Test
    @DisplayName("Short option with '=' is not supported")
    @Disabled(R_NOT_SUPPORTED)
    void testShortWithEqual() throws Exception {
        // Intentionally disabled.
    }

    /**
     * Unambiguous partial long option resolution is not supported by PosixParser.
     */
    @Override
    @Test
    @DisplayName("Unambiguous partial long option (case 4) is not supported")
    @Disabled(R_NOT_SUPPORTED)
    void testUnambiguousPartialLongOption4() throws Exception {
        // Intentionally disabled.
    }
}