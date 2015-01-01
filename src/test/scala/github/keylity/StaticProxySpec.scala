package github.keylity

import org.specs2.mutable._

import scala.collection.mutable.Buffer
import scala.collection.mutable.ArrayBuffer

class StaticProxySpec extends Specification {
  "StaticProxy behavior as transactional" should {

    object transactional {
      def apply(output: Buffer[String]) = StaticProxy(
        before = () => output += "begin",
        success = () => output += "commit",
        failure = e => output += ("rollback: " + e.getMessage),
        after = () => output += "cleanup")
    }

    "Do surrounding work for normal flow" in {
      val output = ArrayBuffer[String]()

      def process(): Unit = transactional(output) {
        output += "business done"
      }
      process()

      output must_== ArrayBuffer("begin", "business done", "commit", "cleanup")
    }

    "Do surrounding work for exceptional flow" in {
      val output = ArrayBuffer[String]()

      def process(): Unit = transactional(output) {
        throw new IllegalArgumentException("illegal format")
      }
      process must throwA[IllegalArgumentException]

      output must_== ArrayBuffer("begin", "rollback: illegal format", "cleanup")
    }
  }
}
