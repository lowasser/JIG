package jig;

import java.util.Collection;

import javax.annotation.Nullable;

public interface UndirectedGraph<V> extends Graph<V> {
  public Collection<? extends UndirectedContext<V>> contexts();

  public UndirectedContext<V> match(@Nullable Object o);

  public UndirectedContext<V> matchAny();

  public int degree(@Nullable Object o);
}
