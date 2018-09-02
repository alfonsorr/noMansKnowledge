package org.alfiler.crawler.scraper

import org.scalatest.{FlatSpecLike, Matchers}

class ElementsTest extends FlatSpecLike with Matchers{
  "ElementsQueue" should "have correct behaviour if inserted something not allready extracted" in {
    val test = for {
      _ <- ElementsQueueState.addNew(Set("a"))
      withB <- ElementsQueueState.addNew(Set("a"))
    } yield withB
    test.run(ElementsQueue.empty).value shouldBe (ElementsQueue(Set("a"), Set.empty), Set("a"))
  }

  it should "have correct behaviour if inserted something allready extracted" in {
    val test = for {
      _ <- ElementsQueueState.addNew(Set("a"))
      _ <- ElementsQueueState.nextElement
      withB <- ElementsQueueState.addNew(Set("a"))
    } yield withB
    test.run(ElementsQueue.empty).value shouldBe (ElementsQueue(Set.empty,Set("a")), Set("a"))
  }
  it should "have correct behaviour if all elements are readed" in {
    val test = for {
      _ <- ElementsQueueState.addNew(Set("a","b","c"))
      fst <- ElementsQueueState.nextElement
      scd <- ElementsQueueState.nextElement
      thr <- ElementsQueueState.nextElement
      last <- ElementsQueueState.nextElement
    } yield (fst,scd,thr,last)
    test.run(ElementsQueue.empty).value shouldBe (ElementsQueue(Set.empty,Set("a","b","c")),(Some("a"), Some("b"),Some("c"),None))
  }
}
