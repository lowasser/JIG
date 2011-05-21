package jig;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableSet;

import java.util.Collections;
import java.util.Set;

final class Contexts {
  public static <V> Context<V> unmodifiableContext(final Context<V> context) {
    checkNotNull(context);
    return new AbstractContext<V>() {
      @Override public V vertex() {
        return context.vertex();
      }

      @Override public Set<V> successors() {
        return Collections.unmodifiableSet(context.successors());
      }

      @Override public Set<V> predecessors() {
        return Collections.unmodifiableSet(context.successors());
      }
    };
  }

  public static <V> Context<V> immutableContext(Context<V> context) {
    return immutableContext(context.vertex(), context.predecessors(),
        context.successors());
  }

  public static <V> Context<V> immutableContext(final V v, Set<V> pre,
      Set<V> suc) {
    final Set<V> predecessors = ImmutableSet.copyOf(pre);
    final Set<V> successors = ImmutableSet.copyOf(suc);
    checkNotNull(v);
    return new AbstractContext<V>() {
      @Override public V vertex() {
        return v;
      }

      @Override public Set<V> successors() {
        return successors;
      }

      @Override public Set<V> predecessors() {
        return predecessors;
      }
    };
  }
}
