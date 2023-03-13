package cz.ladicek.extdemo.cdilite;

import cz.ladicek.extdemo.framework.Importance;
import cz.ladicek.extdemo.framework.Important;
import cz.ladicek.extdemo.framework.Logged;
import cz.ladicek.extdemo.framework.LoggingInterceptor;
import cz.ladicek.extdemo.framework.Processor;
import cz.ladicek.extdemo.framework.ProcessorManager;
import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.Messages;
import jakarta.enterprise.inject.build.compatible.spi.Registration;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import jakarta.enterprise.inject.build.compatible.spi.Validation;
import jakarta.enterprise.lang.model.declarations.ClassInfo;

import java.util.HashSet;
import java.util.Set;

public class FrameworkExtension implements BuildCompatibleExtension {
    private final Set<ClassInfo> processors = new HashSet<>();

    @Discovery
    public void discoverFrameworkClasses(ScannedClasses scan) {
        scan.add(LoggingInterceptor.class.getName());
        scan.add(ProcessorManager.class.getName());
    }

    @Enhancement(types = Processor.class, withSubtypes = true)
    public void addInterceptorBindingToProcessors(ClassConfig clazz) {
        clazz.methods()
                .stream()
                .filter(it -> it.info().name().equals("doWork") && it.info().parameters().isEmpty())
                .forEach(it -> it.addAnnotation(Logged.class));
    }

    @Registration(types = Processor.class)
    public void rememberProcessors(BeanInfo bean) {
        if (bean.isClassBean()) {
            processors.add(bean.declaringClass());
        }
    }

    @Synthesis
    public void registerImportanceImpl(SyntheticComponents synth) {
        String[] importantProcessors = processors.stream()
                .filter(it -> it.hasAnnotation(Important.class))
                .map(ClassInfo::name)
                .toArray(String[]::new);

        synth.addBean(ImportanceImpl.class)
                .type(Importance.class)
                .withParam("importantProcessors", importantProcessors)
                .createWith(ImportanceImplCreator.class);
    }

    @Validation
    public void validateProcessors(Messages msg) {
        if (processors.isEmpty()) {
            msg.error("At least one `Processor` implementation must exist");
        }
    }
}
