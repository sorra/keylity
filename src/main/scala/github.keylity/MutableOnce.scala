package github.keylity

@deprecated("No need to use it. A lazy val with initializer block is enough and thread-safe.")
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