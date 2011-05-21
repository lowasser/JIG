package jig;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Collections2;
import com.google.common.collect.ForwardingCollection;
import com.google.common.collect.ForwardingIterator;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Nullable;

public abstract class AbstractGraph<V> extends AbstractCollection<V> implements
    Graph<V> {
  final class VertexSet extends ForwardingCollection<V> implements Set<V> {
    @Override protected Collection<V> delegate() {
      return AbstractGraph.this;
    }

    @Override public int hashCode() {
      int hashCode = 0;
      for (V v : this)
        hashCode += v.hashCode();
      return hashCode;
    }

    @Override public boolean equals(@Nullable Object obj) {
      if (obj instanceof Set) {
        Set<?> s = (Set<?>) obj;
        return this == obj || (size() == s.size() && containsAll(s));
      }
      return false;
    }
  }

  final class VertexIterator extends ForwardingIterator<V> {
    private final Iterator<V> backingIterator;
    private V prev = null;

    VertexIterator(Iterator<V> backingIterator) {
      this.backingIterator = backingIterator;
    }

    @Override protected Iterator<V> delegate() {
      return backingIterator;
    }

    @Override public V next() {
      return prev = super.next();
    }

    @Override public void remove() {
      checkState(prev != null);
      AbstractGraph.this.remove(prev);
    }
  }

  @Override public boolean equals(@Nullable Object obj) {
    if (obj instanceof Graph) {
      Graph<?> g = (Graph<?>) obj;
      if (!Objects.equal(vertices(), g.vertices()))
        return false;
      for (V v : vertices())
        if (!Objects.equal(match(v), g.match(v)))
          return false;
      return true;
    }
    return false;
  }

  @Override public int hashCode() {
    int hashCode = 0;
    for (Context<V> context : contexts())
      hashCode += context.hashCode();
    return hashCode;
  }

  @Override public String toString() {
    return contexts().toString();
  }

  @Override public Context<V> matchAny() {
    V v = vertices().iterator().next();
    return match(v);
  }

  @Override public boolean containsEdge(Object v, Object w) {
    Context<V> vCxt = match(v);
    return vCxt != null && vCxt.successors().contains(w);
  }

  @Override public boolean addEdge(V v, V w) {
    return add(v) | add(w) | match(v).successors().add(w);
  }

  @Override public boolean removeEdge(Object v, Object w) {
    Context<V> vCxt = match(v);
    return vCxt != null && vCxt.successors().remove(w);
  }

  private transient Collection<Context<V>> contexts;

  @Override public Collection<Context<V>> contexts() {
    if (contexts == null) {
      return contexts =
          Collections2.transform(this, new Function<V, Context<V>>() {
            @Override public Context<V> apply(V v) {
              return match(v);
            }
          });
    }
    return contexts;
  }

  @Override public Context<V> removeVertex(@Nullable Object o) {
    throw new UnsupportedOperationException();
  }

  @Override public boolean remove(@Nullable Object o) {
    return removeVertex(o) != null;
  }

  private transient Set<V> vertices;

  @Override public Set<V> vertices() {
    return (vertices == null) ? vertices = new VertexSet() : vertices;
  }
}
