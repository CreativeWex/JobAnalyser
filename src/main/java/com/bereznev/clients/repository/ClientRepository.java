package com.bereznev.clients.repository;
/*
    =====================================
    @project ClientsMicroservice
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.clients.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
  @Query("select c from Client c where c.email = ?1")
  Optional<Client> findClient(String email);
}
