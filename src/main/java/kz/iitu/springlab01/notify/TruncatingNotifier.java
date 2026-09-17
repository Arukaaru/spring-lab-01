package kz.iitu.springlab01.notify;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("truncating")
@Order(3)
public class TruncatingNotifier implements Notifier {

    private static final Logger log =
            LoggerFactory.getLogger(TruncatingNotifier.class);

    @PostConstruct
    void init() {
        log.info("TRUNCATING notifier initialized");
    }

    @Override
    public String send(String message) {
        if (message.length() <= 20) {
            return message;
        }

        return message.substring(0, 20) + "...";
    }

    @Override
    public String channel() {
        return "truncating";
    }
}