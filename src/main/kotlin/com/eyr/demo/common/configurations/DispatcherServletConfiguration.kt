package com.eyr.demo.common.configurations

import jakarta.servlet.Filter
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer

@Configuration
class DispatcherServletConfiguration: AbstractAnnotationConfigDispatcherServletInitializer() {
    override fun getServletMappings(): Array<String?> {
        return arrayOfNulls(0)
    }

    override fun getServletFilters(): Array<Filter>? {
        return arrayOf()
    }

    override fun getRootConfigClasses(): Array<Class<*>?> {
        return arrayOfNulls(0)
    }

    override fun getServletConfigClasses(): Array<Class<*>?> {
        return arrayOfNulls(0)
    }
}