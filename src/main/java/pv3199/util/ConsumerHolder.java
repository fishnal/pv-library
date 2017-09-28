package pv3199.util;

@FunctionalInterface
public interface ConsumerHolder<T> {
	void accept(ForEachHolder<T> forEachHolder);
}