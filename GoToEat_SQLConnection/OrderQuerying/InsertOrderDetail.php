<?php
require "../DataBaseSelection/DataBaseConfig.php";
$database = $_POST['database'];
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $database);

$order_id = $_POST['order_id'];
$productsize_id = $_POST['productsize_id'];
$customized_id  = $_POST['customized_id'];
$detail_quantity = $_POST['detail_quantity'];
$detail_price = $_POST['detail_price'];

$sql = "INSERT INTO order_detail(order_id, productsize_id, customized_id, detail_quantity, detail_price) VALUES 
('" . $order_id . "','" . $productsize_id . "','" . $customized_id . "','" . $detail_quantity . "','" . $detail_price . "')";

if ($conn->query($sql) === TRUE) {
	echo "Insert Complete";
}else{
	echo "Error Insert";
}

$conn->close();
