package jig;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public final class ImmutableContext<V> extends AbstractContext<V> {
  public static <V> ImmutableContext<V> of(V v, Set<V> predecessors,
      Set<V> successors) {
    return new ImmutableContext<V>(v, predecessors, successors);
  }

  public static <V> ImmutableContext<V> copyOf(Context<V> context) {
    checkNotNull(context);
    if (context instanceof ImmutableContext) {
      return (ImmutableContext<V>) context;
    }
    return of(context.vertex(), context.predecessors(), context.successors());
  }

  private final V v;
  private final Set<V> predecessors;
  private final Set<V> successors;

  private ImmutableContext(V v, Set<V> predecessors, Set<V> successors) {
    this.v = checkNotNull(v);
    this.predecessors = ImmutableSet.copyOf(predecessors);
    this.successors = ImmutableSet.copyOf(successors);
  }

  @Override public V vertex() {
    return v;
  }

  @Override public Set<V> successors() {
    return successors;
  }

  @Override public Set<V> predecessors() {
    return predecessors;
  }
}