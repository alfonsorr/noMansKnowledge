package org.alfiler.crawler.scraper
import cats.data.State
import net.ruippeixotog.scalascraper.browser.Browser

object ElementsQueueState {
  val nextElement:State[ElementsQueue,Option[String]] = State(queue => (queue.pop, queue.head))
  val addNew:Set[String] => State[ElementsQueue,Set[String]] = n => State(queue => (queue.add(n), n))

  def scanPage(implicit browser: Browser):State[ElementsQueue, Option[Set[Formula]]] = for {
    name <- nextElement
    _ = println(s"extracting: $name")
    formulas = name.map(new Scraper(_).get)
    elements = formulas.map(_.flatMap(f => f.input.map(_.name) + f.output.name))
    _ <- addNew(elements.getOrElse(Set.empty))
  } yield formulas
}


