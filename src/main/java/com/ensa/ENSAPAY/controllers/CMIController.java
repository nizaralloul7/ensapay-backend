package com.ensa.ENSAPAY.controllers;

import com.ensa.ENSAPAY.entities.Bill;
import com.ensa.ENSAPAY.services.CMIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/api")
public class CMIController
{
    private final CMIService cmiService;

    @Autowired
    public CMIController(CMIService cmiService)
    {
        this.cmiService = cmiService;
    }

    @PostMapping("/payment")
    @PreAuthorize("hasAuthority('client:write')")
    public void payment(@RequestParam String username, @RequestParam BigDecimal paymentAmount)
    {
        cmiService.payment(username,paymentAmount);
    }

    @PostMapping("/bills/{billId}/verify")
    public Bill payBill(@PathVariable Long billId, @RequestBody String verificationCode)
    {
        return cmiService.confirmPayment(billId,verificationCode);
    }

}
