package com.daviof.minhasfinancas.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class UsuarioDTO {

	private String email;
	private String nome;
	private String senha;
	
}
