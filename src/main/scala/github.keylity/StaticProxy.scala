package github.keylity

class StaticProxy (val before: () => Unit, val success: () => Unit, val failure: Throwable => Unit,
                   val after: () => Unit) {
  
  def apply[R](call: => R)(): R = on(() => call)
  
  def on[R](func: () => R)(): R = {
    before()
    try {
      val result = func()
      success()
      result
    } catch {
      case t: Throwable => failure(t); throw t
    } finally {
      after()
    }
  }
  
}

object StaticProxy {
  
  def apply(before: () => Unit, success: () => Unit, failure: Throwable => Unit, after: () => Unit): StaticProxy = {
    new StaticProxy(before, success, failure, after)
  }

  def builder() = new StaticProxyBuilder
}

class StaticProxyBuilder {
  var $before: Option[()=>Unit] = None
  var $success: Option[()=>Unit] = None
  var $failure: Option[Throwable=>Unit] = None
  var $after: Option[()=>Unit] = None

  def before(func: ()=>Unit) = {$before = Some(func); this}
  def success(func: ()=>Unit) = {$success = Some(func); this}
  def failure(func: Throwable=>Unit) = {$failure = Some(func); this}
  def after(func: ()=>Unit) = {$after = Some(func); this}

  def build(): StaticProxy = {
    val noop = ()=>{}
    StaticProxy($before.getOrElse(noop), $success.getOrElse(noop), $failure.getOrElse(t=>{}), $after.getOrElse(noop))
  }
}