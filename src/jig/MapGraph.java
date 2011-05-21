package math.graphs;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

public class MapGraph<V> implements Graph<V> {
  static final class Adj<V> {
    private final Set<V> suc;
    private final Set<V> pre;

    Adj() {
      this.suc = Sets.newLinkedHashSet();
      this.pre = Sets.newLinkedHashSet();
    }

    public Set<V> successors() {
      return suc;
    }

    public Set<V> predecessors() {
      return pre;
    }
  }

  final class SuccessorSet extends ForwardingSet<V> {
    private final V v;
    private final Set<V> backing;

    private SuccessorSet(V v, Set<V> backing) {
      this.v = v;
      this.backing = backing;
    }

    @Override protected Set<V> delegate() {
      return backing;
    }

    @Override public Iterator<V> iterator() {
      final Iterator<V> backingIterator = super.iterator();
      return new ForwardingIterator<V>() {
        private V prev = null;

        @Override protected Iterator<V> delegate() {
          return backingIterator;
        }

        @Override public V next() {
          return prev = super.next();
        }

        @Override public void remove() {
          checkState(prev != null);
          SuccessorSet.this.remove(prev);
        }
      };
    }

    @Override public boolean removeAll(Collection<?> collection) {
      return standardRemoveAll(collection);
    }

    @Override public boolean add(V w) {
      if (super.add(w)) {
        adj.get(w).pre.add(v);
        return true;
      }
      return false;
    }

    @Override public boolean remove(@Nullable Object w) {
      if (super.remove(w)) {
        adj.get(w).pre.remove(v);
        return true;
      }
      return false;
    }

    @Override public boolean addAll(Collection<? extends V> collection) {
      // TODO Auto-generated method stub
      return super.addAll(collection);
    }

    @Override public boolean retainAll(Collection<?> collection) {
      // TODO Auto-generated method stub
      return super.retainAll(collection);
    }

    @Override public void clear() {
      // TODO Auto-generated method stub
      super.clear();
    }
  }

  final class VertexSet extends ForwardingSet<V> {
    @Override protected Set<V> delegate() {
      return adj.keySet();
    }

    @Override public Iterator<V> iterator() {
      final Iterator<V> backingIterator = delegate().iterator();
      return new ForwardingIterator<V>() {
        private V prev = null;

        @Override protected Iterator<V> delegate() {
          return backingIterator;
        }

        @Override public V next() {
          return prev = super.next();
        }

        @Override public void remove() {
          checkState(prev != null);
          MapGraph.this.remove(prev);
        }
      };
    }

    @Override public boolean removeAll(Collection<?> collection) {
      return standardRemoveAll(collection);
    }

    @Override public boolean add(V v) {
      if (adj.containsKey(v)) {
        return false;
      }
      adj.put(v, new Adj<V>());
      return true;
    }

    @Override public boolean remove(@Nullable Object v) {
      return MapGraph.this.remove(v) != null;
    }

    @Override public boolean addAll(Collection<? extends V> collection) {
      return standardAddAll(collection);
    }

    @Override public boolean retainAll(Collection<?> collection) {
      return standardRetainAll(collection);
    }

    @Override public void clear() {
      adj.clear();
    }
  }

  private final Map<V, Adj<V>> adj;

  @Override public boolean isEmpty() {
    return adj.isEmpty();
  }

  @Override public Set<V> vertices() {
    return adj.keySet();
  }

  @Override public Context<V> match(V v) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override public boolean contains(V v) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override public Context<V> matchAny() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override public void remove(V v) {
    // TODO Auto-generated method stub

  }

}
