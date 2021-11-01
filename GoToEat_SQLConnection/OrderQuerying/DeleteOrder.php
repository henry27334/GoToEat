<?php
require "../DataBaseSelection/DataBaseConfig.php";
$database = $_POST['database'];
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $database);

$customized_id = $_POST['customized_id'];
$sql = "DELETE FROM customized WHERE customized_id = '" . $customized_id . "'";

$order_id = $_POST['order_id'];

if ($conn->query($sql) === TRUE) {
	echo "Delete Single Order Successed. ";
	
	$sql = "SELECT COUNT(detail_id) AS currentNum FROM order_detail 
			INNER JOIN drink_order ON drink_order.order_id = order_detail.order_id
			WHERE drink_order.order_id = '" . $order_id . "'";  //檢查總訂單的商品比數
	$result = $conn->query($sql) or die($conn->error);
	$row = mysqli_fetch_assoc($result);
	if($row['currentNum'] == "0"){
		
		$sql = "DELETE FROM drink_order WHERE order_id = '" . $order_id . "'"; //刪除商品比數為0的總訂單
		if ($conn->query($sql) === TRUE) {
			echo "\n No product selected, delete total order.";
		}
	}
	
} else {
	echo "Error: " . $sql . "<br>" . $conn->error;
}

$conn->close();
