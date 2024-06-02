package com.example.demo.models;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "account")
public class AccountModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String numberAccount;
	private float valueAccount;
	private boolean status;
	private String fullname;
	private String dateF;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
	private List<TransationsModel> transations = new ArrayList<>();
	
}
