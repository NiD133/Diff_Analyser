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
 * Tests for the deprecated {@link PosixParser}.
 *
 * <p>
 * This class inherits from {@link AbstractParserTestCase}, which provides a suite of
 * tests for common command-line parsing behavior. However, the {@link PosixParser}
 * implements a stricter, more traditional interpretation of POSIX-style options
 * and does not support certain features found in other parsers like {@link GnuParser}.
 * </p>
 * <p>
 * Specifically, {@code PosixParser} does not support:
 * </p>
 * <ul>
 *   <li>Long options with a single dash (e.g., {@code -longoption}).</li>
 *   <li>Attaching arguments to short options with an equals sign (e.g., {@code -f=bar}).</li>
 *   <li>Handling of negative numbers as arguments.</li>
 * </ul>
 * <p>
 * Consequently, this test class overrides and disables the corresponding tests
 * from the base class that cover these unsupported features.
 * </p>
 */
class PosixParserTest extends AbstractParserTestCase {
    @Override
    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setUp() {
        super.setUp();
        parser = new PosixParser();
    }

    // ----------------------------------------------------------------------
    // Disabled Tests from AbstractParserTestCase
    //
    // The following tests are inherited from the base class but disabled
    // because PosixParser does not support the tested features.
    // ----------------------------------------------------------------------

    @Override
    @Test
    @Disabled("Unsupported by PosixParser: long options with a single dash.")
    void testAmbiguousLongWithoutEqualSingleDash() {
    }

    @Override
    @Test
    @Disabled("Unsupported by PosixParser: long options with a single dash.")
    void testAmbiguousLongWithoutEqualSingleDash2() {
    }

    @Override
    @Test
    @Disabled("Unsupported by PosixParser: partial long options with a single dash.")
    void testAmbiguousPartialLongOption4() {
    }

    @Override
    @Test
    @Disabled("Unsupported by PosixParser: specific behavior of '--' token.")
    void testDoubleDash2() {
    }

    @Override
    @Test
    @Disabled("Unsupported by PosixParser: long options with a single dash (e.g. -file=name).")
    void testLongWithEqualSingleDash() {
    }

    @Override
    @Test
    @Disabled("Unsupported by PosixParser: long options with a single dash (e.g. -file name).")
    void testLongWithoutEqualSingleDash() {
    }

    @Override
    @Test
    @Disabled("Unsupported by PosixParser: long options with a single dash.")
    void testLongWithUnexpectedArgument1() {
    }

    @Override
    @Test
    @Disabled("Unsupported by PosixParser: negative numbers as arguments (CLI-184).")
    void testNegativeOption() {
    }

    @Override
    @Test
    @Disabled("Unsupported by PosixParser: short options with '=' for arguments (e.g. -f=bar).")
    void testShortWithEqual() {
    }

    @Override
    @Test
    @Disabled("Unsupported by PosixParser: partial long options with a single dash.")
    void testUnambiguousPartialLongOption4() {
    }
}