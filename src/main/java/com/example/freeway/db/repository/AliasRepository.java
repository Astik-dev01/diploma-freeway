package com.example.freeway.db.repository;

import java.util.Optional;

public interface AliasRepository<T> {
    Optional<T> findByAlias(String alias);
}
