package github.keylity.side

import scala.collection.mutable.Map

object SideContext {
  val store = new SideBinding[ContextLike]

  /** Call your block with side context variables around */
  def call[R](variables: (String, Any)*)(block: => R): R = call(() => block, push, variables: _*)

  /** Call your function with side context variables around */
  protected def call[R](func: () => R, push$: () => Unit, variables: (String, Any)*) = {
    push$()
    variables.foreach(apply _)
    try {
      func()
    } finally {
      pop()
    }
  }

  /** Call based on an existing context (you can adapt your application context from IoC framework to ContextLike) */
  def rootCall[R](rootContext: ContextLike)(block: => R): R = {
    if (store.isEmpty) {
      call(() => block, () => push(rootContext))
    } else {
      throw new IllegalStateException("rootCall can only happen on empty SideContext!")
    }
  }

  /** Get variable by key */
  def apply[T](key: String): Option[T] = {
    if (store.isEmpty) {
      None
    } else {
      var result: Option[Any] = None
      for (layer <- store.stack.toList if result.isEmpty) {
        result = layer.get(key)
      }
      result.asInstanceOf[Option[T]]
    }
  }

  /** Put variable (key, value) */
  def apply(kv: (String, Any)): Option[Any] = {
    if (store.isEmpty) {
      push()
    }
    store.get.set(kv._1, kv._2)
  }

  def topOnly[T](key: String): Option[T] = {
    if (store.isEmpty()) {
      None
    } else {
      store.get.get(key).asInstanceOf[Option[T]]
    }
  }

  // no-throw
  private def push(): Unit = store.push(new SideContextFrame)

  // no-throw
  private def push(context: ContextLike) = store.push(context)

  // no-throw
  private def pop() = store.pop()
}

private class SideContextFrame extends ContextLike {
  private val map = Map[String, Any]()

  override def get(key: String): Option[Any] = map.get(key)

  override def set(key: String, value: Any): Option[Any] = map.put(key, value)
}