package eom.study.kafkapractice.persistence

import org.springframework.data.r2dbc.repository.R2dbcRepository

interface CatRepository : R2dbcRepository<Long , CatEntity> {

}