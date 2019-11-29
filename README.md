fp-blockchain
=============

A purely functional toy blockchain in Scala

This project is based on [Naivechain](https://github.com/lhartikk/naivechain) by [lhartikk](https://github.com/lhartikk)

### Usage

```scala
 jdiaz > ~ > code > scala-workspace > blockchain > scala
Welcome to Scala 2.12.10 (OpenJDK 64-Bit Server VM, Java 11.0.5).
Type in expressions for evaluation. Or try :help.

scala> :load app.scala
Loading app.scala...
import java.security.MessageDigest
import java.util.Date
import java.util.Base64
defined class Block
defined object Blockchain
defined object printc

scala> val chain = Blockchain.example(2)
c: List[Block] = List(Block(0,816534932c2b7154836da6afc367695e6337db8a921823784c14378abed4f7d7,null,1575050168,Genesis Block), Block(1,lsBWjg0lYgUO6SquwYHVPjicjB3QWte2exjRraNsdw8=,816534932c2b7154836da6afc367695e6337db8a921823784c14378abed4f7d7,1575050168,Block1), Block(2,go5Q49jCeyYz0UTHkjshpUEcIF4yfrObKsZe8yBj670=,lsBWjg0lYgUO6SquwYHVPjicjB3QWte2exjRraNsdw8=,1575050168,Block2))

scala> printc(chain)
[
   {
    "index": "0",
    "hash": "816534932c2b7154836da6afc367695e6337db8a921823784c14378abed4f7d7",
    "previousHash": "null",
    "unixtime": 1575050168,
    "data": "Genesis Block"
     },
   {
    "index": "1",
    "hash": "lsBWjg0lYgUO6SquwYHVPjicjB3QWte2exjRraNsdw8=",
    "previousHash": "816534932c2b7154836da6afc367695e6337db8a921823784c14378abed4f7d7",
    "unixtime": 1575050168,
    "data": "Block1"
     },
   {
   "index": "2",
   "hash": "go5Q49jCeyYz0UTHkjshpUEcIF4yfrObKsZe8yBj670=",
   "previousHash": "lsBWjg0lYgUO6SquwYHVPjicjB3QWte2exjRraNsdw8=",
   "unixtime": 1575050168,
   "data": "Block2"
    }
]

scala>
```