package com.example.demo.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.models.AccountModel;
import com.example.demo.models.TransationsModel;
import com.example.demo.repository.IAccountRepository;
import com.example.demo.repository.ITransationsRepository;

@Service
public class TransactionsServices {
	
	@Autowired
	ITransationsRepository transationsRepository;
	
	@Autowired
	IAccountRepository  accountRepository;
	
	public ResponseEntity<String> compra( String numberAccount, float valueShop) {
		 
		LocalDateTime fecha = LocalDateTime.now();
		 int aleatorio = operaciones.generarNumeroAleatorio();
		 String idTransation =  String.valueOf(aleatorio);
		 
		AccountModel optionalCard = accountRepository.findByNumberAccount(numberAccount);
        if (optionalCard != null) {
            if (optionalCard.isStatus()) {
            	TransationsModel transation = new TransationsModel();
       		 	transation.setIdTransation(idTransation);
       		 	transation.setValueShop(valueShop);
       		 	transation.setStatus(true);
       		 	transation.setDate(fecha);
       		 	transation.setAccount(optionalCard);
       		 	transationsRepository.save(transation);
       		 	
	   		 	if (optionalCard != null && optionalCard.isStatus() != false) {
		   		   optionalCard.setValueAccount(optionalCard.getValueAccount() - valueShop);
		   	       accountRepository.save(optionalCard);
		
	             }
                
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.badRequest().body("La tarjeta No existe o esta bloqueada");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
		
		    
    }
	
	public ResponseEntity<String> consultaTransacciones( String transactionId, float valueShop) {
		 
		TransationsModel transacciones = transationsRepository.findByIdTransation(transactionId);
       
       return ResponseEntity.status(HttpStatus.CREATED).body("Número de transaccion: " + transacciones.getIdTransation() + ", Valor de comprar: " + transacciones.getValueShop());
		    
   }
	
	public ResponseEntity<String> anularTransacciones( String numberAccount, String idTransation) {
		 
	   AccountModel optionalCard = accountRepository.findByNumberAccount(numberAccount);
	   TransationsModel transacciones = transationsRepository.findByIdTransation(idTransation);
	   LocalDateTime fechaActual = LocalDateTime.now();
	   long diferenciaHoras = ChronoUnit.HOURS.between(transacciones.getDate(), fechaActual);
       if (diferenciaHoras < 24) {
   	 	if (optionalCard != null && optionalCard.isStatus() != false && transacciones.isStatus() != false) {
   		   optionalCard.setValueAccount(optionalCard.getValueAccount() + transacciones.getValueShop());
   	       accountRepository.save(optionalCard);
   	       transacciones.setStatus(false);
   	       transationsRepository.save(transacciones);
   	       return ResponseEntity.status(HttpStatus.CREATED).body("Numero de Cuenta : " + optionalCard.getNumberAccount() + ", Numero Transaccion: " + transacciones.getIdTransation());
          }else {
        	  return ResponseEntity.badRequest().body("La transsacion ya fue anulada");
          }
        	 
       } else {
           return ResponseEntity.notFound().build();
       }
		
		    
   }
	 

}
