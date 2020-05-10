package server
import com.corundumstudio.socketio._
import com.corundumstudio.socketio.listener.{ConnectListener, DataListener, DisconnectListener}
import scala.io.Source
import play.api.libs.json.Json
import scala.util.Random

import warehouse.{Outloading, Layout}
import warehouse.batches.Schedule

class Server(val configuration: String) {

  val config: Configuration = new Configuration {
    setHostname("localhost")
    setPort(8080)
  }

  var AuthorizedClients:Map[String, SocketIOClient] = Map()
  val server: SocketIOServer = new SocketIOServer(config)
  var AuthTokens:List[String] = List()
  server.addConnectListener(new cncListener(this))
  server.addDisconnectListener(new dscListener(this))
  server.addEventListener("LOGIN", classOf[String], new LoginListener(this) )
  server.addEventListener("VIEW_ASSIGNMENT", classOf[String], new ViewAssignmentListener(this))
  server.addEventListener("RUN_ASSIGNMENT", classOf[String], new RunAssignmentListener(this))
  server.addEventListener("VIEW_SLOT", classOf[String], new ViewSlotListener(this))
  server.addEventListener("CHANGE_PRODUCT", classOf[String], new ChangeProductListener(this))
  server.addEventListener("CHANGE_PRODUCT_NAME", classOf[String], new ChangeProductNameListener(this))
  server.start()

  def isAuthorized(username:String):Boolean = {
    this.AuthorizedClients.keys.toList.contains(username)
  }
  
}

class cncListener(f:Server) extends ConnectListener {
  override def onConnect(client: SocketIOClient): Unit = {
    println("Connected: " + client)
  }
}

class dscListener(f:Server) extends DisconnectListener {
  override def onDisconnect(socket: SocketIOClient): Unit = {
    if ( f.AuthorizedClients.values.toList.contains(socket) ) {
      f.AuthorizedClients -= f.AuthorizedClients.keys.toList(f.AuthorizedClients.values.toList.indexOf(socket))
    }
    println("Disconnected: " + socket)
  }
}

class LoginListener(f:Server) extends DataListener[String] {
  override def onData(socket: SocketIOClient, data: String, ackRequest: AckRequest): Unit = {
    val username:String = (Json.parse(data) \ "username").as[String]
    val password:String = (Json.parse(data) \ "password").as[String]
    println(username,password)
    if ( Auth.login(username,password) ) {
      val token:String = Random.alphanumeric.take(8).mkString
      f.AuthTokens = f.AuthTokens :+ token
      socket.sendEvent("LOGIN_PASS", token)
    } else {
      socket.sendEvent("LOGIN_FAIL", "")
    }
  }
}

class ViewAssignmentListener(f:Server) extends DataListener[String] {
  override def onData(socket: SocketIOClient, data: String, ackRequest: AckRequest): Unit = {
    val assignmentNum:Int = data.toInt
    println(assignmentNum)
    if ( Outloading.assignments.keys.toList.contains(assignmentNum)) {
      socket.sendEvent("VIEW_ASSIGNMENT", Schedule.formAssignment(Outloading.assignments(assignmentNum)))
    } else {
      socket.sendEvent("VIEW_ASSIGNMENT", "Assignment not found.")
    }
    println("viewassign")
  }
}

class RunAssignmentListener(f:Server) extends DataListener[String] {
  override def onData(socket: SocketIOClient, data: String, ackRequest: AckRequest): Unit = {
    val assignmentNum:Int = data.toInt
    if ( Outloading.assignments.keys.toList.contains(assignmentNum)) {
      socket.sendEvent("RUN_ASSIGNMENT", Schedule.runAssignment(Outloading.assignments(assignmentNum)))
    } else {
      socket.sendEvent("RUN_ASSIGNMENT", "Assignment not found.")
    }
    println("runassign")
  }
}

class ViewSlotListener(f:Server) extends DataListener[String] {
  override def onData(socket: SocketIOClient, data: String, ackRequest: AckRequest): Unit = {
    val viewSlot:Array[String] = data.split("-")
    if ( Layout.aisles.keys.toList.contains(viewSlot(0).toInt) ) {
      socket.sendEvent("VIEW_SLOT", "PRODUCT: " + Layout.products(Layout.aisles(viewSlot(0).toInt).items(viewSlot(1).toInt).product).name + "; QUANTITY: " + Layout.aisles(viewSlot(0).toInt).items(viewSlot(1).toInt).quantity.toString)
    } else {
      socket.sendEvent("VIEW_SLOT", "Bay not found.")
    }
    println("viewslot")
  }
}

class ChangeProductListener(f:Server) extends DataListener[String] {
  override def onData(socket: SocketIOClient, data: String, ackRequest: AckRequest): Unit = {
    // format: product,newlocation
    val products:Array[String] = data.split(",")
    val productID:Int = products(0).toInt
    val aisle:Int = products(1).split("-")(0).toInt
    val slot:Int = products(1).split("-")(1).toInt
    if ( Layout.aisles.keys.toList.contains(aisle) ) {
      Layout.aisles(aisle).items(slot).product = productID
    } else {
      socket.sendEvent("CHANGE_PRODUCT", "Bay not found, or something...")
    }
    println("changeproduct")
  }
}

class ChangeProductNameListener(f:Server) extends DataListener[String] {
  override def onData(socket: SocketIOClient, data: String, ackRequest: AckRequest): Unit = {
    // format: product,newname
    val products:Array[String] = data.split(",")
    val productID:Int = products(0).toInt
    val newName:String = products(1)
    if ( Layout.products.keys.toList.contains(productID)) {
      Layout.products(productID).name = newName
    } else {
      socket.sendEvent("CHANGE_PRODUCT_NAME", "Product not found.")
    }
    println("changeproductname")
  }
}