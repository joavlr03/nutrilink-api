package br.com.joavlr03.nutrilink_api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.joavlr03.nutrilink_api.model.Doadora;

public interface DoadoraRepository extends JpaRepository<Doadora, UUID> {
    
    Optional<Doadora> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

}
