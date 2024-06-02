package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.models.AccountModel;
import com.example.demo.models.TransationsModel;
import com.example.demo.repository.IAccountRepository;
import com.example.demo.repository.ITransationsRepository;
import com.example.demo.services.AccountServices;
import com.example.demo.services.TransactionsServices;

public class AccountServicesTest {

    @Mock
    private IAccountRepository accountRepository;

    @InjectMocks
    private AccountServices accountServices;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGeneracionTarjeta() {
        AccountModel accountModel = new AccountModel();
        when(accountRepository.save(accountModel)).thenReturn(accountModel);

        AccountModel generatedAccount = accountServices.generacionTarjeta(123);

        assertEquals("Invitado", generatedAccount.getFullname());
        assertEquals(true, generatedAccount.isStatus());
    }

    @Test
    public void testActivacionTarjeta() {
        AccountModel accountModel = new AccountModel();
        accountModel.setStatus(false);

        when(accountRepository.save(accountModel)).thenReturn(accountModel);
        ResponseEntity<String> response = accountServices.ActivacionTarjeta(accountModel);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(true, accountModel.isStatus());
    }

    @Test
    public void testBloquearTarjeta() {
        AccountModel accountModel = new AccountModel();
        accountModel.setStatus(true);

        when(accountRepository.findByNumberAccount("123")).thenReturn(accountModel);
        ResponseEntity<String> response = accountServices.BloquearTarjeta("123");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(false, accountModel.isStatus());
    }

    @Test
    public void testRecargarTarjeta() {
        AccountModel accountModel = new AccountModel();
        accountModel.setStatus(true);

        when(accountRepository.findByNumberAccount("123")).thenReturn(accountModel);
        ResponseEntity<String> response = accountServices.RecargarTarjeta("123", 100.0f);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(100.0f, accountModel.getValueAccount(), 0.001);
    }

    @Test
    public void testConsultarTarjeta() {
        AccountModel accountModel = new AccountModel();
        accountModel.setStatus(true);
        accountModel.setValueAccount(50.0f);

        when(accountRepository.findByNumberAccount("123")).thenReturn(accountModel);
        ResponseEntity<String> response = accountServices.consultarTarjeta("123");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Número de cuenta: 123, Valor de cuenta: 50.0", response.getBody());
    }
    
    
    @Mock
    private ITransationsRepository transationsRepository;


    @InjectMocks
    private TransactionsServices transactionsServices;


    @Test
    public void testCompra() {
        AccountModel accountModel = new AccountModel();
        accountModel.setStatus(true);
        accountModel.setValueAccount(100.0f);
        when(accountRepository.findByNumberAccount("123")).thenReturn(accountModel);

        TransationsModel transation = new TransationsModel();
        when(transationsRepository.save(any())).thenReturn(transation);

        ResponseEntity<String> response = transactionsServices.compra("123", 50.0f);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(50.0f, accountModel.getValueAccount(), 0.001);
    }

    @Test
    public void testConsultaTransacciones() {
        AccountModel accountModel = new AccountModel();
        accountModel.setStatus(true);
        accountModel.setValueAccount(100.0f);
        when(accountRepository.findByNumberAccount("123")).thenReturn(accountModel);

        TransationsModel transationsModel = new TransationsModel();
        transationsModel.setIdTransation("1");
        transationsModel.setValueShop(50.0f);
        transationsModel.setStatus(true);
        when(transationsRepository.findByAccount(accountModel)).thenReturn(transationsModel);

        ResponseEntity<String> response = transactionsServices.consultaTransacciones("123", 50.0f);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Número de transaccion: 1, Valor de comprar: 50.0", response.getBody());
    }

    @Test
    public void testAnularTransacciones() {
        AccountModel accountModel = new AccountModel();
        accountModel.setStatus(true);
        accountModel.setValueAccount(100.0f);
        when(accountRepository.findByNumberAccount("123")).thenReturn(accountModel);

        TransationsModel transationsModel = new TransationsModel();
        transationsModel.setIdTransation("1");
        transationsModel.setValueShop(50.0f);
        transationsModel.setStatus(true);
        LocalDateTime fechaHora = LocalDateTime.now();
        transationsModel.setDate(fechaHora);
        when(transationsRepository.findByIdTransation("1")).thenReturn(transationsModel);

        ResponseEntity<String> response = transactionsServices.anularTransacciones("123", "1");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Numero de Cuenta : 123, Numero Transaccion: 1", response.getBody());
        assertEquals(150.0f, accountModel.getValueAccount(), 0.001);
        assertEquals(false, transationsModel.isStatus());
    }
   
}
