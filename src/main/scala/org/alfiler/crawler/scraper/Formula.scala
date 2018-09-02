package org.alfiler.crawler.scraper

import scala.collection.immutable.SortedSet

trait Line

object Formula{
  trait FormulaImplicits {
    implicit val ordering: Ordering[Formula] =
      Ordering.by(e => e.output.name+e.input.size+e.input.map(_.name).mkString(""))
  }
  object implicits extends FormulaImplicits
}

case class Formula(output:Element, input:SortedSet[Element], craftType:String) extends Line

object Element {
  trait ElementImplicits {
    implicit val ordering: Ordering[Element] = Ordering.by(_.name)
  }
  object implicits extends ElementImplicits
}
case class Element(name:String, amount:Int) extends Line