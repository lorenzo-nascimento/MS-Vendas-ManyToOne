package com.haniel.projeto.service;


import com.haniel.projeto.dto.VendedorDTO;
import com.haniel.projeto.model.Vendedor;
import com.haniel.projeto.repository.VendedorRepository;
import com.haniel.projeto.service.exception.DatabaseException;
import com.haniel.projeto.service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VendedorService {

    @Autowired
    private VendedorRepository repository;

    @Transactional(readOnly = true)
    public List<VendedorDTO> findAll() {
        return repository.findAll()
                .stream().map(VendedorDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public VendedorDTO findById(Long id) {
        Vendedor entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado")
        );
        return new VendedorDTO(entity);
    }

    @Transactional
    public VendedorDTO insert(VendedorDTO dto) {
        Vendedor entity = new Vendedor();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new VendedorDTO(entity);
    }

    @Transactional
    public VendedorDTO update(Long id, VendedorDTO dto){
        try {
            Vendedor entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new VendedorDTO(entity);
        } catch (EntityNotFoundException e){
            throw  new ResourceNotFoundException("Recurso não encontrado");
        }
    }

    @Transactional
    public void delete(Long id){
        if(!repository.existsById(id)){
            throw  new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private void copyDtoToEntity(VendedorDTO dto, Vendedor entity) {

        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());

    }

}

