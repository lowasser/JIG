package jig;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Nullable;

public final class ImmutableGraph<V> extends AbstractGraph<V> {
  private final ImmutableMap<V, ImmutableContext<V>> contexts;

  public static <V> ImmutableGraph<V> copyOf(Graph<V> g) {
    checkNotNull(g);
    if (g instanceof ImmutableGraph)
      return (ImmutableGraph<V>) g;
    ImmutableMap.Builder<V, ImmutableContext<V>> builder =
        ImmutableMap.builder();
    for (Context<V> cxt : g.contexts())
      builder.put(cxt.vertex(), ImmutableContext.copyOf(cxt));
    return new ImmutableGraph<V>(builder.build());
  }

  private ImmutableGraph(ImmutableMap<V, ImmutableContext<V>> contexts) {
    this.contexts = contexts;
  }

  @Override public Context<V> match(@Nullable Object o) {
    return contexts.get(o);
  }

  @Override public Iterator<V> iterator() {
    return contexts.keySet().iterator();
  }

  @Override public boolean contains(@Nullable Object o) {
    return contexts.containsKey(o);
  }

  @Override public Collection<ImmutableContext<V>> contexts() {
    return contexts.values();
  }

  private transient Integer hashCode = null;

  @Override public int hashCode() {
    return (hashCode == null) ? hashCode = super.hashCode() : hashCode;
  }

  @Override public ImmutableContext<V> matchAny() {
    return (ImmutableContext<V>) super.matchAny();
  }

  @Override public int size() {
    return contexts.size();
  }
}
