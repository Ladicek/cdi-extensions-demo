package cz.ladicek.extdemo.quarkus;

import cz.ladicek.extdemo.framework.Importance;
import io.quarkus.runtime.annotations.Recorder;

import java.util.Set;

@Recorder
public class FrameworkRecorder {
    public Importance createImportance(Set<String> importantProcessors) {
        return new ImportanceImpl(importantProcessors);
    }
}
