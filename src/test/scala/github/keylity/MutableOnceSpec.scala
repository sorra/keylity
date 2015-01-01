package github.keylity

import org.specs2.mutable._

class MutableOnceSpec extends Specification {
  "MutableOnce behavior" should {
    "Create, mute once and twice like" in {
      val id = new MutableOnce[Int](0)
      id() must_== 0

      id.set(1)
      id() must_== 1

      id.set(2) must throwA[IllegalStateException]
    }
  }
}
