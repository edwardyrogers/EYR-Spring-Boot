package cc.worldline.customermanagement.v2.api.inbox

import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/inbox")
@Api(description = "Customer Inbox API")
class InboxController