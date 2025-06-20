package com.ludwig.tagless.itv.news.testing.tagless_final

import Model.ArticleEvent

trait ProduceEvent[F[_]] {
  def produce(event: ArticleEvent): F[Unit]
}

object ProduceEvent {
  def apply[F[_]](implicit pe: ProduceEvent[F]): ProduceEvent[F] = pe
}
