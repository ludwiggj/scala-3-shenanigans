package com.ludwig.tagless.itv.news.testing.tagless_final

import Model.Article

trait ArticleRepo[F[_]] {
  def get(id: String): F[Option[Article]]
}

object ArticleRepo {
  def apply[F[_]](implicit ar: ArticleRepo[F]): ArticleRepo[F] = ar
}
