package github.keylity.side

import org.specs2.mutable._

class SideBindingSpec extends Specification {
  "SideBinding behavior" should {
    import SideBinding.call

    "Call with simple bindings" in {
      val a = SideBinding[String]
      val b = SideBinding[String]

      a.isEmpty must beTrue; b.isEmpty must beTrue

      call(a->"A", b->"B") {
        a() must_== "A"; b.? must_== Some("B")
      }

      a.? must_== None; b.? must_== None
    }

    "Call with nested bindings" in {
      val a = SideBinding[Int]
      val b = SideBinding[Int]

      a.? must_== None; b.? must_== None

      call(a->1, b->2) {
        (a(), b()) must_== (1, 2)
        call(b->3) {
          (a(), b()) must_== (1, 3)
        }
        (a(), b()) must_== (1, 2)
      }

      a.isEmpty must beTrue; b.isEmpty must beTrue
    }

    "Fail when empty" in {
      val a = SideBinding[Any]
      def get: Unit = a()
      get must throwA[NullPointerException]
    }
  }
}
