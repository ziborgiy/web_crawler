package com.ziborgiy.webcrawler

import cats.effect.IO
import com.ziborgiy.webcrawler.operations.Titles
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TitlesSpec extends AnyFlatSpec with Matchers {

  private val BASIC_KEY = "https://blog.oyanglul.us/scala/dotty/en/dependent-types"
  private val EXCEPTED_RESULT = "Dependent Types in Scala 3"

  it should "match result of Jsoup.connect method and value in map" in {
    Titles.impl[IO].getTitles(Vector(BASIC_KEY)).unsafeRunSync()(BASIC_KEY) should be (EXCEPTED_RESULT)
  }

}
