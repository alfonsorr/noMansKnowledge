package org.alfiler.crawler.scraper

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

object Main {
  def main(args: Array[String]): Unit = {
    implicit val browser = JsoupBrowser()
  }
}
