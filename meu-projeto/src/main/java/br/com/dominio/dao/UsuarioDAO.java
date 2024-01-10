package br.com.dominio.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.com.dominio.model.Usuario;

public class UsuarioDAO {
	private EntityManagerFactory emf;

	public UsuarioDAO() {
		emf = Persistence.createEntityManagerFactory("projeto");
	}

	public void salvar(Usuario usuario) {
		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();

			String query = em.createQuery("SELECT u FROM Usuario u WHERE u.nome = :nome")
					.setParameter("nome", usuario.getNome()).getResultList().toString();

			
			if (query.length() == 2) {
				em.persist(usuario);
				em.getTransaction().commit();
				System.out.println("Usuário salvo com sucesso!");
			} else {
				System.out.println("Já existe um usuário com o mesmo nome. Não foi possível adicionar.");
			}
		} catch (Exception e) {
			if (em.getTransaction() != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			System.out.println("Erro ao salvar usuário: " + e.getMessage());
		} finally {
			em.close();
		}
	}

	public boolean buscar(String login, String senha) {
		EntityManager em = emf.createEntityManager();

		try {
			String query = em.createQuery("SELECT u FROM Usuario u WHERE u.login = :login AND u.senha = :senha")
					.setParameter("login", login).setParameter("senha", senha).getResultList().toString();

			return query.length() == 2 ? false : true; // Se a lista não estiver vazia, o usuário existe
		} catch (Exception e) {
			System.out.println("Erro ao buscar usuário: " + e.getMessage());
			return false;
		} finally {
			em.close();
		}
	}
}
