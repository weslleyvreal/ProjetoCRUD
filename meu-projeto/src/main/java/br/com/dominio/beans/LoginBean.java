package br.com.dominio.beans;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import br.com.dominio.dao.UsuarioDAO;
import br.com.dominio.model.Usuario;

@Named("loginBean")
@RequestScoped
public class LoginBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private UsuarioDAO usuarioDao = new UsuarioDAO();
	private boolean usuarioLogado;
    private String login;
    private String senha;
	
    @PostConstruct
    public void init() {
        this.login = usuario.getLogin();
        this.senha = usuario.getSenha();
        usuarioDao.salvar(new Usuario("Benigno Sales", "benigno", "1"));
        usuarioDao.salvar(new Usuario("Gabriela Sales", "gabriela", "2"));
        usuarioDao.salvar(new Usuario("Letícia Sales", "leticia", "3"));
    }    
    
    
    public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Inject
    Usuario usuario;
    
    public LoginBean() {
    }
    

    public void logIn() throws IOException {
        usuarioLogado = usuarioDao.buscar(usuario.getLogin(), usuario.getSenha());
        if (!usuarioLogado) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário ou Senha Inválidos", "Login Inválido"));           
        } else {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            if (session != null) {
                session.setAttribute("usuario", usuarioLogado);
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect("/meu-projeto/paginas/product-list.xhtml");
        }
		
    }

    public String logOff() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        session.invalidate();
        return "/login?faces-redirect=true";
    }
}
