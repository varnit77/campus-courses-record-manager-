package edu.ccrm.domain;

import java.util.List;

public interface Searchable<T> {
    List<T> search(String query);
}