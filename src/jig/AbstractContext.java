package jig;

import com.google.common.base.Objects;

import javax.annotation.Nullable;

public abstract class AbstractContext<V> implements Context<V> {

  @Override public int hashCode() {
    return Objects.hashCode(vertex(), predecessors(), successors());
  }

  @Override public boolean equals(@Nullable Object obj) {
    if (obj instanceof Context) {
      Context<?> cxt = (Context<?>) obj;
      return Objects.equal(vertex(), cxt.vertex())
          && Objects.equal(predecessors(), cxt.predecessors())
          && Objects.equal(successors(), cxt.successors());
    }
    return false;
  }

  @Override public String toString() {
    return new StringBuilder().append('<').append(predecessors())
      .append(" => ").append(vertex()).append(" => ").append(successors())
      .toString();
  }
}
