package jig;

import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

public class MapGraph<V> extends AbstractGraph<V> {
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

    public Set<V> predecessors() {
      return (preView == null) ? preView = new PredecessorSet(v, pre) : preView;
    }

    public Set<V> successors() {
      return (sucView == null) ? sucView = new SuccessorSet(v, suc) : sucView;
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

    @Override public boolean add(V w) {
      if (super.add(w)) {
        MapGraph.this.add(w);
        getReverseSet(w).add(v);
        return true;
      }
      return false;
    }

    @Override public boolean addAll(Collection<? extends V> collection) {
      return standardAddAll(collection);
    }

    @Override public void clear() {
      for (V w : this) {
        getReverseSet(w).remove(v);
      }
      super.clear();
    }

    @Override public Iterator<V> iterator() {
      return new VertexIterator(super.iterator());
    }

    @Override public boolean remove(@Nullable Object w) {
      if (super.remove(w)) {
        getReverseSet(w).remove(v);
        return true;
      }
      return false;
    }

    @Override public boolean removeAll(Collection<?> collection) {
      return standardRemoveAll(collection);
    }

    @Override public boolean retainAll(Collection<?> collection) {
      return standardRetainAll(collection);
    }

    @Override protected Set<V> delegate() {
      return backing;
    }

    protected abstract Set<V> getReverseSet(Object w);
  }

  final class PredecessorSet extends AdjacentSet {
    PredecessorSet(V v, Set<V> backing) {
      super(v, backing);
    }

    @Override protected Set<V> getReverseSet(Object w) {
      return adj.get(w).suc;
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

  private final Map<V, Adj> adj;

  public MapGraph() {
    this.adj = Maps.newLinkedHashMap();
  }

  public MapGraph(Set<V> vertices) {
    this.adj = new LinkedHashMap<V, Adj>(vertices.size());
    addAll(vertices);
  }

  @Override public boolean add(V v) {
    if (adj.containsKey(v))
      return false;
    adj.put(v, new Adj(v));
    return true;
  }

  @Override public void clear() {
    adj.clear();
  }

  @Override public Iterator<V> iterator() {
    return new VertexIterator(adj.keySet().iterator());
  }

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

  @Override public int size() {
    return adj.size();
  }
}
