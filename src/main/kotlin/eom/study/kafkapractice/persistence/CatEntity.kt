package eom.study.kafkapractice.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId

@Table(value = "cat")
data class CatEntity(
    @Id
    val catId : Long? = null,
    var sex : Int,
    var name : String,
    var type : String,
    var age : Int,
    var created : LocalDateTime? = java.time.LocalDateTime.now(Clock.system(ZoneId.of("Asia/Seoul"))),
    var modified : LocalDateTime? = null
)
