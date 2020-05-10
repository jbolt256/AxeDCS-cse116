package server

object Auth {
  val usernames:List[String]         = List("00")
  val credentials:Map[String,String] = Map("00" -> "0")

  def login(username:String, password:String): Boolean = {
    if ( this.usernames.contains(username) ) {
      this.credentials(username) == password
    } else {
      false
    }
  }

}
