package com.eyr.demo.business.user

import com.eyr.demo.datasource.user.UserRepoEntity
import org.springframework.data.jpa.domain.Specification

class UserSpecs {
    companion object {
        fun byStatus(
            status: UserRepoEntity.UserStatus
        ): Specification<UserRepoEntity> = run {
            Specification { root, _, builder ->
                builder.equal(root.get<UserRepoEntity.UserStatus>("status"), status);
            }
        }
    }
}