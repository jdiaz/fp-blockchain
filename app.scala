
import java.security.MessageDigest
import java.util.Date
import java.util.Base64

case class Block(
  index: BigInt,
  hash: String,
  prevHash: String,
  unixtime: Long,
  data: String
)

object Blockchain {

  def getLastBlock(chain: List[Block]): Option[Block] = chain match {
    case h :: Nil => Some(h)
    case h :: tail => getLastBlock(tail)
    case _ => None
  }

  def isValidNewBlock(prev: Block, newBlock: Block): Boolean = newBlock match {
    case b if (b.index != prev.index + 1) => false
    case b if (calculateHashForBlock(b) != b.hash) => false
    case b if (b.prevHash != prev.hash) => false
    case _ => true
  }
  
  def addBlock(b: Block, chain: List[Block]): List[Block] = 
    getLastBlock(chain) match {
      case None => List(b)
      case Some(x) if (!isValidNewBlock(x, b)) => chain
      case _ => chain ::: List(b)
    }
  
  def calculateHashForBlock(block: Block): String = 
    computeHash(block.index, block.prevHash, block.unixtime, block.data)

  def computeHash(
    index: BigInt,
    hash: String,
    unixtime: Long,
    data: String
  ): String = {
    val md = MessageDigest.getInstance("SHA-256")
    val hexes = md.digest(
      (index + hash + unixtime + data).getBytes()
    )
    Base64.getEncoder().encodeToString(hexes)
  }

  def genesisBlock(): Block = new Block(
    0,
    "816534932c2b7154836da6afc367695e6337db8a921823784c14378abed4f7d7",
    null,
    getUnixTime(),
    "Genesis Block"
  )

  def getUnixTime(): Long = new Date().getTime() / 1000

  def computeNextBlock(blockData: String, chain: List[Block]): Block = {
    val lastBlock = getLastBlock(chain).getOrElse(genesisBlock())
    val nextIndex = lastBlock.index + 1
    val nextUnixtime = getUnixTime()
    val nextHash = computeHash(nextIndex, lastBlock.hash, nextUnixtime, blockData)
    new Block(nextIndex, nextHash, lastBlock.hash, nextUnixtime, blockData)
  }

  def example(n: Int = 1): List[Block] = {
    
    val genesisChain = List(genesisBlock())
    
    def loop(n: Int, chain: List[Block]): List[Block] = n match {
      case i if (i > 0) => {
        val nblock = computeNextBlock(s"Block${chain.length}", chain)
        val nchain = addBlock(nblock, chain)
        loop(i - 1, nchain)
      }
      case _ => chain 
    }
    loop(n, genesisChain)
  }

}

object chainp {
  def apply(l: List[Block]): Unit = 
    println(fromBlockchainToJSON(l))

  def fromBlockchainToJSON(chain: List[Block]): String = {
    def loop(l: List[Block], acc: StringBuilder): String = 
      l match {
        case h :: Nil => loop(List(), acc.append( 
s"""   {
   "index": "${h.index}",
   "hash": "${h.hash}",
   "previousHash": "${h.prevHash}",
   "unixtime": "${h.unixtime}",
   "data": "${h.data}"
    }
]"""))
        case h :: tail => loop(tail, acc.append(
s"""   {
    "index": "${h.index}",
    "hash": "${h.hash}",
    "previousHash": "${h.prevHash}",
    "unixtime": "${h.unixtime}",
    "data": "${h.data}"
     },
"""))
        case _ => acc.toString()
      }
      loop(chain, new StringBuilder("[\n"))
    }
}
