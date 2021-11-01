<?php
require "../DataBaseSelection/DataBaseConfig.php";
$database = $_POST['database'];
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $database);

$productName = $_POST['product_Name'];

$sql = "SELECT DISTINCT order_detail.detail_id, drink_order.guest_account ,comment.comment, comment.comment_score
FROM drink_order 
inner join order_detail on drink_order.order_id = order_detail.order_id 
left join product_size on product_size.productsize_id = order_detail.productsize_id 
left join product on product.product_id = product_size.product_id 
left join customized on customized.customized_id = order_detail.customized_id 
left join singleplus_1 on customized.sp1_number = singleplus_1.number 
left join singleplus_2 on customized.sp2_number = singleplus_2.number 
left join singleplus_3 on customized.sp3_number = singleplus_3.number 
left join customized_plus on customized.customized_id = customized_plus.customized_id 
left join multipleplus on customized_plus.mp_number = multipleplus.number 
LEFT join comment on order_detail.detail_id = comment.detail_id
WHERE product.product_name = '" . $productName . "' AND comment.recommend = '1'
ORDER BY drink_order.order_date DESC";

$result = $conn->query($sql) or die($conn->error);
$rowNum = mysqli_num_rows($result);

if($rowNum > 0){

	while($row = $result->fetch_assoc()){
		$output[] = $row;
	}

	echo json_encode($output,JSON_UNESCAPED_UNICODE);
}
else{
	echo "Error: No drink comment.";
}

$conn->close();




