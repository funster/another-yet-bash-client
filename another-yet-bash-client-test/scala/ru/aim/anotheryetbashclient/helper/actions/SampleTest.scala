package ru.aim.anotheryetbashclient.helper.actions

import org.scalatest._
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.client.methods.HttpGet
import scala.io.Source
import java.net.URLEncoder

/**
 *
 */
class SampleTest extends FlatSpec {

//  it should "be created properly" in {
//    val httpClient = SampleTest.httpClient
//    val httpGet = new HttpGet("http://bash.im/index?text=42")
//    val result = httpClient.execute(httpGet)
//    val charset = result.getEntity.getContentType.getValue.split("=")(1)
//    val st = Source.fromInputStream(result.getEntity.getContent, charset).getLines().mkString("\n")
//    println(st)
//  }

  it should "be return something new" in {
    val httpClient = SampleTest.httpClient
    val encoded = URLEncoder.encode("сектор газа", "windows-1251")
    val url = "http://bash.im/index?text=" + encoded
    val httpGet = new HttpGet(url)
//    val httpGet = new HttpGet("http://bash.im/index?text=%F1%E5%EA%F2%EE%F0+%E3%E0%E7%E0")
    println(s"Sending request $url")
    val result = httpClient.execute(httpGet)
    val charset = result.getEntity.getContentType.getValue.split("=")(1)
    val st = Source.fromInputStream(result.getEntity.getContent, charset).getLines().mkString("\n")
    println(st)
  }

}

object SampleTest {

  lazy val httpClient = HttpClientBuilder.create().build()

}