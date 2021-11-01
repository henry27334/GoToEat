<?php
require "../DataBaseSelection/DataBaseConfig.php";
$database = $_POST['database'];
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $database);

$username = $_POST['guest_account'];

$sql = "SELECT drink_order.order_id, order_detail.customized_id, product.product_name, product.product_pic, product_size.product_size, singleplus_1.name AS single1name, singleplus_2.name AS single2name, singleplus_3.name AS single3name, customized.add_other, order_detail.detail_quantity, order_detail.detail_price, product_size.productsize_id ,customized.sp1_number, customized.sp2_number, customized.sp3_number,GROUP_CONCAT(product_case.plus_id ORDER BY product_case.plus_id) AS addgroup
FROM drink_order 
INNER JOIN order_detail ON drink_order.order_id = order_detail.order_id
INNER JOIN customized ON  order_detail.customized_id = customized.customized_id
INNER JOIN product_size ON order_detail.productsize_id = product_size.productsize_id
INNER JOIN product ON product_size.product_id = product.product_id
LEFT JOIN product_case ON product.product_add = product_case.product_add
LEFT JOIN singleplus_1 ON customized.sp1_number = singleplus_1.number
LEFT JOIN singleplus_2 ON customized.sp2_number = singleplus_2.number
LEFT JOIN singleplus_3 ON customized.sp3_number = singleplus_3.number
WHERE drink_order.guest_account = '" . $username . "' AND drink_order.order_status = 0
GROUP BY order_detail.detail_id";

$result = $conn->query($sql) or die($conn->error);
$rowNum = mysqli_num_rows($result);

if($rowNum > 0){

	while($row = $result->fetch_assoc()){
		$output[] = $row;
	}

	echo json_encode($output,JSON_UNESCAPED_UNICODE);
}
else{
	echo "No product selected.";
}

$conn->close();




