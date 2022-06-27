package com.ensa.ENSAPAY.controllers;

import com.ensa.ENSAPAY.entities.Unpaid;
import com.ensa.ENSAPAY.services.UnpaidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/unpaids")
public class UnpaidController
{
    private final UnpaidService unpaidService;

    @Autowired
    public UnpaidController(UnpaidService unpaidService)
    {
        this.unpaidService = unpaidService;
    }

    @GetMapping
    public List<Unpaid> getUnpaids()
    {
        return this.unpaidService.getUnpaids();
    }

    @PostMapping
    public void addUnpaid(@RequestBody Unpaid unpaid)
    {
        unpaidService.addUnpaid(unpaid);
    }


}
