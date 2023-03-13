package cz.ladicek.extdemo.framework;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@ApplicationScoped
public class ProcessorManager {
    private final Collection<Processor> processors;

    @Inject
    public ProcessorManager(Instance<Processor> processors) {
        Collection<Processor> result = new ArrayList<>();
        for (Processor processor : processors) {
            result.add(processor);
        }
        this.processors = Collections.unmodifiableCollection(result);
    }

    public Collection<Processor> all() {
        return processors;
    }
}
