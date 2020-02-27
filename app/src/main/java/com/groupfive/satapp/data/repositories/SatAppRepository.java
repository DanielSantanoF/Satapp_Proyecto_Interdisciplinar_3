package com.groupfive.satapp.data.repositories;

import com.groupfive.satapp.retrofit.SatAppService;
import com.groupfive.satapp.retrofit.SatAppServiceGenerator;

public class SatAppRepository {

    SatAppService service;
    SatAppServiceGenerator serviceGenerator;

    public SatAppRepository() {
        service = serviceGenerator.createService(SatAppService.class);
    }
}
