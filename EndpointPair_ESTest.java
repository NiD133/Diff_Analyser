package com.google.common.graph;

import static org.junit.Assert.*;

import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class EndpointPairTest {

  // Factory: ordered

  @Test
  public void orderedFactory_returnsOrderedPair_withExpectedAccessors() {
    EndpointPair<String> pair = EndpointPair.ordered("A", "B");

    assertTrue(pair.isOrdered());
    assertEquals("A", pair.source());
    assertEquals("B", pair.target());
    assertEquals("A", pair.nodeU());
    assertEquals("B", pair.nodeV());
  }

  @Test(expected = NullPointerException.class)
  public void orderedFactory_nullSource_throwsNPE() {
    EndpointPair.ordered(null, "B");
  }

  @Test(expected = NullPointerException.class)
  public void orderedFactory_nullTarget_throwsNPE() {
    EndpointPair.ordered("A", null);
  }

  // Factory: unordered

  @Test
  public void unorderedFactory_returnsUnorderedPair_basicAccessors() {
    EndpointPair<String> pair = EndpointPair.unordered("A", "B");

    assertFalse(pair.isOrdered());
    // The pair must contain both nodes (order does not matter).
    Set<String> nodes = new HashSet<>(Arrays.asList(pair.nodeU(), pair.nodeV()));
    assertEquals(new HashSet<>(Arrays.asList("A", "B")), nodes);
  }

  @Test(expected = NullPointerException.class)
  public void unorderedFactory_nullFirst_throwsNPE() {
    EndpointPair.unordered(null, "B");
  }

  @Test(expected = NullPointerException.class)
  public void unorderedFactory_nullSecond_throwsNPE() {
    EndpointPair.unordered("A", null);
  }

  // Accessor behavior on unordered pairs

  @Test(expected = UnsupportedOperationException.class)
  public void unordered_source_throws() {
    EndpointPair<String> pair = EndpointPair.unordered("A", "B");
    pair.source();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void unordered_target_throws() {
    EndpointPair<String> pair = EndpointPair.unordered("A", "B");
    pair.target();
  }

  // adjacentNode

  @Test
  public void adjacentNode_returnsOtherEndpoint_forUnordered() {
    EndpointPair<String> pair = EndpointPair.unordered("A", "B");
    assertEquals("B", pair.adjacentNode("A"));
    assertEquals("A", pair.adjacentNode("B"));
  }

  @Test
  public void adjacentNode_returnsTarget_forOrdered() {
    EndpointPair<String> pair = EndpointPair.ordered("A", "B");
    assertEquals("B", pair.adjacentNode("A"));
    assertEquals(pair.target(), pair.adjacentNode(pair.source()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void adjacentNode_throwsWhenNodeNotInPair() {
    EndpointPair<String> pair = EndpointPair.unordered("A", "B");
    pair.adjacentNode("C");
  }

  @Test(expected = NullPointerException.class)
  public void adjacentNode_nullNode_throwsNPE() {
    EndpointPair<String> pair = EndpointPair.unordered("A", "A");
    pair.adjacentNode(null);
  }

  @Test
  public void selfLoop_adjacentNodeReturnsSameNode() {
    EndpointPair<String> pair = EndpointPair.unordered("X", "X");
    assertEquals("X", pair.adjacentNode("X"));
  }

  // equals and hashCode

  @Test
  public void equalsAndHashCode_orderedPairs_considerOrder() {
    EndpointPair<String> ab = EndpointPair.ordered("A", "B");
    EndpointPair<String> ab2 = EndpointPair.ordered("A", "B");
    EndpointPair<String> ba = EndpointPair.ordered("B", "A");

    assertEquals(ab, ab2);
    assertEquals(ab.hashCode(), ab2.hashCode());
    assertNotEquals(ab, ba);
  }

  @Test
  public void equalsAndHashCode_unorderedPairs_ignoreOrder() {
    EndpointPair<String> ab = EndpointPair.unordered("A", "B");
    EndpointPair<String> ba = EndpointPair.unordered("B", "A");
    EndpointPair<String> aa = EndpointPair.unordered("A", "A");

    assertEquals(ab, ba);
    assertEquals(ab.hashCode(), ba.hashCode());
    assertNotEquals(ab, aa);
  }

  @Test
  public void equals_isReflexive_andNotEqualToDifferentType() {
    EndpointPair<Integer> pair = EndpointPair.unordered(1, 2);

    assertEquals(pair, pair); // reflexive
    assertNotEquals(pair, "not a pair");
  }

  // Iteration

  @Test
  public void iterator_returnsNodeUThenNodeV_forOrdered() {
    EndpointPair<String> pair = EndpointPair.ordered("A", "B");
    UnmodifiableIterator<String> it = pair.iterator();

    assertTrue(it.hasNext());
    assertEquals("A", it.next());
    assertTrue(it.hasNext());
    assertEquals("B", it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void iterator_returnsTwoNodes_forUnordered() {
    EndpointPair<String> pair = EndpointPair.unordered("A", "B");
    String first = pair.iterator().next();

    // The first element should match nodeU(); overall set should match {A,B}.
    assertEquals(pair.nodeU(), first);
    Set<String> iterated =
        new HashSet<>(Arrays.asList(Iterators.toArray(pair.iterator(), String.class)));
    assertEquals(new HashSet<>(Arrays.asList("A", "B")), iterated);
  }

  // of(Graph, ...)

  @Test
  public void ofGraph_returnsOrderedWhenGraphDirected() {
    MutableGraph<String> g = GraphBuilder.directed().build();
    EndpointPair<String> pair = EndpointPair.of(g, "A", "B");

    assertTrue(pair.isOrdered());
    assertEquals("A", pair.source());
    assertEquals("B", pair.target());
  }

  @Test
  public void ofGraph_returnsUnorderedWhenGraphUndirected() {
    MutableGraph<String> g = GraphBuilder.undirected().build();
    EndpointPair<String> pair = EndpointPair.of(g, "A", "B");

    assertFalse(pair.isOrdered());
    Set<String> nodes = new HashSet<>(Arrays.asList(pair.nodeU(), pair.nodeV()));
    assertEquals(new HashSet<>(Arrays.asList("A", "B")), nodes);
  }

  @Test(expected = NullPointerException.class)
  public void ofGraph_nullGraph_throwsNPE() {
    EndpointPair.of((Graph<?>) null, "A", "B");
  }

  @Test(expected = NullPointerException.class)
  public void ofGraph_nullFirstNode_throwsNPE() {
    MutableGraph<String> g = GraphBuilder.directed().build();
    EndpointPair.of(g, null, "B");
  }

  @Test(expected = NullPointerException.class)
  public void ofGraph_nullSecondNode_throwsNPE() {
    MutableGraph<String> g = GraphBuilder.directed().build();
    EndpointPair.of(g, "A", null);
  }

  // of(Network, ...)

  @Test
  public void ofNetwork_returnsOrderedWhenNetworkDirected() {
    MutableNetwork<String, Integer> n = NetworkBuilder.directed().build();
    EndpointPair<String> pair = EndpointPair.of(n, "A", "B");

    assertTrue(pair.isOrdered());
    assertEquals("A", pair.source());
    assertEquals("B", pair.target());
  }

  @Test
  public void ofNetwork_returnsUnorderedWhenNetworkUndirected() {
    MutableNetwork<String, Integer> n = NetworkBuilder.undirected().build();
    EndpointPair<String> pair = EndpointPair.of(n, "A", "B");

    assertFalse(pair.isOrdered());
    Set<String> nodes = new HashSet<>(Arrays.asList(pair.nodeU(), pair.nodeV()));
    assertEquals(new HashSet<>(Arrays.asList("A", "B")), nodes);
  }

  @Test(expected = NullPointerException.class)
  public void ofNetwork_nullNetwork_throwsNPE() {
    EndpointPair.of((Network<?, ?>) null, "A", "B");
  }

  @Test(expected = NullPointerException.class)
  public void ofNetwork_nullFirstNode_throwsNPE() {
    MutableNetwork<String, Integer> n = NetworkBuilder.directed().build();
    EndpointPair.of(n, null, "B");
  }

  @Test(expected = NullPointerException.class)
  public void ofNetwork_nullSecondNode_throwsNPE() {
    MutableNetwork<String, Integer> n = NetworkBuilder.directed().build();
    EndpointPair.of(n, "A", null);
  }
}