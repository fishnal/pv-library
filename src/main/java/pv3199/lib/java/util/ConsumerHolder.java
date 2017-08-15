package pv3199.lib.java.util;

import java.util.function.Consumer;

@FunctionalInterface
public interface ConsumerHolder<T> extends Consumer<ForEachHolder<T>> {
}