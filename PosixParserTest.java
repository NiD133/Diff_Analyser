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

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test case for the PosixParser.
 *
 * <p>Note: These tests intentionally disable unsupported functionality
 * specific to PosixParser's limitations.</p>
 *
 * <p>TODO: Consider reworking using JUnit parameterized tests in the future.</p>
 */
class PosixParserTest extends AbstractParserTestCase {

    @Override
    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setUp() {
        super.setUp();
        parser = new PosixParser();
    }

    //--------------------------------------------------------------------------
    // Overridden tests that are explicitly disabled for PosixParser
    //--------------------------------------------------------------------------
    
    /**
     * PosixParser doesn't support ambiguous long options without equals sign and single dash.
     */
    @Override
    @Test
    @Disabled("PosixParser doesn't support ambiguous long options without equals sign")
    void testAmbiguousLongWithoutEqualSingleDash() {
        fail("PosixParser doesn't support ambiguous long options without equals sign");
    }

    /**
     * PosixParser doesn't support ambiguous long options without equals sign (variant 2).
     */
    @Override
    @Test
    @Disabled("PosixParser doesn't support ambiguous long options without equals sign")
    void testAmbiguousLongWithoutEqualSingleDash2() {
        fail("PosixParser doesn't support ambiguous long options without equals sign");
    }

    /**
     * PosixParser doesn't support ambiguous partial long options (case 4).
     */
    @Override
    @Test
    @Disabled("PosixParser doesn't support ambiguous partial long options")
    void testAmbiguousPartialLongOption4() {
        fail("PosixParser doesn't support ambiguous partial long options");
    }

    /**
     * PosixParser doesn't support double dash handling (case 2).
     */
    @Override
    @Test
    @Disabled("PosixParser doesn't support double dash in this context")
    void testDoubleDash2() {
        fail("PosixParser doesn't support double dash in this context");
    }

    /**
     * PosixParser doesn't support long options with equals sign and single dash.
     */
    @Override
    @Test
    @Disabled("PosixParser doesn't support long options with equals sign and single dash")
    void testLongWithEqualSingleDash() {
        fail("PosixParser doesn't support long options with equals sign and single dash");
    }

    /**
     * PosixParser doesn't support long options without equals sign and single dash.
     */
    @Override
    @Test
    @Disabled("PosixParser doesn't support long options without equals sign")
    void testLongWithoutEqualSingleDash() {
        fail("PosixParser doesn't support long options without equals sign");
    }

    /**
     * PosixParser doesn't support long options with unexpected arguments.
     */
    @Override
    @Test
    @Disabled("PosixParser doesn't support long options with unexpected arguments")
    void testLongWithUnexpectedArgument1() {
        fail("PosixParser doesn't support long options with unexpected arguments");
    }

    /**
     * PosixParser doesn't support negative options (CLI-184).
     */
    @Override
    @Test
    @Disabled("PosixParser doesn't support negative options (see CLI-184)")
    void testNegativeOption() {
        fail("PosixParser doesn't support negative options (see CLI-184)");
    }

    /**
     * PosixParser doesn't support short options with equals sign.
     */
    @Override
    @Test
    @Disabled("PosixParser doesn't support short options with equals sign")
    void testShortWithEqual() {
        fail("PosixParser doesn't support short options with equals sign");
    }

    /**
     * PosixParser doesn't support unambiguous partial long options (case 4).
     */
    @Override
    @Test
    @Disabled("PosixParser doesn't support unambiguous partial long options")
    void testUnambiguousPartialLongOption4() {
        fail("PosixParser doesn't support unambiguous partial long options");
    }
}