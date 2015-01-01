package github.keylity.side

import org.specs2.mutable._

class SideBindingSpec extends Specification {
  "SideBinding behavior" should {
    val a = SideBinding()
    val b = SideBinding()
    import SideBinding.call

    "Call with simple bindings" in {
      a.isEmpty must beTrue; b.isEmpty must beTrue

      call(a->"A", b->"B") {
        a[String] must_== "A"; b.optionAs[String] must_== Some("B")
      }
      a.option must_== None; b.option must_== None
    }

    "Call with nested bindings" in {
      a.isEmpty must beTrue; b.isEmpty must beTrue

      call(a->1, b->2) {
        (a[Int], b[Int]) must_== (1, 2)
        call(b->3) {
          (a[Int], b[Int]) must_== (1, 3)
        }
        (a[Int], b[Int]) must_== (1, 2)
      }
      a.isEmpty must beTrue; b.isEmpty must beTrue
    }

    "Fail when empty" in {
      def get: Unit = a()
      get must throwA[NullPointerException]
    }
  }
}
