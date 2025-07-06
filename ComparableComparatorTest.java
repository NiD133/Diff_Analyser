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
package org.apache.commons.collections4.comparators;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Unit tests for the {@link ComparableComparator} class.
 */
@SuppressWarnings("boxing")
class ComparableComparatorTest extends AbstractComparatorTest<Integer> {

    /**
     * Provides a list of integers in natural order for testing.
     *
     * @return a list of integers from 1 to 5 in ascending order.
     */
    @Override
    public List<Integer> getComparableObjectsOrdered() {
        List<Integer> orderedList = new LinkedList<>();
        orderedList.add(1);
        orderedList.add(2);
        orderedList.add(3);
        orderedList.add(4);
        orderedList.add(5);
        return orderedList;
    }

    /**
     * Returns the compatibility version of the test.
     *
     * @return the version string "4".
     */
    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    /**
     * Creates an instance of {@link ComparableComparator} for testing.
     *
     * @return a new instance of ComparableComparator.
     */
    @Override
    public Comparator<Integer> makeObject() {
        return new ComparableComparator<>();
    }

    // Uncomment and implement this test if serialization testing is required.
    /*
    @Test
    public void testCreate() throws Exception {
        // Serialize the comparator and write it to a file for compatibility testing.
        writeExternalFormToDisk((java.io.Serializable) makeObject(), 
            "src/test/resources/data/test/ComparableComparator.version4.obj");
    }
    */
}