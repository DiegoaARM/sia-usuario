package edu.sia.service;

import edu.sia.model.Rol;

import java.util.List;
import java.util.Optional;

public interface RolService {
    List<Rol> findAll();
    Optional<Rol> findById(Long id);
    Rol save(Rol rol);
    Rol update(Long id, Rol rol);
    void delete(Long id);
}
