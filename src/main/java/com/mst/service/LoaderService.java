package com.mst.service;

import com.mst.model.Provider;

import java.util.Map;

public interface LoaderService {

    int scanAndLoadProvider(Provider provider) throws Exception;

    Map<Provider, Integer> scanAndLoadAllProviders();

    int triggerManualScan(Provider provider) throws Exception;

    Map<Provider, Integer> triggerManualScanAll();
}
