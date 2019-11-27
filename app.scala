
import java.security.MessageDigest
import java.util.Date

case class Block(
	index: BigInt,
	hash: String,
	prevHash: String,
	timestamp: Long,
	data: String
)

object Blockchain {

	def getLastBlock(chain: List[Block]): Option[Block] = chain match {
		case h :: Nil => Some(h)
		case h :: tail => getLastBlock(tail)
		case _ => None
	}

	def isValidNewBlock(prev: Block, newBlock: Block): Boolean = { 
		if (newBlock.index != prev.index + 1)
			return false
	 	if (calculateHashForBlock(newBlock) != newBlock.hash)
			return false
		if (newBlock.prevHash != prev.hash)
			return false
		true
	}

	def addBlock(b: Block, chain: List[Block]): List[Block] = {
		val last = getLastBlock(chain)
		if (
			!last.isEmpty && !isValidNewBlock(last.get, b)
		) {
			return chain
		}
		if (last.isEmpty && chain.isEmpty)
			return List(b)

		chain ::: List(b)
	}

	def calculateHashForBlock(block: Block): String = 
    computeHash(block.index, block.prevHash, block.timestamp, block.data)

	def computeHash(index: BigInt, hash: String, timestamp: Long, data: String): String = {
		val md = MessageDigest.getInstance("SHA-256")
		md.digest((index + hash + timestamp + data).getBytes()).toString()
	}


	def genesisBlock(): Block = new Block(
		0,
		"816534932c2b7154836da6afc367695e6337db8a921823784c14378abed4f7d7",
		null,
		new Date().getTime() / 1000,
		"Genesis Block"
	)

	def computeNextBlock(blockData: String, chain: List[Block]): Block = {
		val lastBlock = getLastBlock(chain).getOrElse(genesisBlock())
    val nextIndex = lastBlock.index + 1
    val nextTimestamp = new Date().getTime() / 1000
    val nextHash = computeHash(nextIndex, lastBlock.hash, nextTimestamp, blockData)
    new Block(nextIndex, nextHash, lastBlock.hash, nextTimestamp, blockData)
	}

	def example(): List[Block] = {
		val chain1 = List(genesisBlock())
		val sec = computeNextBlock("Bob", chain1)
		println(sec)
	  addBlock(sec, chain1)
	}

}

object chainp {
	def apply(l: List[Block]): Unit = 
		println(fromBlockchainToJSON(l))

	def fromBlockchainToJSON(chain: List[Block]): String = {
		def loop(l: List[Block], acc: String): String = 
			l match {
				case h :: Nil => loop(List(), acc + 
s"""   {
	 "index": "${h.index}",
	 "hash": "${h.hash}",
	 "previousHash": "${h.prevHash}",
	 "timestamp": "${h.timestamp}",
	 "data": "${h.data}"
    }
]""")
				case h :: tail => loop(tail, acc + 
s"""   {
		"index": "${h.index}",
		"hash": "${h.hash}",
		"previousHash": "${h.prevHash}",
		"timestamp": "${h.timestamp}",
		"data": "${h.data}"
     },
""")
				case _ => acc
			}
			loop(chain, "[\n")
		}
}
