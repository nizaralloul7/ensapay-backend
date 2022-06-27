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
import java.util.List;

@Service
public class BillService
{
    private final BillRepository billRepository;
    private final CreditorRepository creditorRepository;
    private final ClientRepository clientRepository;
    private final UnpaidRepository unpaidRepository;

    @Autowired
    public BillService(BillRepository billRepository, CreditorRepository creditorRepository, ClientRepository clientRepository, UnpaidRepository unpaidRepository)
    {
        this.billRepository = billRepository;
        this.creditorRepository = creditorRepository;
        this.clientRepository = clientRepository;
        this.unpaidRepository = unpaidRepository;
    }

    public void createBill(Long creditorId,List<Long> unpaidIds)
    {
        String currentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientRepository.findClientByUsername(currentUser).get() ;

        System.out.println(unpaidIds);

        List<Unpaid> unpaidList = unpaidRepository.getUnpaidsForTheCreditorAndClient(client.getId(),creditorId);
        List<Unpaid> billUnpaids = new ArrayList<>();
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

        Bill generatedBill = new Bill();

        generatedBill.setUnpaidList(billUnpaids);
        generatedBill.setClient(client);
        generatedBill.setState(BillState.pending);
        generatedBill.setCreditor(creditorRepository.findById(creditorId).get());

        generatedBill.setTotalAmount(totalAmount);

        billRepository.save(generatedBill);
    }
}
