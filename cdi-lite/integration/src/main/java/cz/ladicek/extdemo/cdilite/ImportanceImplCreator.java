package cz.ladicek.extdemo.cdilite;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator;

import java.util.Set;

public class ImportanceImplCreator implements SyntheticBeanCreator<ImportanceImpl> {
    @Override
    public ImportanceImpl create(Instance<Object> lookup, Parameters params) {
        String[] importantProcessors = params.get("importantProcessors", String[].class);
        return new ImportanceImpl(Set.of(importantProcessors));
    }
}
