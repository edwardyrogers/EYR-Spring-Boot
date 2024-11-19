@file:Suppress("unused")

package cc.worldline.common.filters.model

import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * A filter for processing HTTP requests and responses for specific API endpoints.
 *
 * This filter extends [OncePerRequestFilter], ensuring that it is invoked only once per request.
 * It wraps the incoming request and response to add custom behavior and manage request metadata.
 *
 * It checks the request URI to determine whether the request should be processed further or skipped.
 *
 * @constructor Creates a new instance of [ModelFilter].
 */
class ModelFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        // Check if the request is for the excluded endpoints
        if (!request.requestURI.startsWith("/api/v2/")) {
            // If the request URI does not start with "/api/v2/",
            // skip processing of this filter and continue with the next one in the chain
            chain.doFilter(request, response)
            return
        }

        // Wrap the request and response objects to add custom behavior
        val reqWrapped = ModelReqWrapper(request) // Create a wrapped version of the request
        val resWrapped = ModelResWrapper(response) // Create a wrapped version of the response

        // Set metadata for the current request in the wrapped request object
        reqWrapped.setCurrentMeta()

        // Continue the filter chain with the wrapped request and response
        chain.doFilter(reqWrapped, resWrapped)
    }
}