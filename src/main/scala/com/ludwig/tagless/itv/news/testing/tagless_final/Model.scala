package com.ludwig.tagless.itv.news.testing.tagless_final

object Model {
  final case class Article(id: String, title: String, topic: String)

  final case class ArticleEvent(article: Article, relatedArticles: List[Article])

  final case class ArticleNotFound(id: String) extends Throwable
}
