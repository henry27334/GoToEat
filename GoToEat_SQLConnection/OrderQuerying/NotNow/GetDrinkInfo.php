<?php
require "../DB_Info/DataBase.php";
$db = new DataBase();
$conn = $db->dbConnect();

$productsize_id = $_POST['productsize_id'];
#$productsize_id = "5021";
$sql = "SELECT product.product_name, product.product_pic, product.product_add ,product_size.product_size 
FROM product
INNER JOIN product_size
ON product.product_id = product_size.product_id AND productsize_id = '" . $productsize_id . "' ";
$result = $conn->query($sql) or die($conn->error);
$rowNum = mysqli_num_rows($result);

if($rowNum > 0){

	while($row = $result->fetch_assoc()){
		$output[] = $row;
	}

	echo json_encode($output,JSON_UNESCAPED_UNICODE);
}
else{
	echo "Error: No drink detail.";
}

$conn->close();




