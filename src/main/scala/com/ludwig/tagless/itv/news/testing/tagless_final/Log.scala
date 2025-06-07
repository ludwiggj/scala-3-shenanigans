package com.ludwig.tagless.itv.news.testing.tagless_final

trait Log[F[_]] {
  def info(msg: String): F[Unit]
}

object Log {
  def apply[F[_]](implicit logger: Log[F]): Log[F] = logger
}