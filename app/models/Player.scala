package models

case class Player(id: Long, team: Team, position: Position, firstName: String, lastName: String) extends Person
