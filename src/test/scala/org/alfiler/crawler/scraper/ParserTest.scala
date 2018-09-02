package org.alfiler.crawler.scraper

import org.scalatest.{FlatSpecLike, Matchers}

import scala.collection.immutable.SortedSet
import cats.implicits._
import Element.implicits._

class ParserTest extends FlatSpecLike with Matchers{

  "Parse" should "parse Chromatic Metal - (Pure Ferrite x1 + Emeril x1) (refines to Chromatic Metal x3 with 0.9 sec/unit output, process \"Chromatic Metal Fusion\")," in {
    val test = "Chromatic Metal - (Pure Ferrite x1 + Emeril x1) (refines to Chromatic Metal x3 with 0.9 sec/unit output, process \"Chromatic Metal Fusion\"),"
    Scraper.parse(test) shouldBe Formula(Element("Chromatic Metal",3),SortedSet(Element("Pure Ferrite",1),Element("Emeril",1)),"refinery").some
  }


  it should "parse with extrange characters"in {
    val test = "Chromatic Metal - (Gold x1 + Silver x1 + Emeril x1) (refines to Chromatic Metal x20 with ? sec/unit output, process \"RECIPE_3INPUT_STELLAR2\")"
    Scraper.parse(test) shouldBe Formula(Element("Chromatic Metal",20),SortedSet(Element("Gold",1),Element("Silver",1),Element("Emeril",1)),"refinery").some
  }

  it should "parse with construction terminal"in {
    val test = "Construction Terminal - (Chromatic Metal x 40 + Pure Ferrite x 25),"
    Scraper.parse(test) shouldBe Formula(Element("Construction Terminal",1),SortedSet(Element("Chromatic Metal",40),Element("Pure Ferrite",25)),"refinery").some
  }

  it should "parse with questions in speed"in {
    val test = "Gold x1 + Silver x1 + Copper x1 → Chromatic Metal x5 (\"RECIPE_3INPUT_STELLAR2\", ? sec./unit output)"
    Scraper.parse(test) shouldBe Formula(Element("Chromatic Metal",5),SortedSet(Element("Gold",1),Element("Silver",1),Element("Copper",1)),"refinery").some
  }
  it should "parse with lines"in {
    val test = "Haz-Mat Gauntlets - (Chromatic Metal x 50 + Sodium Nitrate x 20),"
    Scraper.parse(test) shouldBe Formula(Element("Haz-Mat Gauntlets",1),SortedSet(Element("Chromatic Metal",50),Element("Sodium Nitrate",20)),"refinery").some
  }


}
