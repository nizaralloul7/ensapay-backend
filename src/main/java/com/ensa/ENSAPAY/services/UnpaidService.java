package com.ensa.ENSAPAY.services;

import com.ensa.ENSAPAY.entities.Unpaid;
import com.ensa.ENSAPAY.repositories.UnpaidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UnpaidService
{
    private final UnpaidRepository unpaidRepository;

    @Autowired
    public UnpaidService(UnpaidRepository unpaidRepository)
    {
        this.unpaidRepository = unpaidRepository;
    }


    public List<Unpaid> getUnpaids()
    {
        return unpaidRepository.findAll();
    }

    public void addUnpaid(Unpaid unpaid)
    {
        unpaidRepository.save(unpaid);
    }




}
