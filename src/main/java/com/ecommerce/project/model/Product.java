package com.ecommerce.project.model;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//This class represents a single product

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long productId;
	private String productName;
	private String image;
	private String description;
	private Integer quantity;
	private double price;//example:Rs 1000
	private double discount;//25%
	private double specialPrice;//price-(discount*(1/100)*price) ---> (1000-(1000*0.25))
	
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
}
