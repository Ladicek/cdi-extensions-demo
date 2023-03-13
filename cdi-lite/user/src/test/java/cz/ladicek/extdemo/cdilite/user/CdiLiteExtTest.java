package cz.ladicek.extdemo.cdilite.user;

import cz.ladicek.extdemo.framework.Processor;
import cz.ladicek.extdemo.framework.ProcessorManager;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class CdiLiteExtTest {
    @Inject
    ProcessorManager processors;

    @Test
    public void test() {
        for (Processor processor : processors.all()) {
            processor.doWork();
        }
    }
}
