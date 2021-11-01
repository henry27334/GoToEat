<?php
require "../DB_Info/DataBase.php";
$db = new DataBase();
$conn = $db->dbConnect();

$guest_account = $_POST['guest_account'];
#$guest_account = "A111111";
$sql = "SELECT COUNT(order_detail.detail_id)
FROM order_detail
WHERE order_detail.order_id = 
(SELECT order_id 
FROM drink_order 
WHERE drink_order.guest_account = '" . $guest_account . "' AND drink_order.order_status = 0)";
$result = $conn->query($sql) or die($conn->error);
$rowNum = mysqli_num_rows($result);

if($rowNum > 0){
	$row = $result->fetch_assoc();
	$drinkCount = $row['COUNT(order_detail.detail_id)']; 
	echo $drinkCount;
}
else{
	echo 0;
}

$conn->close();




