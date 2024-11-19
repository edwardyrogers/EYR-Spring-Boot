package cc.worldline.common.interfaces

import cc.worldline.common.models.BizLogicModel

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
    suspend fun execute(req: R): BizLogicModel.RES.Single<V>
}