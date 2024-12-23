package com.eyr.demo.core.models

import org.springframework.data.domain.Page

/**
 * A class representing pagination information.
 *
 * @property size The number of items per page.
 * @property elements The number of elements in the current page.
 * @property currentPageIndex The index of the current page (0-based).
 * @property isLast Indicates whether the current page is the last page.
 * @property isFirst Indicates whether the current page is the first page.
 * @property totalPages The total number of pages available.
 * @property totalElements The total number of elements across all pages.
 *
 * This class can be constructed using either a Spring `Page` object or a total element count.
 */
data class PageInfo(
    var size: Int = 0,
    var elements: Int = 0,
    var currentPageIndex: Int = 0,
    var isLast: Boolean = false,
    var isFirst: Boolean = false,
    var totalPages: Int = 0,
    var totalElements: Long = 0
) {


    /**
     * Constructor that initializes the PageInfo with data from a Spring Page object.
     *
     * @param page The Spring Page object containing pagination information.
     */
    constructor(page: Page<*>) : this() {
        size = page.size
        elements = page.numberOfElements
        currentPageIndex = page.number
        isLast = page.isLast
        isFirst = page.isFirst
        totalPages = page.totalPages
        totalElements = page.totalElements
    }

    /**
     * Constructor that initializes the PageInfo with a total element count.
     *
     * @param totalElements The total number of elements. This assumes a single page of data.
     */
    constructor(totalElements: Int) : this() {
        size = totalElements
        elements = totalElements
        currentPageIndex = 0
        isLast = true
        isFirst = true
        totalPages = 1
        this.totalElements = totalElements.toLong()
    }
}