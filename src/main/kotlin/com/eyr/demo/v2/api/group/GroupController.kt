package cc.worldline.customermanagement.v2.api.group

import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/group")
@Api(description = "Customer Group API")
class GroupController