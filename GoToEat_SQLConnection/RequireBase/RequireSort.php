<?php
require "../DataBaseSelection/DataBaseConfig.php";
$database = $_POST['database'];
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $database);

$sql = 'SELECT product.product_name, product.product_pic, product.product_sort, sort.photo AS sortPhoto
FROM product
INNER JOIN sort ON product.product_sort = sort.sort_type
WHERE product.hide = 0';

$result = $conn->query($sql) or die($conn->error);

while($row = $result->fetch_assoc())
{
	$output[] = $row;
}

print(json_encode($output,JSON_UNESCAPED_UNICODE));

$conn->close();
