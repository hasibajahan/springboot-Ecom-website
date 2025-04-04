package com.ecommerce.project.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Data
@Table(name="users",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "username"),
				@UniqueConstraint(columnNames = "email")
		})
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long userId;
	
	@NotBlank
	@Size(max=20)
	@Column(name="username")
	private String userName;
	
	@NotBlank
	@Size(max=50)
	@Email
	@Column(name="email")
	private String email;
	
	@NotBlank
	@Size(max=120)
	@Column(name="password")
	private String password;

	public User(String userName, String email, String password) {
		this.userName = userName;
		this.email = email;
		this.password = password;
	}
	
	//Managing user roles
	//implementing the relationships that we want to have with the users
	@Getter
	@Setter 
	@ManyToMany(cascade= {CascadeType.PERSIST,CascadeType.MERGE},
			fetch = FetchType.EAGER)
	@JoinTable(name="user_role",
				joinColumns = @JoinColumn(name="user_id"),
				inverseJoinColumns = @JoinColumn(name="role_id"))
	private Set<Role> roles=new HashSet<>();
	
	//Managing addresses
	@Getter
	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST,CascadeType.MERGE},orphanRemoval = true)
//	@JoinTable(name="user_address",
//			joinColumns = @JoinColumn(name="user_id"),
//			inverseJoinColumns = @JoinColumn(name="address_id"))
	private List<Address> addresses=new ArrayList<>();
	
	//mapping products to users(sellers)
	@ToString.Exclude
	@OneToMany(mappedBy = "user",
			cascade = {CascadeType.PERSIST,CascadeType.MERGE},
			orphanRemoval = true)
	private Set<Product> products;
	
	//Mapping cart to users
	@ToString.Exclude
	@OneToOne(mappedBy="user",
			cascade = {CascadeType.PERSIST,CascadeType.MERGE},
			orphanRemoval = true)
	private Cart cart;
	
}
