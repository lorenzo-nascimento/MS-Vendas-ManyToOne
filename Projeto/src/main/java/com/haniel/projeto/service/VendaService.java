package com.haniel.projeto.service;

import com.haniel.projeto.dto.VendaDTO;
import com.haniel.projeto.model.Venda;
import com.haniel.projeto.model.Vendedor;
import com.haniel.projeto.repository.VendaRepository;
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
public class VendaService {

    @Autowired
    private VendaRepository repository;

    @Autowired
    private VendedorRepository vendedorRepository;

    @Transactional(readOnly = true)
    public List<VendaDTO> findAll() {
        return repository.findAll()
                .stream().map(VendaDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public VendaDTO findById(Long id) {
        Venda entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado")
        );
        return new VendaDTO(entity);
    }

    @Transactional
    public VendaDTO insert(VendaDTO dto) {
        Venda entity = new Venda();
        copyDtoToEntity(dto, entity);

        double comissao = entity.getTotal() * 0.10;
        entity.setComissao(comissao);

        if (entity.getTotal() < 15000) {
            entity.setStatus("BAIXA");
        } else if (entity.getTotal() >= 15000 && entity.getTotal() <= 30000) {
            entity.setStatus("MEDIA");
        } else {
            entity.setStatus("ALTA");
        }
        entity = repository.save(entity);

        return new VendaDTO(entity);
    }
    @Transactional
    public VendaDTO update(Long id, VendaDTO dto){
        try {
            Venda entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new VendaDTO(entity);
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


    private void copyDtoToEntity(VendaDTO dto, Venda entity) {

        entity.setTotal(dto.getTotal());
        entity.setData(dto.getData());
        entity.setComissao(dto.getComissao());
        entity.setStatus(dto.getStatus());

        //objeto completo gerenciado
        Vendedor vendedor = vendedorRepository.getReferenceById(dto.getVendedor().getId());
        entity.setVendedor(vendedor);
    }
}
