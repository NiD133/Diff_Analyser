package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.graph.*;
import java.util.Iterator;

/**
 * Tests for EndpointPair class, which represents the two endpoints of an edge in a graph.
 * EndpointPair can be either ordered (for directed graphs) or unordered (for undirected graphs).
 */
public class EndpointPair_ESTest {

    // Test data
    private static final String NODE_A = "A";
    private static final String NODE_B = "B";
    private static final Integer NODE_1 = 1;
    private static final Integer NODE_2 = 2;

    // ========== Factory Method Tests ==========

    @Test
    public void testCreateOrderedEndpointPair() {
        EndpointPair<String> pair = EndpointPair.ordered(NODE_A, NODE_B);
        
        assertNotNull("Ordered endpoint pair should be created", pair);
        assertTrue("Should be ordered", pair.isOrdered());
        assertEquals("Source should be NODE_A", NODE_A, pair.source());
        assertEquals("Target should be NODE_B", NODE_B, pair.target());
    }

    @Test
    public void testCreateUnorderedEndpointPair() {
        EndpointPair<String> pair = EndpointPair.unordered(NODE_A, NODE_B);
        
        assertNotNull("Unordered endpoint pair should be created", pair);
        assertFalse("Should not be ordered", pair.isOrdered());
        assertEquals("NodeU should be NODE_A", NODE_A, pair.nodeU());
        assertEquals("NodeV should be NODE_B", NODE_B, pair.nodeV());
    }

    @Test
    public void testCreateFromDirectedGraph() {
        Graph<Integer> directedGraph = GraphBuilder.directed().build();
        
        EndpointPair<Integer> pair = EndpointPair.of(directedGraph, NODE_1, NODE_2);
        
        assertNotNull("Endpoint pair should be created from directed graph", pair);
        assertTrue("Should be ordered for directed graph", pair.isOrdered());
    }

    @Test
    public void testCreateFromUndirectedGraph() {
        Graph<Integer> undirectedGraph = GraphBuilder.undirected().build();
        
        EndpointPair<Integer> pair = EndpointPair.of(undirectedGraph, NODE_1, NODE_2);
        
        assertNotNull("Endpoint pair should be created from undirected graph", pair);
        assertFalse("Should not be ordered for undirected graph", pair.isOrdered());
    }

    @Test
    public void testCreateFromDirectedNetwork() {
        Network<String, String> directedNetwork = NetworkBuilder.directed().build();
        
        EndpointPair<String> pair = EndpointPair.of(directedNetwork, NODE_A, NODE_B);
        
        assertNotNull("Endpoint pair should be created from directed network", pair);
        assertTrue("Should be ordered for directed network", pair.isOrdered());
    }

    @Test
    public void testCreateFromUndirectedNetwork() {
        Network<String, String> undirectedNetwork = NetworkBuilder.undirected().build();
        
        EndpointPair<String> pair = EndpointPair.of(undirectedNetwork, NODE_A, NODE_B);
        
        assertNotNull("Endpoint pair should be created from undirected network", pair);
        assertFalse("Should not be ordered for undirected network", pair.isOrdered());
    }

    // ========== Node Access Tests ==========

    @Test
    public void testNodeAccessInOrderedPair() {
        EndpointPair<String> orderedPair = EndpointPair.ordered(NODE_A, NODE_B);
        
        assertEquals("Source should be NODE_A", NODE_A, orderedPair.source());
        assertEquals("Target should be NODE_B", NODE_B, orderedPair.target());
        assertEquals("NodeU should be NODE_A", NODE_A, orderedPair.nodeU());
        assertEquals("NodeV should be NODE_B", NODE_B, orderedPair.nodeV());
    }

    @Test
    public void testNodeAccessInUnorderedPair() {
        EndpointPair<String> unorderedPair = EndpointPair.unordered(NODE_A, NODE_B);
        
        assertEquals("NodeU should be NODE_A", NODE_A, unorderedPair.nodeU());
        assertEquals("NodeV should be NODE_B", NODE_B, unorderedPair.nodeV());
    }

    @Test
    public void testAdjacentNodeInSelfLoop() {
        EndpointPair<String> selfLoop = EndpointPair.unordered(NODE_A, NODE_A);
        
        String adjacent = selfLoop.adjacentNode(NODE_A);
        assertEquals("Adjacent node in self-loop should be the same node", NODE_A, adjacent);
    }

    @Test
    public void testAdjacentNodeInOrderedPair() {
        EndpointPair<String> orderedPair = EndpointPair.ordered(NODE_A, NODE_B);
        
        assertEquals("Adjacent to NODE_A should be NODE_B", NODE_B, orderedPair.adjacentNode(NODE_A));
        assertEquals("Adjacent to NODE_B should be NODE_A", NODE_A, orderedPair.adjacentNode(NODE_B));
    }

    // ========== Iterator Tests ==========

    @Test
    public void testIterator() {
        EndpointPair<String> pair = EndpointPair.unordered(NODE_A, NODE_B);
        
        Iterator<String> iterator = pair.iterator();
        assertNotNull("Iterator should not be null", iterator);
        assertTrue("Iterator should have elements", iterator.hasNext());
    }

    // ========== Equality Tests ==========

    @Test
    public void testOrderedPairEquality() {
        EndpointPair<String> pair1 = EndpointPair.ordered(NODE_A, NODE_B);
        EndpointPair<String> pair2 = EndpointPair.ordered(NODE_A, NODE_B);
        EndpointPair<String> pair3 = EndpointPair.ordered(NODE_B, NODE_A);
        
        assertTrue("Same ordered pairs should be equal", pair1.equals(pair2));
        assertFalse("Different ordered pairs should not be equal", pair1.equals(pair3));
        assertTrue("Pair should equal itself", pair1.equals(pair1));
    }

    @Test
    public void testUnorderedPairEquality() {
        EndpointPair<String> pair1 = EndpointPair.unordered(NODE_A, NODE_B);
        EndpointPair<String> pair2 = EndpointPair.unordered(NODE_B, NODE_A);
        EndpointPair<String> pair3 = EndpointPair.unordered(NODE_A, NODE_A);
        
        assertTrue("Unordered pairs with same nodes should be equal", pair1.equals(pair2));
        assertFalse("Unordered pairs with different nodes should not be equal", pair1.equals(pair3));
        assertTrue("Pair should equal itself", pair1.equals(pair1));
    }

    @Test
    public void testOrderedVsUnorderedEquality() {
        EndpointPair<String> orderedPair = EndpointPair.ordered(NODE_A, NODE_B);
        EndpointPair<String> unorderedPair = EndpointPair.unordered(NODE_A, NODE_B);
        
        assertFalse("Ordered and unordered pairs should never be equal", 
                   orderedPair.equals(unorderedPair));
    }

    @Test
    public void testEqualityWithNonEndpointPair() {
        EndpointPair<String> pair = EndpointPair.unordered(NODE_A, NODE_B);
        String notAnEndpointPair = "not an endpoint pair";
        
        assertFalse("EndpointPair should not equal non-EndpointPair objects", 
                   pair.equals(notAnEndpointPair));
    }

    // ========== Exception Tests ==========

    @Test(expected = NullPointerException.class)
    public void testOrderedWithNullNodes() {
        EndpointPair.ordered(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testUnorderedWithNullNodes() {
        EndpointPair.unordered(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFromNullGraph() {
        EndpointPair.of((Graph<?>) null, NODE_A, NODE_B);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFromNullNetwork() {
        EndpointPair.of((Network<?, ?>) null, NODE_A, NODE_B);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFromGraphWithNullNodes() {
        Graph<String> graph = GraphBuilder.directed().build();
        EndpointPair.of(graph, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFromNetworkWithNullNodes() {
        Network<String, String> network = NetworkBuilder.directed().build();
        EndpointPair.of(network, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testAdjacentNodeWithNull() {
        EndpointPair<String> pair = EndpointPair.unordered(NODE_A, NODE_B);
        pair.adjacentNode(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdjacentNodeWithNonContainedNode() {
        EndpointPair<String> pair = EndpointPair.unordered(NODE_A, NODE_B);
        pair.adjacentNode("C"); // Node C is not in the pair
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSourceOnUnorderedPair() {
        EndpointPair<String> unorderedPair = EndpointPair.unordered(NODE_A, NODE_B);
        unorderedPair.source(); // Should throw exception
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTargetOnUnorderedPair() {
        EndpointPair<String> unorderedPair = EndpointPair.unordered(NODE_A, NODE_B);
        unorderedPair.target(); // Should throw exception
    }
}