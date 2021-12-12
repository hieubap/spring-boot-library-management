package vn.isofh.userdetailservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import vn.isofh.jpa.entity.Card;

import javax.persistence.*;

@Entity
@Table(name =  "account")
public class Account {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;

	@Column(name = "id_role")
	private Long idRole;
	@Column(name = "id_user")
	private Long idUser;
	@Column(name = "id_card")
	private Long idCard;

	@OneToOne
	@JoinColumn(name = "id_user",insertable = false,updatable = false)
	private Information userInformation;

	@ManyToOne
	@JoinColumn(name = "id_role",updatable = false,insertable = false)
	private Role roleInformation;

	@OneToOne
	@JoinColumn(name = "id_card",insertable = false,updatable = false)
	private Card cardInformation;

	public Account() {
	}

	public Account(String username, String password,Long id) {
		this.username = username;
		this.password = password;
		this.idRole = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getIdRole() {
		return idRole;
	}

	public void setIdRole(Long idRole) {
		this.idRole = idRole;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public Long getIdCard() {
		return idCard;
	}

	public void setIdCard(Long idCard) {
		this.idCard = idCard;
	}

	@JsonManagedReference
	public Information getUserInformation() {
		return userInformation;
	}

	public void setUserInformation(Information userInformation) {
		this.userInformation = userInformation;
	}

	@JsonManagedReference
	public Role getRoleInformation() {
		return roleInformation;
	}

	public void setRoleInformation(Role roleInformation) {
		this.roleInformation = roleInformation;
	}

	@JsonManagedReference
	public Card getCardInformation() {
		return cardInformation;
	}

	public void setCardInformation(Card cardInformation) {
		this.cardInformation = cardInformation;
	}
}
