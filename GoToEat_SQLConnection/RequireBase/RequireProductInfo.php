<?php

require "../DataBaseSelection/DataBaseConfig.php";
$database = $_POST['database'];
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $database);

$productName = $_POST['product_name'];
$sql="SELECT product.product_id, product.product_sort, product_pic, product.product_add, product.product_introduction, product_size.productsize_id, product_size.product_size, product_size.product_price, GROUP_CONCAT(product_case.plus_id ORDER BY product_case.plus_id) AS plus_id, GROUP_CONCAT(table_unit.Unit) AS plus_id_cn, product_size.size
FROM product 
INNER JOIN product_size ON product.product_id=product_size.product_id
INNER JOIN product_case ON product.product_add = product_case.product_add
LEFT JOIN table_unit ON product_case.plus_id = table_unit.ID
WHERE product.product_name = '" . $productName . "' AND product_size.hide = '0'
GROUP BY product_size.productsize_id
ORDER BY product_size.size";

$result = $conn->query($sql) or die($conn->error);

while($row = $result->fetch_assoc())
{
	$output[] = $row;
}

#var_dump ($output);
print(json_encode($output,JSON_UNESCAPED_UNICODE));

$conn->close();
