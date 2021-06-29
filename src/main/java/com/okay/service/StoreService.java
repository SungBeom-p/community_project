package com.okay.service;

import com.okay.domain.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class StoreService extends Service{

    @Autowired
    StoreRepository storeRepository;


}
