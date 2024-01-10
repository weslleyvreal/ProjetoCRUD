package br.com.dominio.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dominio.dao.ProductDAO;
import br.com.dominio.model.Product;

@Named("productBean")
@RequestScoped
public class ProductBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long productId;

	@Inject
	private ProductDAO productDAO = new ProductDAO();
	private Product product = new Product();

	@PostConstruct
	public void init() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		String idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");		
		String direct = facesContext.getExternalContext().getRequestParameterMap().get("param");
		
		
		
		if (idParam != null) {
			productId = Long.valueOf(idParam);
			product = productDAO.findProductById(productId);

			if (direct != null) {
				deleteProduct(product);
			}

		}

	}

	private List<Product> products;

	public List<Product> getProducts() {
		if (products == null) {
			products = productDAO.getAllProducts();
		}
		return products;
	}

	public ProductBean() {
	}

	public void addProduct() {

		boolean create = productDAO.addProduct(product);

		if (create) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Produto atualizado com sucesso!", null));
		} else {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao atualizar o produto.", null));
		}

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/meu-projeto/paginas/product-list.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
		}

		product = new Product();
	}

	public void updateProduct() {

		boolean updated = productDAO.updateProduct(product);

		if (updated) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Produto atualizado com sucesso!", null));
		} else {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao atualizar o produto.", null));
		}

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/meu-projeto/paginas/product-list.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteProduct(Product product) {
		boolean deleted = productDAO.deleteProduct(product);

		if (deleted) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Produto excluído com sucesso!", null));
		} else {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao excluir o produto.", null));
		}

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/meu-projeto/paginas/product-list.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ProductDAO getProductDAO() {
		return productDAO;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
