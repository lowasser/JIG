package jig;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nullable;

public interface Graph<V> {
  public Context<V> match(@Nullable Object o);

  public Context<V> matchAny();

  public Context<V> removeVertex(@Nullable Object o);

  public boolean containsEdge(@Nullable Object v, @Nullable Object w);

  public boolean addEdge(V v, V w);

  public boolean removeEdge(@Nullable Object v, @Nullable Object w);

  public Collection<? extends Context<V>> contexts();

  public Set<V> vertices();

  public int inDegree(@Nullable Object o);

  public int outDegree(@Nullable Object o);
}
