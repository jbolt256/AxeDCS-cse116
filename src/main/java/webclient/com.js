let username = "";
const socket = io.connect("http://localhost:8080", {transports: ['websocket']});

setupSocket();

function setupSocket() {
    socket.on('LOGIN_PASS', function (event) {
        document.cokie = "auth=" + event
        var newHTML = "        <label for=\"viewAssignmentNumber\">Assignment:</label>\n" +
            "        <input id=\"viewAssignmentNumber\" type=\"number\"><button onclick=\"viewAssignment();\">View Assignment</button><button onclick=\"runAssignment()\">Run Assignment</button><br/>\n" +
            "        <label for=\"viewSlotNumber\">Slot:</label>\n" +
            "        <input id=\"viewSlotNumber\"><button onclick=\"viewSlot();\">View Slot</button><br/>\n" +
            " <input id='productNumber'><input id='newLocationNumber'><button onclick='changeProductLoc();'>Reassign Product</button><br/>" +
            " <input id='productNameNumber'><input id='newName'><button onclick='changeProductName();'>Change Product Name</button>" +

            "        <div id=\"viewArea\"></div>"
        document.getElementById('meta').innerHTML = newHTML;
    });

    socket.on('LOGIN_FAIL', function (event) {
        document.getElementById('login_status').innerHTML = "Authentication information was incorrect."
    });

    socket.on('VIEW_ASSIGNMENT', function (event) {
        document.getElementById('viewArea').innerHTML = event;
    })

    socket.on('VIEW_SLOT', function (event) {
        console.log(event);
        document.getElementById('viewArea').innerHTML = event;
    })
}


function submitUsername() {
    const enteredUsername = document.getElementById("username").value;
    const enteredPassword = document.getElementById("password").value;
    const data = {"username": enteredUsername, "password": enteredPassword}
    if (enteredUsername !== "" && enteredPassword !== "") {
        socket.emit("LOGIN", JSON.stringify(data));
    }
}

function viewSlot() {
    const viewSlotNumber = document.getElementById("viewSlotNumber").value
    socket.emit("VIEW_SLOT", viewSlotNumber)
}

function viewAssignment() {
    const viewAssignmentNumber = document.getElementById("viewAssignmentNumber").value
    socket.emit("VIEW_ASSIGNMENT", viewAssignmentNumber)
}
function runAssignment() {
    const viewAssignmentNumber = document.getElementById("viewAssignmentNumber").value
    socket.emit("RUN_ASSIGNMENT", viewAssignmentNumber)
}
function changeProductLoc() {
    const product = document.getElementById("productNumber").value;
    const newLoc = document.getElementById("newLocationNumber").value;
    socket.emit("CHANGE_PRODUCT", product + "," + newLoc);
}
function changeProductName() {
    const productNewName = document.getElementById("productNameNumber").value;
    const newname = document.getElementById("newName").value;
    socket.emit("CHANGE_PRODUCT_NAME", productNewName + "," + newname);

}