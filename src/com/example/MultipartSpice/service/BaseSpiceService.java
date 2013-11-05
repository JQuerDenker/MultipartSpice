package com.example.MultipartSpice.service;

import android.app.Application;
import com.octo.android.robospice.GoogleHttpClientSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.googlehttpclient.json.Jackson2ObjectPersisterFactory;

/**
 * Project: MultipartSpice
 * Package: com.example.MultipartSpice
 * User: Nelson Sachse
 */
public class BaseSpiceService extends GoogleHttpClientSpiceService {

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();

        Jackson2ObjectPersisterFactory jacksonObjectPersisterFactory = new Jackson2ObjectPersisterFactory(application);

        cacheManager.addPersister(jacksonObjectPersisterFactory);

        return cacheManager;
    }
}
