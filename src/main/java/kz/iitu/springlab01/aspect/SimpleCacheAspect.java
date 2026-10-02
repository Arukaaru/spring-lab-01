package kz.iitu.springlab01.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@Order(4)
public class SimpleCacheAspect {

    private static final Logger log =
            LoggerFactory.getLogger(SimpleCacheAspect.class);

    private final Map<String, Object> cache =
            new ConcurrentHashMap<>();

    @Around("@annotation(kz.iitu.springlab01.cache.SimpleCache)")
    public Object cacheResult(ProceedingJoinPoint pjp) throws Throwable {

        String key =
                pjp.getSignature().toLongString()
                        + Arrays.deepToString(pjp.getArgs());

        if (cache.containsKey(key)) {
            log.info("[CACHE] HIT {}", key);
            return cache.get(key);
        }

        log.info("[CACHE] MISS {}", key);

        Object result = pjp.proceed();

        cache.put(key, result);

        return result;
    }
}