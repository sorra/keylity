package github.keylity

class MutableOnce[T] (var value: T) {
  
  var muted = false
  
  def apply(): T = this.value
  
  def set(value: T): Unit = {
    if (muted) {
      throw new IllegalStateException("MutableOnce is already muted!")
    } else {
      muted = true
      this.value = value
    }
  }
}