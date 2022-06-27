package com.ensa.ENSAPAY.services;

import com.ensa.ENSAPAY.entities.*;
import com.ensa.ENSAPAY.repositories.BillRepository;
import com.ensa.ENSAPAY.repositories.ClientRepository;
import com.ensa.ENSAPAY.repositories.UnpaidRepository;
import com.ensa.ENSAPAY.security.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class CMIService
{
    private final ClientRepository clientRepository;
    private final BillRepository billRepository;
    private final SmsService smsService;
    private final UnpaidRepository unpaidRepository;

    private static final int VERIFICATION_CODE_EXPIRATION_TIME = 5;
    @Autowired
    public CMIService(ClientRepository clientRepository, BillRepository billRepository, SmsService smsService, UnpaidRepository unpaidRepository)
    {
        this.clientRepository = clientRepository;
        this.billRepository = billRepository;
        this.smsService = smsService;
        this.unpaidRepository = unpaidRepository;
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
    public String payBill(Bill bill)
    {
        String authClient = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientRepository.findClientByUsername(authClient).get();

        if(client.getBalance().compareTo( bill.getTotalAmount()) == -1)
            throw new IllegalStateException("Client do not have enough balance");

        System.out.println(bill.getState());

        if(bill.getState() != BillState.paid)
        {
            String verificationCode = PasswordGenerator.NumericString();
            String smsMsg = GenerateSms(verificationCode,bill);
            smsService.sendSMS(client.getPhone(),smsMsg);
            System.out.println(verificationCode);
            System.out.println("passed");
            return verificationCode;
            //billRepository.setVerificationCode(verificationCode,bill.getId());

/*            clientRepository.changeClientBalanceById(clientNewBalance, client.getId());
            billRepository.changeBillStatus(true,billId);*/


        }
        return null;
    }
    //Methode pour confirmer le paiement d'une facture
    public Bill confirmPayment(Long billId,String verificationCode)
    {
        String authClient = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientRepository.findClientByUsername(authClient).get();
        Bill bill = billRepository.findById(billId).get();
        List<Long> unpaidIds = new ArrayList<>();
        if(bill.getState() != BillState.pending)
        {
            throw new IllegalStateException("Bill already paid");
        }
        else
        {
            for(Unpaid unpaid : bill.getUnpaidList()){
                if(unpaid.getState() == UnpaidState.PAID){
                    throw new IllegalStateException("Bill with invalid unpaid");
                }
                else{
                    unpaidIds.add(unpaid.getId());
                }
            }
            LocalDateTime to;
            if(bill.getUpdatedAt() != null){
                to = bill.getUpdatedAt();
            }
            else{
                to = bill.getCreatedAt();
            }
            int minutes = (int) ChronoUnit.MINUTES.between(to, LocalDateTime.now());
            System.out.println(minutes);
            if(minutes < VERIFICATION_CODE_EXPIRATION_TIME)
            {
                if(bill.getVerificationCode() != null && bill.getVerificationCode().equals(verificationCode))
                {
                    BigDecimal clientNewBalance = client.getBalance().subtract(bill.getTotalAmount());
                    clientRepository.changeClientBalanceById(clientNewBalance, client.getId());
                    billRepository.changeBillStatus(BillState.paid.ordinal(),billId);
                    unpaidRepository.setUnpaidState(UnpaidState.PAID.ordinal(),unpaidIds);
                }
                else
                {
                    throw new IllegalStateException("Incorrect Verification code");
                }
            }
            else{
                throw new IllegalStateException("Verification code expired");
            }
        }
        return bill;
    }
    private String GenerateSms(String verificationCode, Bill bill){
        return verificationCode+ " est le code de confirmation pour votre payment pour " +
                bill.getCreditor().getTitle()+ " il s'agit d'un montant de "+ bill.getTotalAmount()+
                " MAD.\n" +
                "Ce code expire dans "+VERIFICATION_CODE_EXPIRATION_TIME+" minutes.";
    }
}
