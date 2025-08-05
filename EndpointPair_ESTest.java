/*
 * Copyright (C) 2016 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Understandable tests for {@link EndpointPair}.
 */
@RunWith(JUnit4.class)
public class EndpointPairTest {

    private static final String NODE_A = "A";
    private static final String NODE_B = "B";
    private static final String NODE_C = "C";

    // --- Factory Method Tests ---

    @Test
    public void of_directedGraph_createsOrderedPair() {
        Graph<String> directedGraph = GraphBuilder.directed().build();
        EndpointPair<String> pair = EndpointPair.of(directedGraph, NODE_A, NODE_B);

        assertTrue("Pair from a directed graph should be ordered", pair.isOrdered());
        assertEquals(NODE_A, pair.source());
        assertEquals(NODE_B, pair.target());
    }

    @Test
    public void of_undirectedGraph_createsUnorderedPair() {
        Graph<String> undirectedGraph = GraphBuilder.undirected().build();
        EndpointPair<String> pair = EndpointPair.of(undirectedGraph, NODE_A, NODE_B);

        assertFalse("Pair from an undirected graph should be unordered", pair.isOrdered());
    }

    @Test
    public void of_directedNetwork_createsOrderedPair() {
        Network<String, String> directedNetwork = NetworkBuilder.directed().build();
        EndpointPair<String> pair = EndpointPair.of(directedNetwork, NODE_A, NODE_B);

        assertTrue("Pair from a directed network should be ordered", pair.isOrdered());
        assertEquals(NODE_A, pair.source());
        assertEquals(NODE_B, pair.target());
    }

    @Test
    public void of_undirectedNetwork_createsUnorderedPair() {
        Network<String, String> undirectedNetwork = NetworkBuilder.undirected().build();
        EndpointPair<String> pair = EndpointPair.of(undirectedNetwork, NODE_A, NODE_B);

        assertFalse("Pair from an undirected network should be unordered", pair.isOrdered());
    }

    // --- Ordered Pair Tests ---

    @Test
    public void ordered_accessors() {
        EndpointPair<String> pair = EndpointPair.ordered(NODE_A, NODE_B);

        assertEquals(NODE_A, pair.source());
        assertEquals(NODE_B, pair.target());
        assertEquals(NODE_A, pair.nodeU());
        assertEquals(NODE_B, pair.nodeV());
    }

    @Test
    public void ordered_isOrdered() {
        EndpointPair<String> pair = EndpointPair.ordered(NODE_A, NODE_B);
        assertTrue(pair.isOrdered());
    }

    // --- Unordered Pair Tests ---

    @Test
    public void unordered_accessors() {
        EndpointPair<String> pair = EndpointPair.unordered(NODE_A, NODE_B);
        // nodeU and nodeV are consistent, but not defined by input order
        assertTrue(ImmutableList.of(pair.nodeU(), pair.nodeV()).containsAll(ImmutableList.of(NODE_A, NODE_B)));
    }

    @Test
    public void unordered_isOrdered() {
        EndpointPair<String> pair = EndpointPair.unordered(NODE_A, NODE_B);
        assertFalse(pair.isOrdered());
    }

    @Test
    public void unordered_source_throwsException() {
        EndpointPair<String> pair = EndpointPair.unordered(NODE_A, NODE_B);
        try {
            pair.source();
            fail("Expected UnsupportedOperationException for source() on an unordered pair.");
        } catch (UnsupportedOperationException expected) {
            assertTrue(expected.getMessage().contains("Cannot call source()/target()"));
        }
    }

    @Test
    public void unordered_target_throwsException() {
        EndpointPair<String> pair = EndpointPair.unordered(NODE_A, NODE_B);
        try {
            pair.target();
            fail("Expected UnsupportedOperationException for target() on an unordered pair.");
        } catch (UnsupportedOperationException expected) {
            assertTrue(expected.getMessage().contains("Cannot call source()/target()"));
        }
    }

    // --- adjacentNode() Tests ---

    @Test
    public void adjacentNode_orderedPair() {
        EndpointPair<String> pair = EndpointPair.ordered(NODE_A, NODE_B);
        assertEquals(NODE_B, pair.adjacentNode(NODE_A));
        assertEquals(NODE_A, pair.adjacentNode(NODE_B));
    }

    @Test
    public void adjacentNode_unorderedPair() {
        EndpointPair<String> pair = EndpointPair.unordered(NODE_A, NODE_B);
        assertEquals(NODE_B, pair.adjacentNode(NODE_A));
        assertEquals(NODE_A, pair.adjacentNode(NODE_B));
    }

    @Test
    public void adjacentNode_selfLoop() {
        EndpointPair<String> orderedPair = EndpointPair.ordered(NODE_A, NODE_A);
        assertEquals(NODE_A, orderedPair.adjacentNode(NODE_A));

        EndpointPair<String> unorderedPair = EndpointPair.unordered(NODE_A, NODE_A);
        assertEquals(NODE_A, unorderedPair.adjacentNode(NODE_A));
    }

    @Test
    public void adjacentNode_nodeNotInPair_throwsException() {
        EndpointPair<String> pair = EndpointPair.ordered(NODE_A, NODE_B);
        try {
            pair.adjacentNode(NODE_C);
            fail("Expected IllegalArgumentException when node is not in the pair.");
        } catch (IllegalArgumentException expected) {
            assertTrue(expected.getMessage().contains("does not contain node " + NODE_C));
        }
    }

    // --- Equality and HashCode Tests ---

    @Test
    public void equals_orderedPairs() {
        EndpointPair<String> pair1 = EndpointPair.ordered(NODE_A, NODE_B);
        EndpointPair<String> pair2 = EndpointPair.ordered(NODE_A, NODE_B);
        EndpointPair<String> differentOrder = EndpointPair.ordered(NODE_B, NODE_A);

        assertEquals("Ordered pairs with same source and target should be equal", pair1, pair2);
        assertEquals("An object should be equal to itself", pair1, pair1);
        assertNotEquals("Ordered pairs are sensitive to order", pair1, differentOrder);
        assertNotEquals("An object should not be equal to null", pair1, null);
        assertNotEquals("An object should not be equal to an object of a different type", pair1, "a string");
    }

    @Test
    public void equals_unorderedPairs_nodeOrderIsIgnored() {
        EndpointPair<String> pair1 = EndpointPair.unordered(NODE_A, NODE_B);
        EndpointPair<String> pair2 = EndpointPair.unordered(NODE_B, NODE_A);

        assertEquals("Unordered pairs should be equal regardless of node order", pair1, pair2);
    }

    @Test
    public void equals_orderedVsUnordered() {
        EndpointPair<String> orderedPair = EndpointPair.ordered(NODE_A, NODE_B);
        EndpointPair<String> unorderedPair = EndpointPair.unordered(NODE_A, NODE_B);

        assertNotEquals("An ordered pair cannot be equal to an unordered pair", orderedPair, unorderedPair);
    }

    @Test
    public void hashCode_orderedPairs() {
        EndpointPair<String> pair1 = EndpointPair.ordered(NODE_A, NODE_B);
        EndpointPair<String> pair2 = EndpointPair.ordered(NODE_A, NODE_B);
        EndpointPair<String> differentOrder = EndpointPair.ordered(NODE_B, NODE_A);

        assertEquals("Hash code should be consistent for equal ordered pairs", pair1.hashCode(), pair2.hashCode());
        assertNotEquals("Hash code should differ for ordered pairs with different order", pair1.hashCode(), differentOrder.hashCode());
    }

    @Test
    public void hashCode_unorderedPairs_nodeOrderIsIgnored() {
        EndpointPair<String> pair1 = EndpointPair.unordered(NODE_A, NODE_B);
        EndpointPair<String> sameNodesDifferentOrder = EndpointPair.unordered(NODE_B, NODE_A);

        assertEquals("HashCode for unordered pairs should not depend on order",
            pair1.hashCode(), sameNodesDifferentOrder.hashCode());
    }

    // --- Iterator Test ---

    @Test
    public void iterator_ordered() {
        EndpointPair<String> pair = EndpointPair.ordered(NODE_A, NODE_B);
        assertTrue(Iterators.elementsEqual(
            ImmutableList.of(NODE_A, NODE_B).iterator(),
            pair.iterator()));
    }

    @Test
    public void iterator_unordered() {
        EndpointPair<String> pair = EndpointPair.unordered(NODE_A, NODE_B);
        // The iteration order is defined as nodeU, nodeV.
        assertTrue(Iterators.elementsEqual(
            ImmutableList.of(pair.nodeU(), pair.nodeV()).iterator(),
            pair.iterator()));
    }

    // --- Precondition (Null) Tests ---

    @Test(expected = NullPointerException.class)
    public void ordered_nullSource_throwsException() {
        EndpointPair.ordered(null, NODE_B);
    }

    @Test(expected = NullPointerException.class)
    public void ordered_nullTarget_throwsException() {
        EndpointPair.ordered(NODE_A, null);
    }

    @Test(expected = NullPointerException.class)
    public void unordered_nullNodeU_throwsException() {
        EndpointPair.unordered(null, NODE_B);
    }

    @Test(expected = NullPointerException.class)
    public void unordered_nullNodeV_throwsException() {
        EndpointPair.unordered(NODE_A, null);
    }

    @Test(expected = NullPointerException.class)
    public void of_nullGraph_throwsException() {
        EndpointPair.of((Graph<String>) null, NODE_A, NODE_B);
    }

    @Test(expected = NullPointerException.class)
    public void of_nullNetwork_throwsException() {
        EndpointPair.of((Network<String, String>) null, NODE_A, NODE_B);
    }

    @Test(expected = NullPointerException.class)
    public void adjacentNode_nullArgument_throwsException() {
        EndpointPair<String> pair = EndpointPair.ordered(NODE_A, NODE_B);
        pair.adjacentNode(null);
    }
}