package org.alfiler.crawler.scraper

import scala.util.Try

object ElementsQueue{
  val empty: ElementsQueue = ElementsQueue(Set.empty, Set.empty)
}

case class ElementsQueue(waiting:Set[String], readed:Set[String]) {
  def head: Option[String] = waiting.headOption
  def pop: ElementsQueue = waiting.headOption.map(h => ElementsQueue(waiting.tail, readed + h)).getOrElse(this)
  val add: Set[String] => ElementsQueue = e => {
    ElementsQueue(e.diff(readed)++waiting, readed)
  }
}
