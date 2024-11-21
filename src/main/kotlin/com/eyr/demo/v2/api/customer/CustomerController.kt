package cc.worldline.customermanagement.v2.api.customer

import cc.worldline.common.models.Request
import cc.worldline.common.models.Response
import cc.worldline.customermanagement.v2.api.customer.CustomerModel.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v2/customer")
class CustomerController(
    private val _customerService: CustomerService,
) {
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
    suspend fun getCustomer(
        @Valid @RequestBody body: Request<GetCustomerREQ>
    ): Response<GetCustomerRES> = run {
        Response.success(
            when (val payload = body.payload) {
                is GetCustomerREQ.ByID -> _customerService.getCustomerByID(
                    payload
                )

                is GetCustomerREQ.ByIC -> _customerService.getCustomerByIC(
                    payload
                )

                is GetCustomerREQ.ByUsername -> _customerService.getCustomerByUsername(
                    payload
                )

                is GetCustomerREQ.ByCustomerNumber -> _customerService.getCustomerByCustomerNumber(
                    payload
                )
            }
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
    suspend fun getCustomers(
        @Valid @RequestBody body: Request<GetCustomersREQ>
    ): Response<GetCustomersRES> = run {
        Response.success(
            when (val payload = body.payload) {
                is GetCustomersREQ.ByCustomerNumber -> _customerService.getCustomersByCustomerNumber(
                    payload
                )

                is GetCustomersREQ.ByIC -> _customerService.getCustomersByIC(
                    payload
                )

                is GetCustomersREQ.All -> _customerService.getCustomers(
                    payload
                )

            }
        )
    }

    @PostMapping("lock-customer")
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
    suspend fun lockCustomer(
        @Valid @RequestBody body: Request<LockCustomerREQ>
    ): Response<Response.VoidPayload> = run {
        _customerService.lockCustomer(body.payload)

        Response.success()
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
    suspend fun updateCustomer(
        @Valid @RequestBody body: Request<UpdateCustomerREQ>
    ): Response<UpdateCustomerRES> = run {
        Response.success(
            when (val payload = body.payload) {
                is UpdateCustomerREQ.ByID -> _customerService.updateCustomerByID(
                    payload
                )

                is UpdateCustomerREQ.ByUsername -> _customerService.updateCustomerByUsername(
                    payload
                )

                is UpdateCustomerREQ.ByCustomerNumber -> _customerService.updateCustomerByCustomerNumber(
                    payload
                )
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
    suspend fun verifyCustomer(
        @Valid @RequestBody body: Request<VerifyCustomerREQ>
    ): Response<VerifyCustomerRES> = run {
        Response.success(
            when (val payload = body.payload) {
                is VerifyCustomerREQ.ByPwd -> _customerService.verifyCustomerByPassword(
                    payload
                )
            }
        )
    }

    @PatchMapping("sync-customer")
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
    suspend fun syncCustomer(
        @Valid @RequestBody body: Request<SyncCustomerREQ>
    ): Response<SyncCustomerRES> = run {
        Response.success(
            when (val payload = body.payload) {
                is SyncCustomerREQ.ForEmail -> _customerService.syncCustomerDetailForEmail(
                    payload
                )

                is SyncCustomerREQ.ForPhone -> _customerService.syncCustomerDetailForPhone(
                    payload
                )
            }
        )
    }
}