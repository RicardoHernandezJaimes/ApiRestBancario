package com.example.demo.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.AccountModel;

@Repository
public interface IAccountRepository extends JpaRepository<AccountModel, Long> {
	
	AccountModel  findByNumberAccount(String numberAccount);


}
