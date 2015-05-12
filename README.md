keylity
=======
Key utility for Scala, key to success!

They are your building blocks. Feel free to use or modify it!

Built with sbt. Tests are in src/test/scala/, serving as code examples.

###StaticProxy
Helps you build something like AOP.
```
transactional {
  // access database
}
```

###SideBinding
You can bind different values to the same symbol in call stacks, even without closure.
> There are local binding, global binding ...... and side binding!

```
val a = SideBinding[Int]
val b = SideBinding[String]
call(a->1, b->"Wow"){
  println((a(), b()))
  call(a->2){
    // ...
  }
}
```
a, b can be global variables.

###SideContext
You can bind nested contexts(backed by hash map) in call stacks, and get/set contextual variables.
```
call("a"->1, "b"->"Wow"){
  println((context("a")[Int], context("b")[String]))
  call("a"->2){
    // ...
  }
}
```
If you are using Servlet or IoC framework, you can also adapt your context as the root of SideContext.
```
class RequestContext extends ContextLike {
  val request: HttpServletRequest = YourCode.getRequest
  override def get(key: String) = Option(request.getAttribute(key))
  override def set(key: String, value: Any) = {
    val old = get(key)
    request.setAttribute(key, value)
    Option(old)
  }
}
val req = new RequestContext
rootCall(req) {
  context("userId", 109913) //set
  call("a"->90) {
    println(context("userId") + " " + context("a")) //get
  }
}
req.get("userId") == Some(109913)
```
If your root context is immutable, you can implement a fake `set` method, and push a new context on it, like this:
```
val req = new RequestContext
rootCall(req) {call() {
  context("userId", 109913) //set
  println(context("userId")) //get
}}
req.get("userId") == None
```
>I will look for a better design in the future.
