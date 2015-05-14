package github.keylity.side

import scala.collection.mutable.Stack

class SideBinding[T] {
  val stackHolder: ThreadLocal[Stack[T]] = new ThreadLocal

  /** Get current value */
  def apply(): T = get

  /** Get current value */
  def get(): T = {
    if (stackHolder.get == null) {
      throw new NullPointerException("Not initialized.")
    } else if (stackHolder.get.isEmpty) {
      throw new NullPointerException("Stack is empty.")
    } else {
      stackHolder.get.top
    }
  }

  /** Get option of current value */
  def ?(): Option[T] = if (isEmpty) None else Some(apply())

  /** Form a raw-typed pair of binding-value, used in SideBinding.call(...) */
  def ->(value: T): (SideBinding[Any], Any) = (this.asInstanceOf[SideBinding[Any]], value)

  /** If this binding has no value now */
  def isEmpty(): Boolean = stackHolder.get == null || stackHolder.get.isEmpty
  
  // no-throw
  private[side] def push(value: T): this.type = {
    checkInit()
    stackHolder.get.push(value)
    this
  }
  
  // no-throw
  private[side] def pop(): T = {
    val value = stackHolder.get.pop()
    checkClear()
    value
  }
  
  private[side] def stack() = stackHolder.get
  
  private def checkInit() = {
    if (stackHolder.get == null) {
      stackHolder.set(Stack())
    }
  }
  private def checkClear() = {
    if (stackHolder.get.isEmpty) {
      stackHolder.remove()      
    }
  }
}

object SideBinding {
  
  /** Creates a new side binding */
  def apply[T]() = new SideBinding[T]
  
  /** Call your block with side bindings around */
  def call[R](bindings: (SideBinding[Any], Any)*)(block: => R): R = call(() => block, bindings: _*)
  
  /** Call your function with side bindings around */
  protected def call[R](func: () => R, bindings: (SideBinding[Any], Any)*) = {
    bindings.foreach { pair => pair._1.push(pair._2) }
    try {
      func()
    } finally {
      bindings.foreach { _._1.pop() }
    }
  }
  
}