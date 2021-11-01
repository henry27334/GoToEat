<?php
require "../DataBaseSelection/DataBaseConfig.php";
$database = $_POST['database'];
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $database);

$customizedID = $_POST['customized_id'];
$sql= "SELECT customized.customized_id, product.product_name, customized.sp1_number, customized.sp2_number, customized.sp3_number, customized.add_other,order_detail.detail_price, order_detail.detail_quantity, GROUP_CONCAT(product_case.plus_id ORDER BY product_case.plus_id) AS addgroup, customized_plus.mp_number, customized_plus.plus_quantity
FROM customized 
LEFT JOIN customized_plus ON customized.customized_id = customized_plus.customized_id
INNER JOIN order_detail ON customized.customized_id = order_detail.customized_id
INNER JOIN product_size ON order_detail.productsize_id = product_size.productsize_id
INNER JOIN product ON product_size.product_id = product.product_id
INNER JOIN product_case ON product_case.product_add = product.product_add
WHERE customized.customized_id = '" . $customizedID . "'
GROUP BY customized_plus.mp_number";

$result = $conn->query($sql) or die($conn->error);
$rowNum = mysqli_num_rows($result);

if($rowNum > 0){

	while($row = $result->fetch_assoc()){
		$output[] = $row;
	}

	echo json_encode($output,JSON_UNESCAPED_UNICODE);
}
else{
	echo "Error: No unit usable";
}

$conn->close();




