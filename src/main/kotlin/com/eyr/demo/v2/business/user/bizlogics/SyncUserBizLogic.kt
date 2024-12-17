package cc.worldline.customermanagement.v2.business.user.bizlogics

import com.eyr.demo.common.constants.ReturnCode
import cc.worldline.common.exceptions.ServiceException
import cc.worldline.common.interfaces.BizLogicValueReturn
import cc.worldline.common.models.BizLogicModel
import cc.worldline.common.utils.DateUtils
import cc.worldline.customermanagement.v2.business.user.UserProjector
import cc.worldline.customermanagement.v2.datasource.user.UserRepository
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class SyncUserBizLogic(
    private val _repository: UserRepository,

    private val otpUsageLimitDays: Long = 30
) : BizLogicValueReturn<SyncUserBizLogic.REQ, SyncUserBizLogic.RES> {

    override fun execute(req: REQ): BizLogicModel.RES.Single<RES> = run {
        val userRes = _repository.findByUsernameIgnoreCase(
            username = req.username, UserProjector.UserEntity::class.java
        ).orElse(null)

        val user = userRes ?: throw ServiceException(
            ReturnCode.NOT_FOUND, "find $req"
        )

//        val userFromAsscend = _ctrClient.onBoardingCustomer(user.ic).payload ?: throw ServiceException(
//            ReturnCode.NOT_FOUND, "Connector.onBoardingCustomer"
//        )
//
//        if (userFromAsscend.responseCode == "00") {
//            val modifiedDate: String?
//
//            when (req) {
//                is REQ.ForEmail -> {
//                    modifiedDate = userFromAsscend.modifiedEmailDate
//
//                    user.email = userFromAsscend.emailAdd
//                    user.emailModifiedDate = userFromAsscend.modifiedEmailDate?.let { dateString ->
//                        toLocalDateTime(dateString)
//                    }
//
//                    _repository.save(user)
//                }
//
//                is REQ.ForPhone -> {
//                    modifiedDate = userFromAsscend.modifiedMobileDate
//
//                    user.phoneNo = userFromAsscend.mobileNumber
//                    user.phoneModifiedDate = userFromAsscend.modifiedMobileDate?.let { dateString ->
//                        toLocalDateTime(dateString)
//                    }
//
//                    _repository.save(user)
//                }
//            }
//
//            BizLogicModel.RES.Single(
//                data = RES(
//                    isInOtpLimited = when {
//                        modifiedDate.isNullOrBlank() -> true
//
//                        else -> LocalDate.now().minusDays(otpUsageLimitDays) >= LocalDate.parse(
//                            modifiedDate,
//                            DateUtils.asianDateFormatter("")
//                        )
//                    }
//                )
//            )
//        } else {
//            throw ServiceException(
//                ReturnCode.ASCCEND_ERROR,
//                message = AsccendCode.toMessage(
//                    userFromAsscend.responseCode, userFromAsscend.responseDesc
//                )
//            )
//        }

        throw ServiceException(
            ReturnCode.GENERAL_ERROR,
            "Not Implement"
        )
    }

    private fun toLocalDateTime(dateString: String): LocalDateTime = run {
        LocalDate.parse(
            dateString,
            DateUtils.asianDateFormatter("")
        )
            .atTime(
                LocalTime.of(0, 0, 0)
            )
    }

    sealed class REQ(
        open val username: String,
    ) : BizLogicModel.REQ.ValueReturn() {
        data class ForEmail(
            override val username: String,
        ) : REQ(username)

        data class ForPhone(
            override val username: String,
        ) : REQ(username)
    }


    data class RES(
        val isInOtpLimited: Boolean
    )
}