package com.ensa.ENSAPAY.controllers;

import com.ensa.ENSAPAY.entities.Admin;
import com.ensa.ENSAPAY.entities.Bill;
import com.ensa.ENSAPAY.services.BillService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class BillController {
    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping(value = "/bill/{id}")

    public Bill getBill(@PathVariable Long id)
    {
        return this.billService.getBill(id);
    }
}
