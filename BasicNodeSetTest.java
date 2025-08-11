/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.jxpath;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

/**
 * Test BasicNodeSet
 */
class BasicNodeSetTest extends AbstractJXPathTest {

    protected JXPathContext context;
    protected BasicNodeSet nodeSet;

    /**
     * "Nudge" the nodeSet to update internal state.
     */
    protected void nudge() {
        nodeSet.getPointers();
        nodeSet.getValues();
        nodeSet.getNodes();
    }

    /**
     * Add the pointers for the specified path to {@code nodeSet}.
     *
     * @param xpath
     */
    protected void addPointers(final String xpath) {
        context.iteratePointers(xpath).forEachRemaining(pointer -> nodeSet.add(pointer));
        nudge();
    }

    /**
     * Remove the pointers for the specified path from {@code nodeSet}.
     *
     * @param xpath
     */
    protected void removePointers(final String xpath) {
        context.iteratePointers(xpath).forEachRemaining(pointer -> nodeSet.remove(pointer));
        nudge();
    }

    @Override
    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
        context = JXPathContext.newContext(new TestMixedModelBean());
        nodeSet = new BasicNodeSet();
    }

    /**
     * Test adding pointers to the node set.
     */
    @Test
    void testAddPointers() {
        // Given: An empty node set
        
        // When: Adding integer pointers
        addPointers("/bean/integers");
        
        // Then: Verify pointers, values, and nodes
        List<String> expectedPointerPaths = list(
            "/bean/integers[1]",
            "/bean/integers[2]",
            "/bean/integers[3]",
            "/bean/integers[4]"
        );
        List<Integer> expectedValues = list(1, 2, 3, 4);
        
        assertEquals(expectedPointerPaths.toString(), nodeSet.getPointers().toString(), 
            "Added pointers should match expected paths");
        assertEquals(expectedValues, nodeSet.getValues(), 
            "NodeSet values should match added integers");
        assertEquals(expectedValues, nodeSet.getNodes(), 
            "NodeSet nodes should match added integers");
    }

    /**
     * Test that nodes differ from values in XML models.
     */
    @Test
    void testNodesDifferFromValuesInXmlModel() {
        // Given: An empty node set
        
        // When: Adding contact pointers from XML
        addPointers("/document/vendor/contact");
        
        // Then: Verify pointers, values, and node details
        List<String> expectedPointerPaths = list(
            "/document/vendor[1]/contact[1]",
            "/document/vendor[1]/contact[2]",
            "/document/vendor[1]/contact[3]",
            "/document/vendor[1]/contact[4]"
        );
        List<String> expectedValues = list("John", "Jack", "Jim", "Jack Black");
        
        assertEquals(expectedPointerPaths.toString(), nodeSet.getPointers().toString(), 
            "Contact pointers should match expected paths");
        assertEquals(expectedValues, nodeSet.getValues(), 
            "Contact values should match expected text content");
        
        // Validate node details
        List<Element> elements = nodeSet.getNodes();
        assertEquals(4, elements.size(), 
            "Should have four contact elements");
        
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            assertEquals("contact", element.getTagName(), 
                "Element tag name should be 'contact' at index " + i);
            assertEquals(expectedValues.get(i), element.getFirstChild().getNodeValue(), 
                "Element text content should match at index " + i);
        }
    }

    /**
     * Test removing pointers from the node set.
     */
    @Test
    void testRemovePointers() {
        // Given: Node set with integers
        addPointers("/bean/integers");
        
        // When: Removing last integer
        removePointers("/bean/integers[4]");
        
        // Then: Verify remaining pointers, values, and nodes
        List<String> expectedPointerPaths = list(
            "/bean/integers[1]",
            "/bean/integers[2]",
            "/bean/integers[3]"
        );
        List<Integer> expectedValues = list(1, 2, 3);
        
        assertEquals(expectedPointerPaths.toString(), nodeSet.getPointers().toString(), 
            "Pointers after removal should match expected paths");
        assertEquals(expectedValues, nodeSet.getValues(), 
            "Values after removal should match remaining integers");
        assertEquals(expectedValues, nodeSet.getNodes(), 
            "Nodes after removal should match remaining integers");
    }
}