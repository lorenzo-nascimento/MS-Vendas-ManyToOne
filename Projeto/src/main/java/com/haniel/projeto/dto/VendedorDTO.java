package com.haniel.projeto.dto;
import com.haniel.projeto.model.Vendedor;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class VendedorDTO {

    private Long id;

    @NotBlank(message = "Campo nome é obrigatório")
    private String nome;
    @NotBlank(message = "Campo email é obrigatório")
    private String email;


    public VendedorDTO(Vendedor entity) {
        id = entity.getId();
        nome = entity.getNome();
        email = entity.getEmail();

    }
}

