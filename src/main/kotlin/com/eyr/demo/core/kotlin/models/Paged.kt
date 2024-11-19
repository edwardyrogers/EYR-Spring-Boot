package cc.worldline.common.models

import cc.worldline.common.interfaces.Payload
import org.springframework.data.domain.Page

/**
 * A data class representing a paginated response.
 *
 * @param T The type of content in the pagination.
 * @property pageInfo Contains information about the pagination (size, total pages, etc.).
 * @property content The list of items for the current page.
 */
data class Paged<T>(
    val pageInfo: PageInfo,
    val content: List<T>
) : Payload {

    /**
     * Secondary constructor that initializes Paged with a Spring Page object.
     *
     * @param page The Spring Page object containing pagination information and content.
     */
    @Suppress("unused")
    constructor(page: Page<T>) : this(
        PageInfo(page),
        page.content
    )
}