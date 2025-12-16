package com.example.migration.domain.repository;

import com.example.migration.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface LegacyClientRepository {

    List<Client> findAll();

    Optional<Client> findById(Long id);

    void save(Client client);
}


