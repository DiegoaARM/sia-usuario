package edu.sia.service.impl;

import edu.sia.model.Rol;
import edu.sia.repository.RolRepository;
import edu.sia.service.RolService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    @Override
    public Optional<Rol> findById(Long id) {
        return rolRepository.findById(id);
    }

    @Override
    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public Rol update(Long id, Rol rol) {
        return rolRepository.findById(id)
                .map(existing -> {
                    existing.setNombreRol(rol.getNombreRol());
                    // agrega más campos si existen
                    return rolRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));
    }

    @Override
    public void delete(Long id) {
        rolRepository.deleteById(id);
    }
}

