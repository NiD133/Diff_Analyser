/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests for the deprecated {@link PosixParser}.
 * <p>
 * This class inherits from {@link AbstractParserTestCase} and selectively disables
 * tests for features that the {@code PosixParser} does not support.
 * </p>
 */
public class PosixParserTest extends AbstractParserTestCase {

    @Override
    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setUp() {
        super.setUp();
        parser = new PosixParser();
    }

    /**
     * Overrides and disables the superclass test for ambiguous long options
     * specified with a single dash (e.g., {@code -o-ambiguous}). The
     * {@code PosixParser} does not support this syntax.
     */
    @Override
    @Test
    @Disabled("Not supported by PosixParser: ambiguous long options with a single dash.")
    void testAmbiguousLongWithoutEqualSingleDash() {
        // This test is intentionally left blank to disable the inherited test case.
        // The PosixParser's behavior for this scenario is not applicable.
    }
}