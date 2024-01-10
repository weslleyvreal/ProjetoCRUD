package br.com.dominio.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import br.com.dominio.model.Product;

public class ProductDAO {

	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("projeto");
	private EntityManager em = emf.createEntityManager();

	private List<Product> productList = new ArrayList<>();

	public Product findProductById(Long id) {
		return em.find(Product.class, id);
	}

	public boolean addProduct(Product product) {

		EntityTransaction transaction = em.getTransaction();
		boolean success = false;

		try {
			em.getTransaction().begin();
			em.persist(product);
			em.getTransaction().commit();
			success = true;
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			success = false;
			e.printStackTrace();
		} finally {
			em.close();
		}

		return success;
	}

	public boolean updateProduct(Product updatedProduct) {

		EntityTransaction transaction = em.getTransaction();
		boolean success = false;

		try {
			transaction.begin();

			Product existingProduct = em.find(Product.class, updatedProduct.getId());

			if (existingProduct != null) {
				existingProduct.setName(updatedProduct.getName());
				existingProduct.setImage(updatedProduct.getImage());
				existingProduct.setDescription(updatedProduct.getDescription());
				em.merge(existingProduct);
				transaction.commit();
				success = true;
			} else {
				transaction.rollback();
			}
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {

			em.close();
		}

		return success;
	}

	
	public boolean deleteProduct(Product product) {
		EntityTransaction transaction = em.getTransaction();
		boolean success = false;

		try {
			transaction.begin();

			Product existingProduct = em.find(Product.class, product.getId());

			if (existingProduct != null) {
				em.remove(existingProduct);
				transaction.commit();
				success = true;
			} else {
				transaction.rollback();
			}
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {

			//em.close();
		}

		return success;
	}

	public List<Product> getAllProducts() {
		return em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
	}

	// Outros métodos como buscar por ID, etc.
}
