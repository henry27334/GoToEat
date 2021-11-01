<?php
require "../DataBaseSelection/DataBase.php";
$db = new DataBase();
if (isset($_POST['guest_account']) && isset($_POST['guest_password']) && isset($_POST['guest_name']) && isset($_POST['guest_phone']) && isset($_POST['guest_mail']) && isset($_POST['guest_address'])) {
    if ($db->dbConnect()) {
        if ($db->signUp("guest", $_POST['guest_account'], $_POST['guest_password'], $_POST['guest_name'], $_POST['guest_phone'] , $_POST['guest_mail'] , $_POST['guest_address'])) {
            echo "註冊成功，將轉跳至登入畫面!";
        } else echo "註冊失敗!";
    } else echo "Error: Database connection";
} else echo "錯誤，所有資訊必需填寫。";
?>
