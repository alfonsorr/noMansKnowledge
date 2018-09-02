package org.alfiler.crawler.scraper

import cats.data._
import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import org.scalatest.{FlatSpecLike, Matchers}
import cats.implicits._
import cats.instances._
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import java.io._

class CrawlerTest extends FlatSpecLike with Matchers{
  implicit val browser: Browser = JsoupBrowser()
  "Crawler" should "scan a page and return the formulas with the new state" in {
    val program: State[ElementsQueue, Option[Set[Formula]]] = (1 to 2000).map(_ => ElementsQueueState.scanPage).reduce((a, b) => for{
      ae <- a
      be <- b
    } yield ae |+| be)
    val result = program.run(ElementsQueue.empty.add(Set("Gold"))).value
    println(result._1)
    val pw = new PrintWriter(new File("hello.json" ))
    result._2.get.map(_.asJson.noSpaces+"\n").foreach(pw.write)
    pw.close()

  }
}
