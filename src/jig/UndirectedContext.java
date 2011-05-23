package jig;

import java.util.Set;

public interface UndirectedContext<V> extends Context<V> {
  public int degree();

  public Set<V> neighbors();
}
