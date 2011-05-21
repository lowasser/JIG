package jig;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

public class MapGraph<V> extends ForwardingSet<V> implements Graph<V> {
  private final class VertexIterator extends ForwardingIterator<V> {
    private final Iterator<V> backingIterator;
    private V prev = null;

    private VertexIterator(Iterator<V> backingIterator) {
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
      removeVertex(prev);
    }
  }

  final class Adj extends AbstractContext<V> {
    private final V v;
    private final Set<V> suc;
    private final Set<V> pre;
    private transient Set<V> preView;
    private transient Set<V> sucView;

    Adj(V v) {
      this.v = v;
      this.suc = Sets.newLinkedHashSet();
      this.pre = Sets.newLinkedHashSet();
    }

    public Set<V> successors() {
      return (sucView == null) ? sucView = new SuccessorSet(v, suc) : sucView;
    }

    public Set<V> predecessors() {
      return (preView == null) ? preView = new PredecessorSet(v, pre) : preView;
    }

    @Override public V vertex() {
      return v;
    }
  }

  abstract class AdjacentSet extends ForwardingSet<V> {
    private final V v;
    private final Set<V> backing;

    AdjacentSet(V v, Set<V> backing) {
      this.v = v;
      this.backing = backing;
    }

    @Override protected Set<V> delegate() {
      return backing;
    }

    protected abstract Set<V> getReverseSet(Object w);

    @Override public Iterator<V> iterator() {
      return new VertexIterator(super.iterator());
    }

    @Override public boolean removeAll(Collection<?> collection) {
      return standardRemoveAll(collection);
    }

    @Override public boolean add(V w) {
      if (super.add(w)) {
        getReverseSet(w).add(v);
        return true;
      }
      return false;
    }

    @Override public boolean remove(@Nullable Object w) {
      if (super.remove(w)) {
        getReverseSet(w).remove(v);
        return true;
      }
      return false;
    }

    @Override public boolean addAll(Collection<? extends V> collection) {
      return standardAddAll(collection);
    }

    @Override public boolean retainAll(Collection<?> collection) {
      return standardRetainAll(collection);
    }

    @Override public void clear() {
      for (V w : this) {
        getReverseSet(w).remove(v);
      }
      super.clear();
    }
  }

  final class SuccessorSet extends AdjacentSet {
    SuccessorSet(V v, Set<V> backing) {
      super(v, backing);
    }

    @Override protected Set<V> getReverseSet(Object w) {
      return adj.get(w).pre;
    }
  }

  final class PredecessorSet extends AdjacentSet {
    PredecessorSet(V v, Set<V> backing) {
      super(v, backing);
    }

    @Override protected Set<V> getReverseSet(Object w) {
      return adj.get(w).suc;
    }
  }

  private final Map<V, Adj> adj;

  @Override public Context<V> match(@Nullable Object v) {
    return adj.get(v);
  }

  @Override public Context<V> matchAny() {
    return adj.values().iterator().next();
  }

  @Override public Context<V> removeVertex(@Nullable Object v) {
    Adj vAdj = adj.remove(v);
    for (V w : vAdj.pre)
      adj.get(w).suc.remove(v);
    for (V w : vAdj.suc)
      adj.get(w).pre.remove(v);
    return Contexts.unmodifiableContext(vAdj);
  }

  public MapGraph() {
    this.adj = Maps.newLinkedHashMap();
  }

  public MapGraph(Set<V> vertices) {
    this.adj = new LinkedHashMap<V, Adj>(vertices.size());
    addAll(vertices);
  }

  @Override public boolean remove(@Nullable Object object) {
    return removeVertex(object) != null;
  }

  @Override public Iterator<V> iterator() {
    final Iterator<V> backingIterator = super.iterator();
    return new VertexIterator(backingIterator);
  }

  @Override public boolean removeAll(Collection<?> collection) {
    return standardRemoveAll(collection);
  }

  @Override public boolean add(V v) {
    if (adj.containsKey(v))
      return false;
    adj.put(v, new Adj(v));
    return true;
  }

  @Override public boolean retainAll(Collection<?> collection) {
    return standardRetainAll(collection);
  }

  @Override public void clear() {
    adj.clear();
  }

  @Override protected Set<V> delegate() {
    return adj.keySet();
  }
}
