package kz.iitu.springlab01.service;

import kz.iitu.springlab01.audit.Audited;
import kz.iitu.springlab01.cache.SimpleCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class CatalogService {

    private static final Logger log =
            LoggerFactory.getLogger(CatalogService.class);

    @Autowired
    @Lazy
    private CatalogService self;

    @SimpleCache
    public String findById(long id) {

        log.info("[SERVICE] findById target executed for id={}", id);

        sleep(50);

        return "Item no. " + id;
    }

    @Audited(action = "CATALOG_LIST", logArguments = true)
    public List<String> findAll(int limit) {

        sleep(300);

        return IntStream.rangeClosed(1, limit)
                .mapToObj(i -> "Item no. " + i)
                .toList();
    }

    @Audited(action = "CATALOG_REMOVE")
    public String remove(long id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid identifier: " + id
            );
        }

        return "Removed item no. " + id;
    }

    public String removeTwice(long id) {

        String first = self.remove(id);
        String second = self.remove(id + 1);

        return first + "; " + second;
    }

    private void sleep(long ms) {

        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}