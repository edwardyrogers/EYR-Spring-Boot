package com.eyr.demo.core.constants

class CoreConst {
    companion object {
        const val MIDDLEWARE_CONDITION = "" +
                "(@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
                "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
                "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
                "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
                "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
                "@annotation(org.springframework.web.bind.annotation.PatchMapping)) && " +
                "!@within(org.springframework.cloud.openfeign.FeignClient)"
    }
}