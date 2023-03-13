package cz.ladicek.extdemo.framework;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.util.logging.Level;
import java.util.logging.Logger;

@Logged
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor {
    @Inject
    Importance importance;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        Class<?> clazz = ctx.getMethod().getDeclaringClass();
        Logger log = Logger.getLogger(clazz.getName());
        Level level = importance.isImportant(clazz) ? Level.WARNING : Level.INFO;
        try {
            log.log(level, "Starting work");
            return ctx.proceed();
        } finally {
            log.log(level, "Work finished");
        }
    }
}
