package com.eyr.demo.core.interfaces

import com.eyr.demo.core.models.BizLogicModel

/**
 * Interface defining business logic for operations that return a custom value.
 * @param V The type of data being returned.
 */
interface BizLogicValueReturn<R : BizLogicModel.REQ.ValueReturn, V> {
    /**
     * Executes the business logic for a given request and returns the result.
     *
     * @param req The request object containing the necessary data to perform the operation.
     * @return A response object containing the result of the operation.
     */
    fun execute(req: R): BizLogicModel.RES.Single<V>
}