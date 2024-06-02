package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.TransactionsServices;

@RestController
@RequestMapping("/transaction")
public class TransactionsController {
	
	@Autowired
	TransactionsServices transactionsServices;
	
	@GetMapping("/purchase")
	 public ResponseEntity<String> compra( @RequestParam String numberAccount, @RequestParam float valueShop) {
		  
		 return transactionsServices.compra(numberAccount, valueShop);
		    
    }
	
	@GetMapping("/{transactionId}")
	 public ResponseEntity<String> consultaTransacciones( @PathVariable String transactionId, @RequestParam float valueShop) {
		  
		 return transactionsServices.consultaTransacciones(transactionId, valueShop);
		    
   }
	
	@PostMapping("/anulation")
	public ResponseEntity<String> anularTransacciones( @RequestParam String numberAccount, @RequestParam String idTransation) {
		return transactionsServices.anularTransacciones(numberAccount, idTransation);
	}
	

}
