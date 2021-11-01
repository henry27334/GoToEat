<?php

require "../DataBaseSelection/DataBaseConfig.php";
$database = $_POST['database'];
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $database);

$sql = "SELECT product.product_name, product.product_pic ,product.product_introduction, COUNT(product.product_name) AS sellingQuantity
FROM drink_order 
INNER JOIN order_detail ON drink_order.order_id = order_detail.order_id
INNER JOIN product_size ON product_size.productsize_id = order_detail.productsize_id
INNER JOIN product ON product.product_id = product_size.product_id
WHERE drink_order.order_status = 1
GROUP BY product.product_name
ORDER BY COUNT(product.product_name) DESC LIMIT 0,10";

$result = $conn->query($sql) or die($conn->error);
$rowNum = mysqli_num_rows($result);

if($rowNum > 0){

	while($row = $result->fetch_assoc()){
		$output[] = $row;
	}

	echo json_encode($output,JSON_UNESCAPED_UNICODE);
}
else{
	echo "Error: No history selling rank.";
}

$conn->close();
