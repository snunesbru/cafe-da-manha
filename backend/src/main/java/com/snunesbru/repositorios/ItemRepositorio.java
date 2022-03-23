package com.snunesbru.repositorios;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.snunesbru.entidades.Item;

@Repository
public class ItemRepositorio {
	
	@PersistenceContext
    private EntityManager entityManager;

	@Transactional
	public void inserirItensColaborador(Long idColaborador, List<Item> itens) {
		Object ultimoIdItemObject = entityManager.createNativeQuery("SELECT MAX(id) as ultimo_id  FROM ITEM")
				.getSingleResult();
		Long idItem = 0L;
		if (ultimoIdItemObject != null) {
			idItem = ((BigInteger) ultimoIdItemObject).longValue();
		}
		for (Item item : itens) {
			item.setId(++idItem);
			entityManager.createNativeQuery("INSERT INTO ITEM (id, nome, colaborador_id) VALUES (?,?,?)")
					.setParameter(1, item.getId()).setParameter(2, item.getNome()).setParameter(3, idColaborador)
					.executeUpdate();
		}
	}

	@Transactional
	public List<Item> obterItensColaborador(Long idColaborador) {
		@SuppressWarnings("unchecked")
		List<Object[]> itensObjects = entityManager
				.createNativeQuery("SELECT id, nome FROM ITEM WHERE colaborador_id = ?").setParameter(1, idColaborador)
				.getResultList();

		List<Item> itens = new ArrayList<Item>();

		for (Object[] itemObjects : itensObjects) {
			Item item = new Item();
			item.setId(((BigInteger) itemObjects[0]).longValue());
			item.setNome((String) itemObjects[1]);
			itens.add(item);
		}
		return itens;
	}

	@Transactional
	public void excluirItensColaborador(Long id) {
		entityManager.createNativeQuery("DELETE FROM ITEM WHERE colaborador_id = ?").setParameter(1, id)
				.executeUpdate();
	}

	@Transactional
	public void excluirItens(List<Long> ids) {
		if (!ids.isEmpty()) {
			entityManager.createNativeQuery("DELETE FROM ITEM WHERE id IN (:ids)").setParameter("ids", ids).executeUpdate();
		}
	}
	
}
