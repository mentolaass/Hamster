package ru.mentola.hamster.pool.api;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractObjectPool<T> {
    private final List<T> cache = new ArrayList<>();

    @Nullable
    public T get(final Predicate<T> filter) {
        return this.cache.stream()
                .filter(filter)
                .findFirst()
                .orElse(null);
    }

    public void add(final T t) {
        if (!this.cache.contains(t))
            this.cache.add(t);
    }

    public void remove(final Predicate<T> filter) {
        this.cache.removeIf(filter);
    }

    public List<T> getUnModifiableCache() {
        return Collections.unmodifiableList(this.cache);
    }

    public void clear() {
        this.cache.clear();
    }
}
