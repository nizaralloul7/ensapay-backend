package com.ensa.ENSAPAY.services;


import com.ensa.ENSAPAY.entities.Bill;
import com.ensa.ENSAPAY.entities.BillState;
import com.ensa.ENSAPAY.entities.Client;
import com.ensa.ENSAPAY.entities.Unpaid;
import com.ensa.ENSAPAY.repositories.BillRepository;
import com.ensa.ENSAPAY.repositories.ClientRepository;
import com.ensa.ENSAPAY.repositories.CreditorRepository;
import com.ensa.ENSAPAY.repositories.UnpaidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BillService
{
    private final BillRepository billRepository;
    private final CreditorRepository creditorRepository;
    private final ClientRepository clientRepository;
    private final UnpaidRepository unpaidRepository;
    private final CMIService cmiService;

    @Autowired
    public BillService(BillRepository billRepository, CreditorRepository creditorRepository, ClientRepository clientRepository, UnpaidRepository unpaidRepository, CMIService cmiService)
    {
        this.billRepository = billRepository;
        this.creditorRepository = creditorRepository;
        this.clientRepository = clientRepository;
        this.unpaidRepository = unpaidRepository;
        this.cmiService = cmiService;
    }

    public Long createBill(Long creditorId,List<Long> unpaidIds)
    {
        String currentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientRepository.findClientByUsername(currentUser).get() ;
        Bill bill = generateBill(creditorId,client);
        System.out.println(unpaidIds);

        List<Unpaid> unpaidList = unpaidRepository.getUnpaidsForTheCreditorAndClient(creditorId,client.getId());
        Set<Unpaid> billUnpaids = new HashSet<>();
        BigDecimal totalAmount = BigDecimal.valueOf(0);
        for(Unpaid unpaid : unpaidList)
        {
            Long temp = unpaid.getId();
            if(unpaidIds.contains(temp))
            {
                billUnpaids.add(unpaid);
                totalAmount = totalAmount.add(unpaid.getAmount());

            }
        }

        bill.setUnpaidList(billUnpaids);
        bill.setTotalAmount(totalAmount);
        bill.setVerificationCode(cmiService.payBill(bill));
        Long id = billRepository.save(bill).getId();
        return id;
    }

    public Bill getBill(Long id) {
        return this.billRepository.findById(id).get();
    }

    public Long generateCharity(Long creditorId, BigDecimal amount) {
        String currentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientRepository.findClientByUsername(currentUser).get() ;
        Bill bill = generateBill(creditorId,client);
        bill.setTotalAmount(amount);
        bill.setVerificationCode(cmiService.payBill(bill));
        Long id = billRepository.save(bill).getId();
        return id;
    }

    public Bill generateBill(Long creditorId, Client client){
        Bill generatedBill = new Bill();
        generatedBill.setClient(client);
        generatedBill.setState(BillState.pending);
        generatedBill.setCreditor(creditorRepository.findById(creditorId).get());
        return generatedBill;
    }
}
