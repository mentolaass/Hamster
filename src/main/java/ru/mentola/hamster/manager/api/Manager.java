package ru.mentola.hamster.manager.api;

import java.util.List;

public interface Manager<R, F> {
    void init();
    R get(final F f);
    void sync(final R r);
    void add(final R r);
    void remove(final R r);
    List<R> getAll();
    void close();
}