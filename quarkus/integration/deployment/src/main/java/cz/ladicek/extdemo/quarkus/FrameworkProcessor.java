package cz.ladicek.extdemo.quarkus;

import cz.ladicek.extdemo.framework.Importance;
import cz.ladicek.extdemo.framework.Important;
import cz.ladicek.extdemo.framework.Logged;
import cz.ladicek.extdemo.framework.LoggingInterceptor;
import cz.ladicek.extdemo.framework.Processor;
import cz.ladicek.extdemo.framework.ProcessorManager;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.AnnotationsTransformerBuildItem;
import io.quarkus.arc.deployment.BeanDiscoveryFinishedBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.arc.deployment.TransformedAnnotationsBuildItem;
import io.quarkus.arc.deployment.ValidationPhaseBuildItem;
import io.quarkus.arc.deployment.ValidationPhaseBuildItem.ValidationErrorBuildItem;
import io.quarkus.arc.processor.AnnotationsTransformer;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import jakarta.enterprise.inject.spi.DeploymentException;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.DotName;
import org.jboss.jandex.MethodInfo;

import java.util.Set;
import java.util.stream.Collectors;

public class FrameworkProcessor {
    static final DotName IMPORTANT = DotName.createSimple(Important.class);

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem("framework");
    }

    @BuildStep
    AdditionalBeanBuildItem discoverFrameworkClasses() {
        return AdditionalBeanBuildItem.builder()
                .setUnremovable()
                .addBeanClasses(LoggingInterceptor.class, ProcessorManager.class)
                .build();
    }

    @BuildStep
    AnnotationsTransformerBuildItem addInterceptorBindingToProcessors() {
        return new AnnotationsTransformerBuildItem(new AnnotationsTransformer() {
            @Override
            public boolean appliesTo(AnnotationTarget.Kind kind) {
                return kind == AnnotationTarget.Kind.METHOD;
            }

            @Override
            public void transform(TransformationContext transformationContext) {
                MethodInfo method = transformationContext.getTarget().asMethod();
                if (method.declaringClass().interfaceNames().contains(DotName.createSimple(Processor.class))
                        && method.name().equals("doWork")
                        && method.parametersCount() == 0) {
                    transformationContext.transform().add(AnnotationInstance.builder(Logged.class).build()).done();
                }
            }
        });
    }

    @BuildStep
    @Record(ExecutionTime.STATIC_INIT)
    SyntheticBeanBuildItem registerImportanceImpl(BeanDiscoveryFinishedBuildItem beanDiscovery,
            TransformedAnnotationsBuildItem transformedAnnotations, FrameworkRecorder recorder) {
        Set<String> importantProcessors = beanDiscovery.beanStream()
                .classBeans()
                .withBeanType(Processor.class)
                .filter(it -> transformedAnnotations.hasAnnotation(it.getImplClazz(), IMPORTANT))
                .collect(Collectors.mapping(it -> it.getImplClazz().name().toString(), Collectors.toSet()));

        return SyntheticBeanBuildItem.configure(ImportanceImpl.class)
                .types(Importance.class)
                .runtimeProxy(recorder.createImportance(importantProcessors))
                .done();
    }

    @BuildStep
    void validateProcessors(ValidationPhaseBuildItem validation, BuildProducer<ValidationErrorBuildItem> errors) {
        if (validation.getContext()
                .beans()
                .classBeans()
                .withBeanType(Processor.class)
                .isEmpty()) {
            errors.produce(new ValidationErrorBuildItem(
                    new DeploymentException("At least one `Processor` implementation must exist")));
        }
    }
}
