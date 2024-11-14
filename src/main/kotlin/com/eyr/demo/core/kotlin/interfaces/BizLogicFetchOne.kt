package com.eyr.demo.core.kotlin.interfaces

import com.eyr.demo.core.kotlin.models.BizLogicModel

/**
 * A generic interface that defines a business logic contract for fetching data of a specified type.
 */
interface BizLogicFetchOne {

    /**
     * Executes the business logic for a given request and returns the result.
     *
     * @param req The request object containing the necessary data to perform the fetch operation.
     * @return A response object containing the result of the operation.
     * @param T The type of data being requested and returned.
     */
    fun <T> execute(req: BizLogicModel.REQ.Data<T>): BizLogicModel.RES.Single<T>
}