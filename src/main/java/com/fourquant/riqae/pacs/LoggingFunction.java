package com.fourquant.riqae.pacs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class LoggingFunction {

  public Float execute(final Method method, final Object[] args)
        throws LoggingFunctionException {

    final Class<?> declaringClass = method.getDeclaringClass();

    final Object newInstance;

    try {
      newInstance = declaringClass.newInstance();
      float startTime = System.nanoTime();

      method.invoke(newInstance, args);

      float endTime = System.nanoTime();

      return (endTime - startTime) / 1_000_000 / 1_000;
    } catch (InstantiationException | IllegalAccessException |
          InvocationTargetException e) {

      throw new LoggingFunctionException(e);
    }
  }
}
