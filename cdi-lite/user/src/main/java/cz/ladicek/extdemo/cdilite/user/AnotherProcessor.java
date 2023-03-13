package cz.ladicek.extdemo.cdilite.user;

import cz.ladicek.extdemo.framework.Processor;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.logging.Logger;

@ApplicationScoped
public class AnotherProcessor implements Processor {
    private static final Logger log = Logger.getLogger(AnotherProcessor.class.getName());

    @Override
    public void doWork() {
        log.info("Working barely enough");
    }
}
