package github.keylity

class StaticProxy (before: () => Unit, success: () => Unit, failure: Throwable => Unit, after: () => Unit) {
  
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
  
  def apply(before: () => Unit, success: () => Unit, failure: Throwable => Unit, after: () => Unit) = {
    new StaticProxy(before, success, failure, after)
  }
  
}

class StaticProxyBuilder {
  
}