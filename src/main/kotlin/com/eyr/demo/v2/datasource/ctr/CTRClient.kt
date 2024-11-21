package cc.worldline.customermanagement.v2.datasource.ctr

import cc.worldline.customermanagement.common.bean.ApiResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "CTR",
    url = "\${connector-service.feign-url}"
)
interface CTRClient {
    /**
     * 0003
     */
    @GetMapping("/api/v1/customer")
    fun onBoardingCustomer(
        @RequestParam identityNumber: String,
    ): ApiResponse<CTRModel.CustomerProfileRES>
}