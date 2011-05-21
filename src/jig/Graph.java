package jig;

import java.util.Set;

import javax.annotation.Nullable;

public interface Graph<V> {
  public boolean isEmpty();

  public Set<V> vertices();

  public Context<V> match(V v);

  public boolean contains(V v);

  public Context<V> matchAny();

  public Context<V> remove(@Nullable Object o);
}
