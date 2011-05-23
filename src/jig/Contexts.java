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
}
