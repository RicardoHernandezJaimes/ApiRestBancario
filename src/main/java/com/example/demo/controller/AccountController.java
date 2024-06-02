package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.AccountModel;
import com.example.demo.services.AccountServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/card")
public class AccountController {
	
	@Autowired
	AccountServices accountServices;
	
	@GetMapping("/{idProducto}/number")
    public AccountModel generacionTarjeta(@PathVariable int idProducto) {
    
        return accountServices.generacionTarjeta(idProducto);
	}
	
   @PostMapping("/enroll")
   public ResponseEntity<String> ActivacionTarjeta(@RequestBody AccountModel account) {

     return accountServices.ActivacionTarjeta(account);
   }
	   
   @PostMapping("/{numberAccount}")  /// metodo  delete
   public ResponseEntity<String> BloquearTarjeta(@PathVariable String numberAccount) {
     
	  return accountServices.BloquearTarjeta(numberAccount);
   }
	   
   @PostMapping("/balance")
   public ResponseEntity<String> RecargarTarjeta(@RequestParam String numberAccount, @RequestParam float valueAccount) {
	  
	  return accountServices.RecargarTarjeta(numberAccount, valueAccount);
   }
   
	@GetMapping("/balance/{numberAccount}")
    public ResponseEntity<String> consultarTarjeta(@PathVariable String numberAccount) {
		return accountServices.consultarTarjeta(numberAccount);
		
	}
	
	
	
}
