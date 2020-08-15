package curso.api.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique = true)
	private String login;

	private String token ="";
	
	private String Cpf;
	
	private String senha;

	private String nome;

	@OneToMany(mappedBy = "usuario", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Telefone> telefones = new ArrayList<Telefone>();

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuarios_role", uniqueConstraints = @UniqueConstraint(columnNames = { "usuario_id", "role_id" }, name = "unique_role_user"),
	joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName ="id", table = "usuario", unique = false, foreignKey = @ForeignKey(name = "usuario_fk", value = ConstraintMode.CONSTRAINT)),
	inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", table = "role", unique = false, updatable = false,
	foreignKey = @ForeignKey(name ="role_fk", value = ConstraintMode.CONSTRAINT)))
	private List<Role> roles;/* os papeis ou acessos */

	
	public String getCpf() {
		return Cpf;
	}
	
	public void setCpf(String cpf) {
		Cpf = cpf;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/*
	 * Sao as autorizaçoes, na qual dar acesso ao usuario exemplo: ROLE_ADMIN,
	 * ROLE_VISITANTE e etc
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}
    @JsonIgnore // vai ignora os dados na hora de consultar um usuário
	@Override
	public String getPassword() {
		return this.senha;
	}
    
    @JsonIgnore // vai ignora os dados na hora de consultar um usuário
	@Override
	public String getUsername() {
		return this.login;
	}
    
    @JsonIgnore // vai ignora os dados na hora de consultar um usuário
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
    @JsonIgnore // vai ignora os dados na hora de consultar um usuário
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
    @JsonIgnore // vai ignora os dados na hora de consultar um usuário
	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}
    @JsonIgnore // vai ignora os dados na hora de consultar um usuário
	@Override
	public boolean isEnabled() {
		return true;
	}

}
