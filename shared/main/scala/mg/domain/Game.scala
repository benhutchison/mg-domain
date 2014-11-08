package mg

package domain

import scala.concurrent._


trait Api {

  def join(player: String): Future[Game]

  def games(): Iterable[Game]

  def reveal(game: Game, playerId: Int, card: Card): Game

  def concede(game: Game, playerId: Int): Game

  def waitOnUpdate(game: Game): Future[Game]
}

case class Player(id: Int, name: String, score: Int = 0)

case class Game(id: Int,
                currentPlayerId: Int,
                players: Map[Int, Player],
                cards: Seq[Card],
                revealed: Seq[Card] = Seq.empty,
                matched: Seq[Card] = Seq.empty,
                turn: Int = 0,
                winnerId: Option[Int] = None) {

  def reveal(card: Card): Game = {
    if (revealed.size < 2 && !revealed.contains(card))
      copy(revealed = this.revealed :+ card)
    else
      this
  }

  def advanceTurn(): Game = {
    if (revealed.size != 2)
      return this

    val Seq(c1, c2) = revealed

    val isMatch = c1.num == c2.num
    val nextMatched = if (isMatch)
       matched :+ c1 :+ c2
      else
        matched
    val scoreIncr = if (isMatch) 1 else 0
    val gameWon = matched.size == cards.size
    if (gameWon)
      copy(winnerId = Some(currentPlayerId))
    else
      copy(
        currentPlayerId =
          players.filterKeys(_ != currentPlayerId).head._1,
        players =
          players.adjust(currentPlayerId)(p => p.copy(score = p.score + scoreIncr)),
        matched =
          nextMatched,
        revealed =
          Seq.empty,
        turn =
          turn + 1
      )
  }

  def concede(concedingPlayerId: Int) = {
    if (winnerId.isDefined)
      this
    else
      copy(winnerId = Some(opponent(concedingPlayerId).id))
  }

  def opponent(playerId: Int): Player = {
    players.filterKeys(_ != playerId).head._2
  }

  def currentPlayerName = players(currentPlayerId).name

  def ended = winnerId.isDefined

  override def toString = {
    def playerLabel(p: Player) = {
      val extra = if (winnerId.isDefined && winnerId.get == p.id)
        "Winner, "
      else if (p.id == currentPlayerId)
        s"To move, "
      else ""
      s"${players(p.id).name}(${extra}Score=${p.score})"
    }
    val turnLabel = s"(${if(ended) "Ended, " else ""}turn=$turn)"
    val playerLabels = players.values.map(playerLabel).mkString("  ")
    s"Game: $turnLabel $playerLabels"
  }
}

case class Card(id: Int, num: Int)