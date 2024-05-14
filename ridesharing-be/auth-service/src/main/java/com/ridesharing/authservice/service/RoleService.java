package com.ridesharing.authservice.service;

import com.ridesharing.authservice.model.Role;
import com.ridesharing.authservice.repository.RoleRepository;
import com.ridesharing.core.model.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService implements BaseServiceImpl<Role, String> {

    private final RoleRepository repository;


    @Override
    public List<Role> findAll() {
        return repository.findAll();
    }

    @Override
    public Role save(Role entity) {
        return repository.save(entity);
    }

    @Override
    public List<Role> saveAll(List<Role> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public Role findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: "+ id));
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public Optional<Role> findByName(String name){
        return repository.findByName(name);
    }
}
