package com.eyr.demo.common.utils

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class PageUtils {
    companion object {
        fun getPageRequest(
            sort: String? = null,
            orderBy: String = "createdDate",
            pageSize: Int = 20,
            pageNumber: Int = 0,
        ) = run {
            PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by(
                    if (
                        sort.isNullOrBlank() ||
                        sort.equals("ASC", ignoreCase = true)
                    ) {
                        Sort.Direction.ASC
                    } else {
                        Sort.Direction.DESC
                    },
                    orderBy
                )
            )
        }
    }
}