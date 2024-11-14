package com.eyr.demo.core.kotlin.annotaions

import kotlin.reflect.KClass

/**
 * Annotation used to mark functions for error handling.
 *
 * This annotation indicates that the annotated function or class should have
 * centralized error handling applied through an aspect, specifically
 * the `ErrorCatcherAspect`. It allows you to specify the context
 * (using the `where` parameter) in which the error occurred,
 * facilitating better error reporting and logging.
 *
 * @property where The class type indicating the context of the error,
 *                 which can be used for formatting error codes or
 *                 determining the source of the error.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ErrorCatcher(
    val where: KClass<*>
)