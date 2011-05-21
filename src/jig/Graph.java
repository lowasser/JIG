package jig;

import java.util.Set;

import javax.annotation.Nullable;

public interface Graph<V> extends Set<V> {
  public Context<V> match(@Nullable Object o);

  public Context<V> matchAny();

  public Context<V> removeVertex(@Nullable Object o);
}
