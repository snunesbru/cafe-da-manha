package com.snunesbru.repositorios;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.snunesbru.entidades.Colaborador;
import com.snunesbru.entidades.Item;

@Repository
public class ColaboradorRepositorio {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ItemRepositorio itemRepositorio;

	@Transactional
	public Colaborador inserir(Colaborador colaborador) {
		Object ultimoIdObject = entityManager.createNativeQuery("SELECT MAX(id) as ultimo_id  FROM COLABORADOR")
				.getSingleResult();
		Long id = 0L;
		if (ultimoIdObject != null) {
			id = ((BigInteger) ultimoIdObject).longValue();
		}
		colaborador.setId(++id);

		entityManager.createNativeQuery("INSERT INTO COLABORADOR (id, cpf, nome) VALUES (?,?,?)")
				.setParameter(1, colaborador.getId()).setParameter(2, colaborador.getCpf())
				.setParameter(3, colaborador.getNome()).executeUpdate();

		itemRepositorio.inserirItensColaborador(colaborador.getId(), colaborador.getItens());

		return colaborador;
	}

	@Transactional
	public Colaborador obterColaboradorPeloId(Long id) {
		Object[] colaboradorObjects = (Object[]) entityManager
				.createNativeQuery("SELECT id, nome, cpf  FROM COLABORADOR WHERE id = ?").setParameter(1, id)
				.getSingleResult();
		Colaborador colaborador = new Colaborador();
		colaborador.setId(((BigInteger) colaboradorObjects[0]).longValue());
		colaborador.setNome((String) colaboradorObjects[1]);
		colaborador.setCpf((String) colaboradorObjects[2]);
		List<Item> itens = itemRepositorio.obterItensColaborador(colaborador.getId());
		colaborador.setItens(itens);

		return colaborador;
	}

	@Transactional
	public List<Colaborador> obterColaboradores() {
		@SuppressWarnings("unchecked")
		List<Object[]> colaboradoresObjects = entityManager.createNativeQuery("SELECT id, nome, cpf  FROM COLABORADOR")
				.getResultList();

		List<Colaborador> colaboradores = new ArrayList<Colaborador>();

		for (Object[] colaboradorObjects : colaboradoresObjects) {
			Colaborador colaborador = new Colaborador();
			colaborador.setId(((BigInteger) colaboradorObjects[0]).longValue());
			colaborador.setNome((String) colaboradorObjects[1]);
			colaborador.setCpf((String) colaboradorObjects[2]);

			List<Item> itens = itemRepositorio.obterItensColaborador(colaborador.getId());
			colaborador.setItens(itens);

			colaboradores.add(colaborador);
		}

		return colaboradores;
	}

	@Transactional
	public Colaborador atualizar(Colaborador colaborador) {
		entityManager.createNativeQuery("UPDATE COLABORADOR SET cpf = ?, nome = ? WHERE id = ?")
				.setParameter(1, colaborador.getCpf()).setParameter(2, colaborador.getNome())
				.setParameter(3, colaborador.getId()).executeUpdate();

		List<Item> itens = itemRepositorio.obterItensColaborador(colaborador.getId());

		if (colaborador.getItens() != null && !colaborador.getItens().isEmpty()) {
			List<Long> itensParaRemover = itens.stream().filter(item -> !colaborador.getItens().contains(item))
					.map(Item::getId).toList();
			List<Item> itensParaAdicionar = colaborador.getItens().stream().filter(item -> !itens.contains(item))
					.toList();
			itemRepositorio.excluirItens(itensParaRemover);
			itemRepositorio.inserirItensColaborador(colaborador.getId(), itensParaAdicionar);
		} else {
			itemRepositorio.excluirItensColaborador(colaborador.getId());
		}

		return colaborador;
	}

	@Transactional
	public void excluir(Long id) {
		itemRepositorio.excluirItensColaborador(id);
		entityManager.createNativeQuery("DELETE FROM COLABORADOR WHERE id = ?").setParameter(1, id).executeUpdate();
	}

}
