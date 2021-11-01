<?php
require "../DataBaseSelection/DataBaseConfig.php";
$database = $_POST['database'];
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $database);

$guest_account = $_POST['guest_account'];
$sql = "SELECT order_id FROM drink_order WHERE guest_account = '" . $guest_account . "' AND order_status = '0' ";
$result = $conn->query($sql) or die($conn->error);
$row = mysqli_fetch_assoc($result);
$order_id = $row['order_id'];

$sql = "UPDATE drink_order SET order_status = '1' WHERE order_id = '" . $order_id . "'";
if ($conn->query($sql) === TRUE) {
	echo "Update Order Status Successed";
} else {
	echo "Error: " . $sql . "<br>" . $conn->error;
}

$conn->close();
