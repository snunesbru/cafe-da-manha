package com.snunesbru.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "colaborador")
public class Colaborador implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8205518584234042202L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "nome")
	private String nome;

	@Column(name = "cpf", unique = true)
	private String cpf;

	@OneToMany(mappedBy = "colaborador", targetEntity = Item.class, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Item> itens = new ArrayList<Item>();

	public String getNome() {
		return nome;
	}

	public Colaborador(Long id) {
		super();
		this.id = id;
	}

	public Colaborador() {
		super();
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Item> getItens() {
		return itens;
	}

	public void setItens(List<Item> novosItens) {
		this.itens = novosItens;
	}
}
