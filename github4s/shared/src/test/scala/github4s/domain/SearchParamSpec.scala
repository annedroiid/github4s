package github4s.domain

import github4s.domain.DateOperator._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.time.{LocalDate, ZoneOffset, ZonedDateTime}

class SearchParamSpec extends AnyFlatSpec with Matchers {
  "PullRequestMergedDate.value" should "format the Date using ISO_DATE format and prepend the operator" in {
    val prMergedDateParam = PullRequestMergedDate(LocalDate.of(2023, 3, 5), BeforeDate)
    prMergedDateParam.value shouldBe "merged:<2023-03-05"
  }

  "PullRequestMergedDateTime.value" should "format the Date using ISO_DATE_TIME format and prepend the operator" in {
    val prMergedDateParam = PullRequestMergedDateTime(
      ZonedDateTime.of(2023, 3, 5, 16, 5, 30, 0, ZoneOffset.ofHours(4)),
      AfterOrOnDate
    )
    prMergedDateParam.value shouldBe "merged:>=2023-03-05T16:05:30+04:00"
  }

  "PullRequestMergedBetweenDates.value" should "format the Date using ISO_DATE format and prepend the operator" in {
    val prMergedDateParam =
      PullRequestMergedBetweenDates(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 5, 4))
    prMergedDateParam.value shouldBe "merged:2023-01-01..2023-05-04"
  }

  "PullRequestMergedBetweenDateTimes.value" should "format the Date using ISO_DATE_TIME format and prepend the operator" in {
    val prMergedDateParam =
      PullRequestMergedBetweenDateTimes(
        ZonedDateTime.of(2023, 1, 1, 4, 45, 15, 0, ZoneOffset.ofHours(8)),
        ZonedDateTime.of(2023, 12, 25, 8, 30, 0, 0, ZoneOffset.ofHours(14))
      )
    prMergedDateParam.value shouldBe "merged:2023-01-01T04:45:15+08:00..2023-12-25T08:30:00+14:00"
  }
}
