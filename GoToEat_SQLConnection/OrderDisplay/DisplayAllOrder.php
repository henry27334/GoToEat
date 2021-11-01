<?php
require "../DataBaseSelection/DataBaseConfig.php";
$database = $_POST['database'];
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $database);

$guest_account = $_POST['guest_account'];

$sql = "SELECT drink_order.order_id, drink_order.order_date, SUM(order_detail.detail_quantity) AS 'drinkQuantity', SUM(order_detail.detail_price) AS 'totalPrice' 
FROM order_detail 
INNER JOIN drink_order ON order_detail.order_id = drink_order.order_id 
WHERE drink_order.guest_account = '" . $guest_account . "' AND drink_order.order_status = '1' 
GROUP BY order_detail.order_id
ORDER BY order_detail.order_id DESC";

$result = $conn->query($sql) or die($conn->error);
$rowNum = mysqli_num_rows($result);

if($rowNum > 0){

	while($row = $result->fetch_assoc()){
		$output[] = $row;
	}

	echo json_encode($output,JSON_UNESCAPED_UNICODE);
}
else{
	echo "Error: No history order.";
}

$conn->close();




