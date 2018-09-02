package org.alfiler.crawler.scraper

import cats.implicits._
import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

import scala.collection.immutable
import scala.collection.immutable.SortedSet
import scala.util.Try
import scala.util.matching.Regex

object Scraper{

  import Element.implicits._
  private def createFormula(input:Set[(String,String)], outputName:String, outputAmmount:Option[String]):Formula = {
    val elementsList = input.map(t => Element(t._1.trim,t._2.toInt))
    val outputElement = Element(outputName.trim,outputAmmount.map(_.toInt).getOrElse(1) )
    Formula(outputElement,SortedSet.empty[Element] ++ elementsList,"refinery")
  }

  private val element = """(\w[\w| |\-|']+\w)"""
  private val elementCuantity =  raw"""$element x *([0-9]+)"""
  private val receipeName = """(\w[\w|\-|\s|\/]+)"""
  private val arrow = 8594.toChar
  private val creationSpeed = """(\d+[\.\d+]*|\?)"""
  private val endOfCrafting = raw"""(?: *\(refines to $elementCuantity with $creationSpeed *sec\.?\/unit *output(?:, process "$receipeName")?\))?\.?,?$$"""
  val threeElementFormula: Regex = raw"""^$elementCuantity \+ $elementCuantity \+ $elementCuantity $arrow $elementCuantity \("$receipeName",\s*$creationSpeed\s*sec\.\/unit output\s*\)\s*$$""".r
  val twoElementFormula: Regex = raw"""^$elementCuantity \+ $elementCuantity $arrow $elementCuantity \("$receipeName",\s*$creationSpeed\s*sec\.\/unit output\s*\)\s*$$""".r
  val oneElementFormula: Regex = raw"""^$elementCuantity $arrow $elementCuantity \("$receipeName",\s*$creationSpeed\s*sec\.\/unit output\s*\)\s*$$""".r
  val elementOfSource: Regex = raw"""^$elementCuantity$$""".r
  val threeElementCrafting:Regex = raw"""^$element *- *\( *$elementCuantity *\+ *$elementCuantity *\+ *$elementCuantity *\)$endOfCrafting""".r
  val twoElementCrafting:Regex =   raw"""^$element *- *\( *$elementCuantity *\+ *$elementCuantity *\)$endOfCrafting""".r
  val oneElementCrafting:Regex =   raw"""^$element *- *\( *$elementCuantity *\)$endOfCrafting""".r
  val parse:String => Option[Line] = {
    case threeElementFormula(i1,a1,i2,a2,i3,a3,oi,oa, _*) =>
      createFormula(Set((i1,a1),(i2,a2),(i3,a3)),oi,Option(oa)).some
    case twoElementFormula(i1,a1,i2,a2,oi,oa, _*) =>
      createFormula(Set((i1,a1),(i2,a2)),oi,Option(oa)).some
    case oneElementFormula(i1,a1,oi,oa, _*) =>
      createFormula(Set((i1,a1)),oi,Option(oa)).some
    case elementOfSource(name,units) => Element(name,units.toInt).some
    case threeElementCrafting(oi,i1,a1,i2,a2,i3,a3,_,oa, _*) =>
      createFormula(Set((i1,a1),(i2,a2),(i3,a3)),oi,Option(oa)).some
    case twoElementCrafting(oi,i1,a1,i2,a2,_,oa, _*) =>
      createFormula(Set((i1,a1),(i2,a2)),oi,Option(oa)).some
    case oneElementCrafting(oi,i1,a1,_,oa, _*) =>
      createFormula(Set((i1,a1)),oi,Option(oa)).some
    case _ => none[Line]
  }

  private val extractBlueprintFormula:String => List[Line] => Option[Formula] = elementName => formulas => {
    val elements = formulas.flatMap{
      case e:Element => e.some
      case _ => none[Element]
    }.toSet
    if (elements.isEmpty)
      none[Formula]
    else
      Formula(Element(elementName,1),SortedSet.empty[Element] ++ elements,"blueprint").some
  }




}

class Scraper(elementName:String)(implicit browser:Browser) {
  val urlElementName: String = elementName.replace(" ","_")
  private lazy val doc = Try{browser.get("https://nomanssky.gamepedia.com/" + urlElementName)}
  private val blueprintExtractor = Scraper.extractBlueprintFormula(elementName)
  val getFormulas:List[Line] => Set[Formula] = linesParsed => {
    val onlyFormulas = linesParsed.flatMap{
      case e:Formula => e.some
      case _ => None
    }.toSet
    blueprintExtractor(linesParsed).fold(onlyFormulas)(onlyFormulas + _)
  }

  def get: Set[Formula] = {
    val linesParsed:List[Line] = doc.map(d => (d >> elementList("li")).map(_.text).flatMap(t => Scraper.parse(t))).getOrElse(List.empty)
    getFormulas(linesParsed)
  }
}
