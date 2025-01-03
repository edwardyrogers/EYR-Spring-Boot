package com.eyr.demo.core.interfaces

import com.eyr.demo.core.models.BizLogicModel

/**
 * Interface defining business logic for fetching multiple items in a paginated format.
 */
interface BizLogicFetchMany {

    /**
     * Executes the fetch operation based on the provided request and returns a paginated result.
     *
     * @param req The request object containing parameters needed to execute the fetch operation.
     * @return A paginated response containing multiple items of type T.
     * @param T The type of data items being fetched.
     */
    fun <T> execute(req: BizLogicModel.REQ.Data<T>): BizLogicModel.RES.Paginated<T>
}