package cz.ladicek.extdemo.quarkus;

import java.util.Set;

import cz.ladicek.extdemo.framework.Importance;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class FrameworkRecorder {

    public Importance createImportance(Set<String> importantProcessors) {
        return new ImportanceImpl(Set.copyOf(importantProcessors));
    }
}
