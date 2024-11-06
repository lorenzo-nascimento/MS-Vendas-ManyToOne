package com.haniel.projeto.dto;

import com.haniel.projeto.model.Venda;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class VendaDTO {

    private Long id;

    @NotBlank(message = "Campo total é obrigatório")
    private Double total;
    private LocalDate data;
    private Double comissao;
    private String status;

    private VendedorDTO vendedor;

    public VendaDTO(Venda entity) {
        id = entity.getId();
        total = entity.getTotal();
        data = entity.getData();
        comissao = entity.getComissao();
        status = entity.getStatus();
        vendedor = new VendedorDTO(entity.getVendedor());
    }
}