package cc.worldline.common.interfaces

import cc.worldline.common.models.BizLogicModel

/**
 * Interface defining business logic for operations that do not return a result.
 */
interface BizLogicVoid<R : BizLogicModel.REQ.Void> {

    /**
     * Executes an operation based on the provided request, but does not return a result.
     *
     * @param req The request object containing the necessary data to execute the operation.
     * @return Void, indicating that no meaningful result is returned.
     */
    suspend fun execute(req: R)
}