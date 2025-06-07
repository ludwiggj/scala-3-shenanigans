package com.ludwig.tagless.itv.news.testing.tagless_final

import Model.ArticleEvent

sealed trait Event

object Event {
  case class ArticleFetched(id: String) extends Event

  case class ContentfulCalled(topic: String) extends Event

  case class EventProduced(event: ArticleEvent) extends Event
}