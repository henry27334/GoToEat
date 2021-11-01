<?php
require "../DataBaseSelection/DataBaseConfig.php";
$database = $_POST['database'];
$db = new DataBaseConfig();
$conn = mysqli_connect($db->servername, $db->username, $db->password, $database);

$addTable = $_POST['add_table'];
$sql="SELECT name, price FROM " . $addTable . "  WHERE hide = '0' ";
$result = $conn->query($sql) or die($conn->error);

while($row = $result->fetch_assoc())
{
	$output[] = $row;
}

#var_dump ($output);
print(json_encode($output,JSON_UNESCAPED_UNICODE));

$conn->close();
