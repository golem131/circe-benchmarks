package io.circe.benchmarks

import org.openjdk.jmh.annotations._
import play.api.libs.functional.syntax._
import play.api.libs.json.{ Format, JsPath, JsValue, Json, Writes }

/**
 * Note that this file appears in both the scala-2.11 and scala-2.12 source trees, and any changes
 * should be reflected in both places.
 */
trait PlayFooInstances {
  implicit val playFormatFoo: Format[Foo] = (
    (JsPath \ "s").format[String] and
    (JsPath \ "d").format[Double] and
    (JsPath \ "i").format[Int] and
    (JsPath \ "l").format[Long] and
    (JsPath \ "bs").format[List[Boolean]]
  )(Foo.apply, unlift(Foo.unapply))
}

trait PlayData { self: ExampleData =>
  @inline def encodeP[A](a: A)(implicit encode: Writes[A]): JsValue = encode.writes(a)

  val foosP: JsValue = encodeP(foos)
  val intsP: JsValue = encodeP(ints)
}

trait PlayEncoding { self: ExampleData =>
  @Benchmark
  def encodeFoosPlay: JsValue = encodeP(foos)

  @Benchmark
  def encodeIntsPlay: JsValue = encodeP(ints)
}

trait PlayDecoding { self: ExampleData =>
  @Benchmark
  def decodeFoosPlay: Map[String, Foo] = foosP.as[Map[String, Foo]]

  @Benchmark
  def decodeIntsPlay: List[Int] = intsP.as[List[Int]]
}

trait PlayPrinting { self: ExampleData =>
  @Benchmark
  def printFoosPlay: String = Json.stringify(foosP)

  @Benchmark
  def printIntsPlay: String = Json.stringify(intsP)
}

trait PlayParsing { self: ExampleData =>
  @Benchmark
  def parseFoosPlay: JsValue = Json.parse(foosJson)

  @Benchmark
  def parseIntsPlay: JsValue = Json.parse(intsJson)
}
