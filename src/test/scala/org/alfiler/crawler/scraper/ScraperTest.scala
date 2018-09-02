package org.alfiler.crawler.scraper

import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import org.scalatest.{FlatSpecLike, Matchers}

class ScraperTest extends FlatSpecLike with Matchers{
  import Formula.implicits._
  implicit val browser: Browser = JsoupBrowser()

  "Scraper" should "get Emeril correctly" in {
    val emeril = new Scraper("Emeril").get
    emeril.foreach(println)
    emeril.size shouldBe 14
  }

  it should "get Chromatic Metal correctly" in {
    val emeril = new Scraper("Chromatic Metal").get
    emeril.toList.sorted.foreach(println)
    emeril.size shouldBe 49
  }
}
