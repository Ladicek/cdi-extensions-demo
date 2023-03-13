package cz.ladicek.extdemo.quarkus;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

import java.util.Set;

@Recorder
public class FrameworkRecorder {
    public RuntimeValue<ImportanceImpl> createImportance(String[] importantProcessors) {
        return new RuntimeValue<>(new ImportanceImpl(Set.of(importantProcessors)));
    }
}
