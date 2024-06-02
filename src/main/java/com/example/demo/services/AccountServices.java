package com.example.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.models.AccountModel;
import com.example.demo.repository.IAccountRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Service
public class AccountServices {
	
	@Autowired
	IAccountRepository accountRepository;
	
	
    public AccountModel generacionTarjeta(int idProducto) {
        Date fechaCreacion = new Date(); // Fecha de creación actual
        
        operaciones operaciones = new operaciones();

        String numeroTarjeta = operaciones.generarNumeroTarjeta(idProducto);
        String fechaVencimiento = operaciones.generarFechaVencimiento(fechaCreacion);
        float valueAccount = 0;
        String fullname = "Invitado";

        // Guardar en la base de datos
        AccountModel account = new AccountModel();
        account.setNumberAccount(numeroTarjeta);
        account.setValueAccount(valueAccount);
        account.setStatus(false);
        account.setFullname(fullname);
        account.setDateF(fechaVencimiento);
        accountRepository.save(account);

        return account;
	}
    
    public ResponseEntity<String> ActivacionTarjeta(AccountModel account) {
        // validamos si la tarjeta esta activa
	   if(account.isStatus()) {
		   return ResponseEntity.accepted().body("La tarjeta ya está activada");
	   }
        account.setStatus(true);
        accountRepository.save(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(account.getNumberAccount());
   }
    
    public ResponseEntity<String> BloquearTarjeta(String numberAccount) {
        AccountModel optionalCard = accountRepository.findByNumberAccount(numberAccount);
        if (optionalCard != null) {
            if (optionalCard.isStatus()) {
                // Lógica para bloquear la tarjeta
                optionalCard.setStatus(false);
                accountRepository.save(optionalCard);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.badRequest().body("La tarjeta ya está bloqueada");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    public ResponseEntity<String> RecargarTarjeta(String numberAccount, float valueAccount) {
 	   AccountModel optionalCard = accountRepository.findByNumberAccount(numberAccount);
        if (optionalCard != null && optionalCard.isStatus() != false) {
     	   optionalCard.setValueAccount(valueAccount);
 	       accountRepository.save(optionalCard);
 	       return ResponseEntity.status(HttpStatus.CREATED).body("Número de cuenta: " + optionalCard.getNumberAccount() + ", Valor de cuenta: " + optionalCard.getValueAccount());

        }
        return ResponseEntity.badRequest().body("No se le puede agregar saldo a la tarjeta");
    }
    
    public ResponseEntity<String> consultarTarjeta(String numberAccount) {
		AccountModel optionalCard = accountRepository.findByNumberAccount(numberAccount);
	       if (optionalCard != null && optionalCard.isStatus() != false) {
	    	  
		       return ResponseEntity.status(HttpStatus.CREATED).body("Número de cuenta: " + optionalCard.getNumberAccount() + ", Valor de cuenta: " + optionalCard.getValueAccount());

	       }
	       return ResponseEntity.badRequest().body("No se puede consultar el saldo");
	}
    

}

class operaciones {
	
	public String generarNumeroTarjeta(int idProducto) {
        Random random = new Random();
        StringBuilder numeroTarjeta = new StringBuilder(String.valueOf(idProducto));

        // Generar 10 dígitos aleatorios
        for (int i = 0; i < 10; i++) {
            numeroTarjeta.append(random.nextInt(10));
        }

        // Calcular el dígito de verificación usando el algoritmo de Luhn
        int suma = 0;
        boolean alt = false;
        for (int i = numeroTarjeta.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(numeroTarjeta.substring(i, i + 1));
            if (alt) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            suma += n;
            alt = !alt;
        }
        int digitoVerificacion = (10 - (suma % 10)) % 10;
        numeroTarjeta.append(digitoVerificacion);

        return numeroTarjeta.toString();
    }

    public String generarFechaVencimiento(Date fechaCreacion) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaCreacion);

        // Sumar 3 años a la fecha de creación
        calendar.add(Calendar.YEAR, 3);

        // Obtener el mes y el año de la fecha de vencimiento
        int mes = calendar.get(Calendar.MONTH) + 1; // Se suma 1 porque los meses en Java van de 0 a 11
        int anio = calendar.get(Calendar.YEAR);

        return String.format("%02d/%d", mes, anio);
    }
    
    public static int generarNumeroAleatorio() {
        Random random = new Random();
        return 10000 + random.nextInt(90000); // Genera un número entre 10000 y 99999
    }
	
}
