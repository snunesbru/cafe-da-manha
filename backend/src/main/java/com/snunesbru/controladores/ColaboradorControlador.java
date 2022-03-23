package com.snunesbru.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snunesbru.entidades.Colaborador;
import com.snunesbru.repositorios.ColaboradorRepositorio;

@RestController
@RequestMapping("/api/colaboradores")
public class ColaboradorControlador {
	
	@Autowired
	ColaboradorRepositorio colaboradorRepositorio;
	
	@GetMapping
	public ResponseEntity<List<Colaborador>> obterColaboradores() {
		List<Colaborador> colaboradores = colaboradorRepositorio.obterColaboradores();
		
		return ResponseEntity.ok(colaboradores);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Colaborador> obterColaboradorPeloID(@PathVariable("id") long id) {
		Colaborador colaborador = colaboradorRepositorio.obterColaboradorPeloId(id);
		
		if (colaborador == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return ResponseEntity.ok(colaborador);
	}
	
	@PostMapping
	public ResponseEntity<Colaborador> criarColaborador(@RequestBody Colaborador colaborador) {
		Colaborador colaboradorCriado = colaboradorRepositorio.inserir(colaborador);
		
		return ResponseEntity.ok(colaboradorCriado);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Colaborador> atualizarColaborador(@PathVariable("id") Long id, @RequestBody Colaborador colaborador) {
		Colaborador colaboradorParaAtualizar = colaboradorRepositorio.obterColaboradorPeloId(id);
		
		if (colaboradorParaAtualizar == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		Colaborador colaboradorAtualizado = colaboradorParaAtualizar;
		
		colaboradorAtualizado.setCpf(colaborador.getCpf());
		colaboradorAtualizado.setNome(colaborador.getNome());
		colaboradorAtualizado.setItens(colaborador.getItens());
		
		colaboradorRepositorio.atualizar(colaboradorAtualizado);
		
		return ResponseEntity.ok(colaboradorAtualizado);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deletarColaborador(@PathVariable("id") Long id) {
		try {
			colaboradorRepositorio.excluir(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
