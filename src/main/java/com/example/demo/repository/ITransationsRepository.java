package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.TransationsModel;
import com.example.demo.models.AccountModel;



@Repository
public interface ITransationsRepository extends JpaRepository<TransationsModel, Long> {
	
	TransationsModel findByIdTransation(String idTransation);
	TransationsModel findByAccount(AccountModel account);


}
