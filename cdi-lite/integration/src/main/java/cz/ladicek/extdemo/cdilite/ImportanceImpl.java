package cz.ladicek.extdemo.cdilite;

import cz.ladicek.extdemo.framework.Importance;

import java.util.Set;

public class ImportanceImpl implements Importance {
    private final Set<String> importantClasses;

    ImportanceImpl(Set<String> importantClasses) {
        this.importantClasses = importantClasses;
    }

    @Override
    public boolean isImportant(Class<?> clazz) {
        return importantClasses.contains(clazz.getName());
    }
}
