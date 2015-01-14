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
