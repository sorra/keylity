keylity
=======
Key utility for Scala, key to success!

They are your building blocks. Feel free to use or modify it!

Built with sbt. Tests are in src/test/scala/, serving as code examples.

###MutableOnce
A value holder whose value can be modified once and only once.

###StaticProxy
Helps you build something like AOP.
```
transactional {
  // access database
}
```

###SideBinding
> There are local binding, global binding ...... and side binding!

```
call(a->1, b->"Wow"){
  println((a[Int], b[String]))
  call(a->2){
    // ...
  }
}
```

You can bind different values to the same symbol in call stacks.

###SideContext
You can bind nested contexts(backed by hash map) in call stacks, and get/set contextual variables.
```
call("a"->1, "b"->"Wow"){
  println((ctx("a")[Int], ctx("b")[String]))
  call("a"->2){
    // ...
  }
}
```
