package com.bczyzowski.service;

import io.micronaut.cache.annotation.CacheConfig;
import io.micronaut.cache.annotation.CacheInvalidate;
import io.micronaut.cache.annotation.CachePut;
import io.micronaut.cache.annotation.Cacheable;

import javax.inject.Singleton;
import java.time.Month;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Singleton
@CacheConfig("headlines")
public class NewsService {

    private static final Map<Month, List<String>> HEADLINES = new HashMap<>() {{
        put(Month.NOVEMBER, Arrays.asList("Micronaut Graduates to Trial Level in Thoughtworks technology radar Vol.1",
                "Micronaut AOP: Awesome flexibility without the complexity"));
        put(Month.OCTOBER, Collections.singletonList("Micronaut AOP: Awesome flexibility without the complexity"));
    }};

    @Cacheable
    public List<String> headlines(Month month) {
        try {
            TimeUnit.SECONDS.sleep(3);
            return HEADLINES.get(month);
        } catch (InterruptedException e) {
            return null;
        }
    }

    @CachePut(parameters = {"month"})
    public List<String> addHeadline(Month month, String headline) {
        if (HEADLINES.containsKey(month)) {
            List<String> l = new ArrayList<>(HEADLINES.get(month));
            l.add(headline);
            HEADLINES.put(month, l);
        } else {
            HEADLINES.put(month, Arrays.asList(headline));
        }
        return HEADLINES.get(month);
    }

    @CacheInvalidate(parameters = {"month"})
    public void removeHeadline(Month month, String headline) {
        if (HEADLINES.containsKey(month)) {
            List<String> l = new ArrayList<>(HEADLINES.get(month));
            l.remove(headline);
            HEADLINES.put(month, l);
        }
    }
}