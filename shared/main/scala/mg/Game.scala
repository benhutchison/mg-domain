package mg

import scala.collection.immutable._

trait Api {

  def games: Seq[Game]

  def opens: Seq[String]

  def join(player1: String, player2: String): Game

  def reveal(game: Game, playerNum: Int, c1: Card, c2: Card): (Game, Boolean)
}

object Games {

  val PairCount = 20

  var seq: Int = 1

  def nextSeq() = {
    val s = seq; seq = seq + 1; s
  }

  val games = collection.mutable.Buffer.empty[Game]

  val opens = collection.mutable.Buffer.empty[String]

  def open(player1: String) = {
    opens.+=(player1)
    opens
  }

  def join(player1: String, player2: String): Game = {
    val cards = 1.to(PairCount).flatMap(n =>
      Seq(Card(util.Random.nextInt, n), Card(util.Random.nextInt, n)))

    util.Random.shuffle(cards)

    val game = new Game(nextSeq(), util.Random.nextInt(2) + 1,
      Set(Player(1, player1), Player(2, player2)), cards)
    games.+=(game)
    game
  }

  def reveal(game: Game, playerNum: Int, c1: Card, c2: Card): (Game, Boolean) = {

    val correctPlayer = playerNum == game.currentPlayer
    val cardMatch = c1.num == c2.num

    if (correctPlayer && cardMatch) {
      (game.copy(
        currentPlayer = if (game.currentPlayer == 2) 1 else 2,
        players = game.players.map(p =>
          if (p.id == game.currentPlayer) p.copy(score = p.score + 1) else p),
        cards = game.cards.filterNot(c => c == c1 || c == c2)
      ), true)
    }
    else (game, false)
  }
}

case class Player(id: Int, name: String, score: Int = 0)

case class Game(id: Int, currentPlayer: Int, players: Set[Player], cards: Seq[Card])

case class Card(id: Int, num: Int)