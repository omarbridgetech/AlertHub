package com.mst.service;

import com.mst.model.Provider;

import java.util.Map;

public interface LoaderService {

    int scanAndLoadProvider(Provider provider);

    Map<Provider, Integer> scanAndLoadAllProviders();

    int triggerManualScan(Provider provider);

    Map<Provider, Integer> triggerManualScanAll();
}
