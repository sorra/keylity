package github.keylity.side

import scala.collection.mutable.Map

object SideContext {
  val store = new SideBinding[Map[String, Any]]

  /** Call your block with side context variables around */
  def call[R](variables: (String, Any)*)(block: => R): R = call(() => block, variables: _*)

  /** Call your function with side context variables around */
  def call[R](func: () => R, variables: (String, Any)*) = {
    push()
    variables.foreach(apply _)
    try {
      func()
    } finally {
      pop()
    }
  }

  /** Get variable by key */
  def apply[T](key: String): Option[T] = {
    if (store.isEmpty) {
      None
    } else {
      var result: Option[Any] = None
      for (map <- store.stack.toList if result.isEmpty) {
        result = map.get(key)
      }
      result.asInstanceOf[Option[T]]
    }
  }

  /** Put variable (key, value) */
  def apply(kv: (String, Any)): Option[Any] = {
    if (store.isEmpty) {
      push()
    }
    store.get.put(kv._1, kv._2)
  }

  def topOnly[T](key: String): Option[T] = {
    if (store.isEmpty()) {
      None
    } else {
      store.get.get(key).asInstanceOf[Option[T]]
    }
  }

  // no-throw
  private def push(): Unit = store.push(Map())

  // no-throw
  private def pop() = store.pop()
}