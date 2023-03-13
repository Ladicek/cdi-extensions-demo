package cz.ladicek.extdemo.quarkus.user;

import cz.ladicek.extdemo.framework.Important;
import cz.ladicek.extdemo.framework.Processor;
import jakarta.inject.Singleton;

import java.util.logging.Logger;

@Singleton
@Important
public class MyProcessor implements Processor {
    private static final Logger log = Logger.getLogger(MyProcessor.class.getName());

    @Override
    public void doWork() {
        log.info("Working really hard");
    }
}
