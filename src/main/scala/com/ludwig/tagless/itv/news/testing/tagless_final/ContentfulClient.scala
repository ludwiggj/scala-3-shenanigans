package com.ludwig.tagless.itv.news.testing.tagless_final

import Model.Article

trait ContentfulClient[F[_]] {
  def articlesByTopic(topic: String): F[List[Article]]
}

object ContentfulClient {
  def apply[F[_]](implicit cc: ContentfulClient[F]): ContentfulClient[F] = cc
}
