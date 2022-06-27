package com.ensa.ENSAPAY.services;

import com.ensa.ENSAPAY.entities.Bill;
import com.ensa.ENSAPAY.entities.BillState;
import com.ensa.ENSAPAY.entities.Client;
import com.ensa.ENSAPAY.repositories.BillRepository;
import com.ensa.ENSAPAY.repositories.ClientRepository;
import com.ensa.ENSAPAY.security.PasswordGenerator;
import com.ensa.ENSAPAY.security.auth.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.ChronoUnit;

@Service
public class CMIService
{
    private final ClientRepository clientRepository;
    private final BillRepository billRepository;
    private final SmsService smsService;

    private static final int VERIFICATION_CODE_EXPIRATION_TIME = 5;
    @Autowired
    public CMIService(ClientRepository clientRepository, BillRepository billRepository, SmsService smsService)
    {
        this.clientRepository = clientRepository;
        this.billRepository = billRepository;
        this.smsService = smsService;
    }

    // Methode pour ajouter du solde au balance dial client
    public void payment(String username, BigDecimal paymentAmount)
    {
        if(paymentAmount == null)
            throw new IllegalStateException("Cannot deposit null amount");

        Client client = clientRepository.findClientByUsername(username).get();

        BigDecimal clientNewBalance = client.getBalance().add(paymentAmount);

        clientRepository.changeClientBalanceById(clientNewBalance, client.getId());
    }

    //Methode pour payer une facture
    public void payBill(Long billId)
    {
        String authClient = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientRepository.findClientByUsername(authClient).get();
        Bill bill = billRepository.findById(billId).get();

        if(client.getBalance().compareTo( bill.getTotalAmount()) == -1)
            throw new IllegalStateException("Client do not have enough balance");

        System.out.println(bill.getState());

        if(bill.getState() != BillState.paid)
        {
            String verificationCode = PasswordGenerator.NumericString();
            String smsMsg = GenerateSms(verificationCode,bill);
            smsService.sendSMS(client.getPhone(),smsMsg);
            billRepository.setVerificationCode(verificationCode,bill.getId());

/*            clientRepository.changeClientBalanceById(clientNewBalance, client.getId());
            billRepository.changeBillStatus(true,billId);*/

            System.out.println("passed");
        }
        else
            throw new IllegalStateException("Bill already paid");

    }
    //Methode pour confirmer le paiement d'une facture
    public void confirmPayment(Long billId,String verificationCode)
    {
        ApplicationUser authClient = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientRepository.findClientByUsername(authClient.getUsername()).get();
        Bill bill = billRepository.findById(billId).get();
        if(bill.getState() != BillState.pending)
        {
            throw new IllegalStateException("Bill already paid");
        }
        else
        {
            int minutes = (int) ChronoUnit.MINUTES.between(LocalDateTime.now(), bill.getUpdatedAt());
            if(minutes < VERIFICATION_CODE_EXPIRATION_TIME)
            {
                if(bill.getVerificationCode().equals(verificationCode))
                {
                    BigDecimal clientNewBalance = client.getBalance().subtract(bill.getTotalAmount());
                    clientRepository.changeClientBalanceById(clientNewBalance, client.getId());
                    billRepository.changeBillStatus(BillState.paid,verificationCode,billId);
                }
                else
                {
                    throw new IllegalStateException("Incorrect Verification");
                }
            }
        }

    }
    private String GenerateSms(String verificationCode, Bill bill){
        return verificationCode+ " est le code de confirmation pour votre payment pour " +
                bill.getCreditor().getTitle()+ " pour un montant de "+ bill.getTotalAmount()+
                " MAD.\n Ce code expire dans "+VERIFICATION_CODE_EXPIRATION_TIME+" minutes.";
    }
}
