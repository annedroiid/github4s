/*
 * Copyright 2016-2023 47 Degrees Open Source <https://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github4s.domain

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, ZonedDateTime}

sealed trait SearchParam {
  protected def paramName: String
  protected def paramValue: String
  def value: String = s"$paramName:$paramValue"
}

sealed trait IssueType extends SearchParam {
  override def paramName: String = "type"
}

case object IssueTypeIssue extends IssueType {
  override def paramValue: String = "issue"
}

case object IssueTypePullRequest extends IssueType {
  override def paramValue: String = "pr"
}

final case class SearchIn(values: Set[SearchInValue]) extends SearchParam {
  override def paramName: String  = "in"
  override def paramValue: String = values.map(_.value).mkString(",")
}

sealed trait SearchInValue {
  def value: String
}

case object SearchInTitle extends SearchInValue {
  override def value: String = "title"
}

case object SearchInBody extends SearchInValue {
  override def value: String = "body"
}

case object SearchInComments extends SearchInValue {
  override def value: String = "comments"
}

sealed trait IssueState extends SearchParam {
  override def paramName: String = "state"
}

case object IssueStateOpen extends IssueState {
  override def paramValue: String = "open"
}

case object IssueStateClosed extends IssueState {
  override def paramValue: String = "closed"
}

final case class LabelParam(label: String, exclude: Boolean = false) extends SearchParam {
  override def paramName: String = s"${if (exclude) "-" else ""}label"

  override def paramValue: String = label
}

sealed trait OwnerParam extends SearchParam

final case class OwnerParamOwnedByUser(user: String) extends OwnerParam {
  override def paramName: String = "user"

  override def paramValue: String = user
}

final case class OwnerParamInRepository(repo: String) extends OwnerParam {
  override def paramName: String = "repo"

  override def paramValue: String = repo
}

final case class LanguageParam(language: String) extends SearchParam {
  override def paramName: String = "language"

  override def paramValue: String = language
}

final case class TopicParam(topic: String) extends SearchParam {
  override def paramName: String = "topic"

  override def paramValue: String = topic
}

final case class AuthorParam(author: String) extends SearchParam {
  override protected def paramName: String = "author"

  override protected def paramValue: String = author
}

sealed trait PullRequestStatus extends SearchParam {
  override def paramName: String = "is"
}

case object PullRequestStatusMerged extends PullRequestStatus {
  override protected def paramValue: String = "merged"
}

case object PullRequestStatusUnmerged extends PullRequestStatus {
  override protected def paramValue: String = "unmerged"
}

sealed abstract class DateOperator(val value: String)

object DateOperator {
  case object AfterDate      extends DateOperator(">")
  case object AfterOrOnDate  extends DateOperator(">=")
  case object BeforeDate     extends DateOperator("<")
  case object BeforeOrOnDate extends DateOperator("<=")
  case object OnDate         extends DateOperator("")
}

sealed trait PullRequestMergedParam extends SearchParam {
  override def paramName: String = "merged"

  def dateFormat: DateTimeFormatter     = DateTimeFormatter.ISO_DATE
  def dateTimeFormat: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
}

final case class PullRequestMergedDate(date: LocalDate, operator: DateOperator)
    extends PullRequestMergedParam {
  override def paramValue: String = operator.value + date.format(dateFormat)
}

final case class PullRequestMergedDateTime(date: ZonedDateTime, operator: DateOperator)
    extends PullRequestMergedParam {
  override def paramValue: String = operator.value + date.format(dateTimeFormat)
}

final case class PullRequestMergedBetweenDates(date1: LocalDate, date2: LocalDate)
    extends PullRequestMergedParam {
  override protected def paramValue: String =
    date1.format(dateFormat) + ".." + date2.format(dateFormat)
}

final case class PullRequestMergedBetweenDateTimes(date1: ZonedDateTime, date2: ZonedDateTime)
    extends PullRequestMergedParam {
  override protected def paramValue: String =
    date1.format(dateTimeFormat) + ".." + date2.format(dateTimeFormat)
}
