package jig;

import java.util.Set;

public interface Context<V> {
  public V vertex();
  public Set<V> successors();
  public Set<V> predecessors();
}
