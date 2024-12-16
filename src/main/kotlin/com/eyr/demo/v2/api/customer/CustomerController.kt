package cc.worldline.customermanagement.v2.api.customer

import cc.worldline.common.interfaces.Payload
import cc.worldline.common.models.Paged
import cc.worldline.common.models.Request
import cc.worldline.common.models.Response
import cc.worldline.customermanagement.v2.api.customer.CustomerModel.*
import cc.worldline.customermanagement.v2.business.user.UserProjector
import cc.worldline.customermanagement.v2.business.user.bizlogics.*
import cc.worldline.customermanagement.v2.common.utils.PageUtils
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.Valid

@RestController
@RequestMapping("/api/v2/customer")
class CustomerController(
    private val _mapper: CustomerMapper,
    private val _getUsersBizLogic: GetUsersBizLogic,
    private val _getUserBizLogic: GetUserBizLogic,
    private val _lockUserBizLogic: LockUserBizLogic,
    private val _updateUserBizLogic: UpdateUserBizLogic,
    private val _verifyBizLogic: VerifyUserBizLogic,
) {

    @PostMapping("/test-date")
    fun getDate(
        @RequestBody body: Request<Payload.Empty>
    ): Response<DateResponse> {
        return Response.success(
            DateResponse(
                date = LocalDate.now(),
                dateTime = LocalDateTime.of(2024, 12, 13, 10, 30, 0)
            )
        )
    }

    data class DateResponse(
        val date: LocalDate,
        val dateTime: LocalDateTime
    ) : Payload

    @PostMapping("get-customer")
    @Operation(summary = "Retrieve a single customer with given condition")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Success", content = [
                    Content(
                        mediaType = "application/json",
                    )
                ]
            ),
        ]
    )
    fun getCustomer(
        @Valid @RequestBody body: Request<GetCustomerREQ>
    ): Response<GetCustomerRES> = run {
        val response = _getUserBizLogic.execute(
            GetUserBizLogic.REQ(
                body.payload,
                UserProjector.fromString(body.payload.proj)
            )
        )

        Response.success(
            GetCustomerRES(
                response.data
            )
        )
    }

    @PostMapping("get-customers")
    @Operation(summary = "Retrieve customers with given condition")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Success", content = [
                    Content(
                        mediaType = "application/json",
                    )
                ]
            ),
        ]
    )
    fun getCustomers(
        @Valid @RequestBody body: Request<GetCustomersREQ>
    ): Response<GetCustomersRES> = run {
        val payload = body.payload

        val response = _getUsersBizLogic.execute(
            GetUsersBizLogic.REQ(
                PageUtils.getPageRequest(
                    payload.sort, payload.orderBy, payload.pageSize, payload.pageNumber,
                ),
                payload,
                UserProjector.fromString(body.payload.proj)
            )
        )

        Response.success(
            GetCustomersRES(
                customers = Paged(response.data)
            )
        )
    }

    @PatchMapping("lock-customer")
    @Operation(summary = "Lock a single customer by given condition")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Success", content = [
                    Content(
                        mediaType = "application/json",
                    )
                ]
            ),
        ]
    )
    fun lockCustomer(
        @Valid @RequestBody body: Request<LockCustomerREQ>
    ): Response<LockCustomerRES> = run {
        return@run Response.success(
            _lockUserBizLogic.execute(
                LockUserBizLogic.REQ(body.payload.username)
            ).let {
                _mapper.toLockedCustomerRES(it.data)
            }
        )
    }

    @PatchMapping("update-customer")
    @Operation(summary = "update a single customer by given condition")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Success", content = [
                    Content(
                        mediaType = "application/json",
                    )
                ]
            ),
        ]
    )
    fun updateCustomer(
        @Valid @RequestBody body: Request<UpdateCustomerREQ>
    ): Response<UpdateCustomerRES> = run {
        return@run Response.success(
            _updateUserBizLogic.execute(
                UpdateUserBizLogic.REQ(
                    payload = body.payload
                )
            ).let {
                _mapper.toUpdateCustomerRES(it.data)
            }
        )
    }

    @PostMapping("verify-customer")
    @Operation(summary = "synchronise customer data from the ASCCEND")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Success", content = [
                    Content(
                        mediaType = "application/json",
                    )
                ]
            ),
        ]
    )
    fun verifyCustomer(
        @Valid @RequestBody body: Request<VerifyCustomerREQ>
    ): Response<VerifyCustomerRES> = run {
        return@run Response.success(
            _verifyBizLogic.execute(
                VerifyUserBizLogic.REQ(
                    body.payload
                )
            ).let {
                _mapper.toVerifyCustomerRES(it.data)
            }
        )
    }

//    @PatchMapping("sync-customer")
//    @Operation(summary = "synchronise customer data from the ASCCEND")
//    @ApiResponses(
//        value = [
//            ApiResponse(
//                responseCode = "200", description = "Success", content = [
//                    Content(
//                        mediaType = "application/json",
//                    )
//                ]
//            ),
//        ]
//    )
//    fun syncCustomer(
//        @Valid @RequestBody body: Request<SyncCustomerREQ>
//    ): Response<SyncCustomerRES> = run {
//        Response.success(
//            when (val payload = body.payload) {
//                is SyncCustomerREQ.ForEmail -> _customerService.syncCustomerDetailForEmail(
//                    payload
//                )
//
//                is SyncCustomerREQ.ForPhone -> _customerService.syncCustomerDetailForPhone(
//                    payload
//                )
//            }
//        )
//    }
}