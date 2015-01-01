package github.keylity.side

import org.specs2.mutable._

class SideContextSpec extends Specification {
  "SideContext behavior" should {
    import SideContext.call
    val ctx = SideContext

    "Call with simple context" in {
      (ctx[String]("a"), ctx[Int]("b")) must_== (None, None)

      call("a"->"Hello", "b"->42) {
        ctx[String]("a").get + " " + ctx[Int]("b").get must_== "Hello 42"
      }
      (ctx[String]("a"), ctx[Int]("b")) must_== (None, None)
    }

    "Call with nested context" in {
      (ctx[String]("a"), ctx[Int]("b")) must_== (None, None)

      call("a"->"Hello", "b"->42) {
        (ctx[String]("a").get, ctx[Int]("b").get) must_== ("Hello", 42)
        call("b"->99) {
          (ctx[String]("a").get, ctx[Int]("b").get) must_== ("Hello", 99)
        }
      }
      (ctx[String]("a"), ctx[Int]("b")) must_== (None, None)
    }

    "Call nested but get top value only" in {
      call("t"->1) {
        ctx.topOnly[Int]("t") must_== Some(1)
        call() { // Creates an empty context on the top, not containing "t"
          ctx.topOnly[Int]("t") must_== None
        }
      }
    }

    "Change the current value of a context variable" in {
      call("t"->1) {
        ctx("t", 2)  must_== Some(1)
        ctx[Int]("t") must_== Some(2)
      }
    }
  }
}
